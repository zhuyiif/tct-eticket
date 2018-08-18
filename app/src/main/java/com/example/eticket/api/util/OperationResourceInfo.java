package com.example.eticket.api.util;

import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class OperationResourceInfo {
    private URITemplate uriTemplate;
    private ClassResourceInfo classResourceInfo;
    private Method methodToInvoke;
    private Method annotatedMethod;
    private String httpMethod;
    private List<MediaType> produceMimes;
    private List<MediaType> consumeMimes;
    private boolean encoded;
    private String defaultParamValue;
    private List<Parameter> parameters;
    private boolean oneway;

    public OperationResourceInfo(Method mInvoke, ClassResourceInfo cri) {
        this(mInvoke, mInvoke, cri);
    }

    OperationResourceInfo(OperationResourceInfo ori, ClassResourceInfo cri) {
        this.uriTemplate = ori.uriTemplate;
        this.methodToInvoke = ori.methodToInvoke;
        this.annotatedMethod = ori.annotatedMethod;
        this.httpMethod = ori.httpMethod;
        this.produceMimes = ori.produceMimes;
        this.consumeMimes = ori.consumeMimes;
        this.encoded = ori.encoded;
        this.defaultParamValue = ori.defaultParamValue;
        this.parameters = ori.parameters;
        this.oneway = ori.oneway;
        this.classResourceInfo = cri;
    }

    public OperationResourceInfo(Method mInvoke, Method mAnnotated, ClassResourceInfo cri) {
        methodToInvoke = mInvoke;
        annotatedMethod = mAnnotated;
        if (mAnnotated != null) {
            parameters = ResourceUtils.getParameters(mAnnotated);
        }
        classResourceInfo = cri;
        checkMediaTypes(null, null);
        checkEncoded();
        checkDefaultParameterValue();
        checkOneway();
    }

    //CHECKSTYLE:OFF
    public OperationResourceInfo(Method m,
                                 ClassResourceInfo cri,
                                 URITemplate template,
                                 String httpVerb,
                                 String consumeMediaTypes,
                                 String produceMediaTypes,
                                 List<Parameter> params,
                                 boolean oneway) {
        //CHECKSTYLE:ON
        methodToInvoke = m;
        annotatedMethod = null;
        classResourceInfo = cri;
        uriTemplate = template;
        httpMethod = httpVerb;
        checkMediaTypes(consumeMediaTypes, produceMediaTypes);
        parameters = params;
        this.oneway = oneway;
    }

    private void checkOneway() {
        if (annotatedMethod != null) {
            // ignore, do not support temporarily
//            oneway = AnnotationUtils.getAnnotation(annotatedMethod.getAnnotations(), Oneway.class) != null;
        }
    }

    public boolean isOneway() {
        return oneway;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public URITemplate getURITemplate() {
        return uriTemplate;
    }

    public void setURITemplate(URITemplate u) {
        uriTemplate = u;
    }

    public ClassResourceInfo getClassResourceInfo() {
        return classResourceInfo;
    }

    public Method getMethodToInvoke() {
        return methodToInvoke;
    }

    public Method getAnnotatedMethod() {
        return annotatedMethod;
    }

    public void setMethodToInvoke(Method m) {
        methodToInvoke = m;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String m) {
        httpMethod = m;
    }

    public boolean isSubResourceLocator() {
        return httpMethod == null ? true : false;
    }


    public List<MediaType> getProduceTypes() {

        return produceMimes;
    }

    public List<MediaType> getConsumeTypes() {

        return consumeMimes;
    }

    private void checkMediaTypes(String consumeMediaTypes,
                                 String produceMediaTypes) {
        if (consumeMediaTypes != null) {
            consumeMimes = JAXRSUtils.sortMediaTypes(consumeMediaTypes);
        } else {
            Consumes cm =
                    AnnotationUtils.getMethodAnnotation(annotatedMethod, Consumes.class);
            if (cm != null) {
                consumeMimes = JAXRSUtils.sortMediaTypes(JAXRSUtils.getMediaTypes(cm.value()));
            } else if (classResourceInfo != null) {
                consumeMimes = JAXRSUtils.sortMediaTypes(classResourceInfo.getConsumeMime());
            }
        }
        if (produceMediaTypes != null) {
            produceMimes = JAXRSUtils.sortMediaTypes(produceMediaTypes);
        } else {
            Produces pm =
                    AnnotationUtils.getMethodAnnotation(annotatedMethod, Produces.class);
            if (pm != null) {
                produceMimes = JAXRSUtils.sortMediaTypes(JAXRSUtils.getMediaTypes(pm.value()));
            } else if (classResourceInfo != null) {
                produceMimes = JAXRSUtils.sortMediaTypes(classResourceInfo.getProduceMime());
            }
        }
    }

    public boolean isEncodedEnabled() {
        return encoded;
    }

    public String getDefaultParameterValue() {
        return defaultParamValue;
    }

    private void checkEncoded() {
        encoded = AnnotationUtils.getMethodAnnotation(annotatedMethod,
                Encoded.class) != null;
        if (!encoded && classResourceInfo != null) {
            encoded = AnnotationUtils.getClassAnnotation(classResourceInfo.getServiceClass(),
                    Encoded.class) != null;
        }
    }

    private void checkDefaultParameterValue() {
        DefaultValue dv = AnnotationUtils.getMethodAnnotation(annotatedMethod,
                DefaultValue.class);
        if (dv == null && classResourceInfo != null) {
            dv = AnnotationUtils.getClassAnnotation(
                    classResourceInfo.getServiceClass(),
                    DefaultValue.class);
        }
        if (dv != null) {
            defaultParamValue = dv.value();
        }
    }
}

