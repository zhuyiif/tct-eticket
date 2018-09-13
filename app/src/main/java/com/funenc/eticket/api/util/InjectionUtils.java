package com.funenc.eticket.api.util;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

public class InjectionUtils {

    public static boolean isConcreteClass(Class<?> cls) {
        return !cls.isInterface() && !Modifier.isAbstract(cls.getModifiers());
    }

    public static MultivaluedMap<String, Object> extractValuesFromBean(Object bean, String baseName) {
        MultivaluedMap<String, Object> values = new MetadataMap<String, Object>();
        fillInValuesFromBean(bean, baseName, values);
        return values;
    }

    public static void fillInValuesFromBean(Object bean, String baseName,
                                            MultivaluedMap<String, Object> values) {
        for (Method m : bean.getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0
                    && m.getName().length() > 3) {
                String propertyName = m.getName().substring(3);
                if (propertyName.length() == 1) {
                    propertyName = propertyName.toLowerCase();
                } else {
                    propertyName = propertyName.substring(0, 1).toLowerCase()
                            + propertyName.substring(1);
                }
                if (baseName.contains(propertyName)
                        || "class".equals(propertyName)
                        || "declaringClass".equals(propertyName)) {
                    continue;
                }
                if (!"".equals(baseName)) {
                    propertyName = baseName + "." + propertyName;
                }

                Object value = extractFromMethod(bean, m);
                if (value == null) {
                    continue;
                }
                if (isPrimitive(value.getClass())) {
                    values.putSingle(propertyName, value);
                } else if (isSupportedCollectionOrArray(value.getClass())) {
                    List<Object> theValues = null;
                    if (value.getClass().isArray()) {
                        theValues = Arrays.asList(value);
                    } else if (value instanceof Set) {
                        theValues = new ArrayList<Object>((Set<?>)value);
                    } else {
                        theValues = CastUtils.cast((List<?>)value);
                    }
                    values.put(propertyName, theValues);
                } else {
                    fillInValuesFromBean(value, propertyName, values);
                }
            }
        }
    }

    public static Object extractFromMethod(Object requestObject,
                                           Method method) {
        try {
            Method methodToInvoke = checkProxy(method, requestObject);
            return methodToInvoke.invoke(requestObject);
        } catch (IllegalAccessException ex) {
            reportServerError("METHOD_ACCESS_FAILURE", method.getName());
        } catch (Exception ex) {
            reportServerError("METHOD_INJECTION_FAILURE", method.getName());
        }
        return null;
    }

    public static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
                || Number.class.isAssignableFrom(type)
                || Boolean.class.isAssignableFrom(type)
                || String.class == type;
    }

    public static boolean isSupportedCollectionOrArray(Class<?> type) {
        return Collection.class.isAssignableFrom(type) || type.isArray();
    }

    public static Method checkProxy(Method methodToInvoke, Object resourceObject) {
        if (Proxy.class.isInstance(resourceObject)) {
            for (Class<?> c : resourceObject.getClass().getInterfaces()) {
                try {
                    Method m = c.getMethod(
                            methodToInvoke.getName(), methodToInvoke.getParameterTypes());
                    if (m != null) {
                        return m;
                    }
                } catch (NoSuchMethodException ex) {
                    //ignore
                }
            }
        }
        return methodToInvoke;
    }

    public static void reportServerError(String messageName, String parameter) {
        Log.i(messageName, parameter);
        throw new RuntimeException(messageName+":"+parameter);
    }
}
