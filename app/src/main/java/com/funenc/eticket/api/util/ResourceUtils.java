package com.funenc.eticket.api.util;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

public class ResourceUtils {
    //// for OperationResourceInfo

    public static <T> List<T> cast(List<?> p, Class<T> cls) {
        return (List<T>)p;
    }
    public static List<Parameter> getParameters(Method resourceMethod) {
        Annotation[][] paramAnns = resourceMethod.getParameterAnnotations();
        if (paramAnns.length == 0) {
            return cast(Collections.emptyList(), Parameter.class);
        }
        Class<?>[] types = resourceMethod.getParameterTypes();
        List<Parameter> params = new ArrayList<Parameter>(paramAnns.length);
        for (int i = 0; i < paramAnns.length; i++) {
            Parameter p = getParameter(i, paramAnns[i], types[i]);
            params.add(p);
        }
        return params;
    }

    public static Parameter getParameter(int index, Annotation[] anns, Class<?> type) {

        Context ctx = AnnotationUtils.getAnnotation(anns, Context.class);
        if (ctx != null) {
            return new Parameter(ParameterType.CONTEXT, index, null);
        }

        boolean isEncoded = AnnotationUtils.getAnnotation(anns, Encoded.class) != null;
        String dValue = AnnotationUtils.getDefaultParameterValue(anns);

        Parameter p = null;

        PathParam a = AnnotationUtils.getAnnotation(anns, PathParam.class);
        if (a != null) {
            p = new Parameter(ParameterType.PATH, index, a.value(), isEncoded, dValue);
        }
        if (p == null) {
            QueryParam q = AnnotationUtils.getAnnotation(anns, QueryParam.class);
            if (q != null) {
                p = new Parameter(ParameterType.QUERY, index, q.value(), isEncoded, dValue);
            }
        }
        if (p != null) {
            return p;
        }

        MatrixParam m = AnnotationUtils.getAnnotation(anns, MatrixParam.class);
        if (m != null) {
            return new Parameter(ParameterType.MATRIX, index, m.value(), isEncoded, dValue);
        }

        FormParam f = AnnotationUtils.getAnnotation(anns, FormParam.class);
        if (f != null) {
            return new Parameter(ParameterType.FORM, index, f.value(), isEncoded, dValue);
        }

        HeaderParam h = AnnotationUtils.getAnnotation(anns, HeaderParam.class);
        if (h != null) {
            return new Parameter(ParameterType.HEADER, index, h.value(), isEncoded, dValue);
        }

        p = null;
        CookieParam c = AnnotationUtils.getAnnotation(anns, CookieParam.class);
        if (c != null) {
            p = new Parameter(ParameterType.COOKIE, index, c.value(), isEncoded, dValue);
        } else {
            p = new Parameter(ParameterType.REQUEST_BODY, index, null);
        }

        return p;
    }

    //// for createClassResourceInfo

    public static ClassResourceInfo createClassResourceInfo(Class rClass, Class sClass, boolean isRoot, boolean enableStatic) {
        ClassResourceInfo cri = new ClassResourceInfo(rClass, sClass, isRoot, enableStatic);

        if (isRoot) {
            URITemplate t = URITemplate.createTemplate(cri.getPath());
            cri.setURITemplate(t);
        }

        evaluateResourceClass(cri, enableStatic);
        return checkMethodDispatcher(cri) ? cri : null;
    }

    private static void evaluateResourceClass(ClassResourceInfo cri, boolean enableStatic) {
        MethodDispatcher md = new MethodDispatcher();
        for (Method m : cri.getServiceClass().getMethods()) {

            Method annotatedMethod = AnnotationUtils.getAnnotatedMethod(m);

            String httpMethod = AnnotationUtils.getHttpMethodValue(annotatedMethod);
            Path path = AnnotationUtils.getMethodAnnotation(annotatedMethod, Path.class);

            if (httpMethod != null || path != null) {
                md.bind(createOperationInfo(m, annotatedMethod, cri, path, httpMethod), m);
                if (httpMethod == null) {
                    Log.e("RESOURCE_OP_EXC", "missing http method");
                }
            }
        }
        cri.setMethodDispatcher(md);
    }

    private static boolean checkMethodDispatcher(ClassResourceInfo cr) {
        if (cr.getMethodDispatcher().getOperationResourceInfos().isEmpty()) {
            Log.w("NO_RESOURCE_OP_EXC",
                    cr.getServiceClass().getName().toString());
            return false;
        }
        return true;
    }

    private static OperationResourceInfo createOperationInfo(Method m, Method annotatedMethod,
                                                             ClassResourceInfo cri, Path path, String httpMethod) {
        OperationResourceInfo ori = new OperationResourceInfo(m, annotatedMethod, cri);
        URITemplate t = URITemplate.createTemplate(path);
        ori.setURITemplate(t);
        ori.setHttpMethod(httpMethod);
        return ori;
    }
}
