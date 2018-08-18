package com.example.eticket.api.util;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class MethodDispatcher {
    private Map<OperationResourceInfo, Method> oriToMethod =
            new LinkedHashMap<OperationResourceInfo, Method>();
    private Map<Method, OperationResourceInfo> methodToOri =
            new LinkedHashMap<Method, OperationResourceInfo>();

    public MethodDispatcher() {

    }

    MethodDispatcher(MethodDispatcher md, ClassResourceInfo cri) {
        for (OperationResourceInfo ori : md.getOperationResourceInfos()) {
            OperationResourceInfo clone = new OperationResourceInfo(ori, cri);
            oriToMethod.put(clone, clone.getMethodToInvoke());
            methodToOri.put(clone.getMethodToInvoke(), clone);
        }
    }

    public void bind(OperationResourceInfo o, Method... methods) {
        Method primary = methods[0];

        for (Method m : methods) {
            methodToOri.put(m, o);
        }

        oriToMethod.put(o, primary);
    }

    public OperationResourceInfo getOperationResourceInfo(Method method) {
        return methodToOri.get(method);
    }

    public Set<OperationResourceInfo> getOperationResourceInfos() {
        return oriToMethod.keySet();
    }

    public Method getMethod(OperationResourceInfo op) {
        return oriToMethod.get(op);
    }
}

