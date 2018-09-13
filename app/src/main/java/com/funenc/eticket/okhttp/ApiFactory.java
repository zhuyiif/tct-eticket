package com.funenc.eticket.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

//import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
//import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.funenc.eticket.api.util.AnnotationUtils;
import com.funenc.eticket.api.util.ClassResourceInfo;
import com.funenc.eticket.api.util.InjectionUtils;
import com.funenc.eticket.api.util.MetadataMap;
import com.funenc.eticket.api.util.OperationResourceInfo;
import com.funenc.eticket.api.util.Parameter;
import com.funenc.eticket.api.util.ParameterType;
import com.funenc.eticket.api.util.ResourceUtils;
import com.funenc.eticket.api.SubwayService;
import com.funenc.eticket.api.util.UriBuilderImpl;
import com.funenc.eticket.engine.AppEngine;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiFactory {
    private static final String SLASH = "/";
    private OkHttpClient client;

    public ApiFactory() {
        client = new OkHttpClient();
    }

    public ApiFactory(HttpUtils httpUtils){
        client = httpUtils.client;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public <T> T createService(final String baseAddress, final Class<T> sClass, final Callback cb) {
        if (isNetworkAvailable(AppEngine.getSystemContext())) {

//            List<Object> providerList = new ArrayList<Object>();
//            providerList.add(new JacksonJsonProvider());
//
//            SubwayService subwayService = JAXRSClientFactory.create(baseAddress, SubwayService.class, providerList);
//            VersionInfo latestVersion = subwayService.getLatestVersion();
//            System.out.println(latestVersion.getVersionName());
//            return latestVersion.getVersionName();
        final ClassResourceInfo cri = createResourceInfo(sClass);
//        for(Method m:SubwayService.class.getMethods()){
//            m.getDeclaredAnnotations();
//        }
            T service = sClass.cast(Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{sClass}, new InvocationHandler(){
                @Override
                public Object invoke(Object proxy, final Method method, Object[] params) throws Throwable {
//                    Log.d("proxy=", proxy.toString());
//                    Log.d("method=", method.toGenericString());
                    Log.d(sClass.getSimpleName()+ " Proxy", "begin to call " + method.getName());
                    if(proxy == null){
                        Log.d(sClass.getSimpleName()+ " Proxy", "proxy is null");
                    }else{
                        Log.d(sClass.getSimpleName()+ " Proxy", "proxy is not null");
                        Log.d("Proxy is called by", proxy.getClass().getName());
                    }
                    Log.d("Proxy is actually", method.getDeclaringClass().getName());
                    OperationResourceInfo ori = cri.getMethodDispatcher().getOperationResourceInfo(method);

                    MultivaluedMap<ParameterType, Parameter> types = getParametersInfo(params, ori);
                    boolean isRoot = cri.getURITemplate() != null;
                    List<Object> pathParams = getPathParamValues(types, params, ori, isRoot, cri);

                    int bodyIndex = getBodyIndex(types, ori);

                    UriBuilder builder = new UriBuilderImpl(new URI(baseAddress));
                    if (isRoot) {
                        addNonEmptyPath(builder, ori.getClassResourceInfo().getURITemplate().getValue());
                    }
                    addNonEmptyPath(builder, ori.getURITemplate().getValue());
                    handleQueries(types, params, builder);
                    URI uri = builder.buildFromEncoded(pathParams.toArray()).normalize();

                    MultivaluedMap<String, String> headers = new MetadataMap<String, String>(false, true);
                    MultivaluedMap<String, String> paramHeaders = new MetadataMap<String, String>();
                    handleHeaders(paramHeaders, types, params);
                    handleCookies(paramHeaders, types, params);
                    headers.putAll(paramHeaders);
                    setRequestHeaders(headers, ori, types.containsKey(ParameterType.FORM),
                            bodyIndex == -1 ? null : params[bodyIndex].getClass(), method.getReturnType());

                    RequestBody body = null;
                    if (bodyIndex != -1) {
                        body = (RequestBody) params[bodyIndex];
                    } else if (types.containsKey(ParameterType.FORM))  {
                        body = handleForm(types, params);
                    } else if (types.containsKey(ParameterType.REQUEST_BODY))  {
                        // ignore temporarily
//                        body = handleMultipart(types, ori, params);
                    }

                    Headers.Builder headerBuilder = new Headers.Builder();
                    for(String key : headers.keySet()){
                        for(String value:headers.get(key)){
                            headerBuilder.add(key, value);
                        }
                    }
                    Request request = new Request.Builder()
                            .url(uri.toString())
                            .headers(headerBuilder.build()).method(ori.getHttpMethod(), body)
                            .build();

                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("get me",e.toString());
                            cb.onFailure(call,e);

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            cb.onResponse(call,response);
                        }
                    });
                    return null;
                }
            }));
            return service;
        } else {
            Toast.makeText(AppEngine.getSystemContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private static ClassResourceInfo createResourceInfo(Class cls){
        boolean isRoot = AnnotationUtils.getClassAnnotation(cls, Path.class) != null;
        return ResourceUtils.createClassResourceInfo(cls, cls, isRoot, true);
    }

    private static MultivaluedMap<ParameterType, Parameter> getParametersInfo(
            Object[] params, OperationResourceInfo ori) {
        MultivaluedMap<ParameterType, Parameter> map =
                new MetadataMap<ParameterType, Parameter>();

        List<Parameter> parameters = ori.getParameters();
        if (parameters.size() == 0) {
            return map;
        }
        int requestBodyParam = 0;
        int multipartParam = 0;
        for (Parameter p : parameters) {
            if (p.getType() == ParameterType.CONTEXT) {
                // ignore
                continue;
            }
            if (p.getType() == ParameterType.REQUEST_BODY) {
                requestBodyParam++;
//                if (getMultipart(ori, p.getIndex()) != null) {
//                    multipartParam++;
//                }
            }
            map.add(p.getType(), p);
        }

        if (map.containsKey(ParameterType.REQUEST_BODY)) {
            if (requestBodyParam > 1 && requestBodyParam != multipartParam) {
                reportInvalidResourceMethod(ori.getMethodToInvoke(), "SINGLE_BODY_ONLY");
            }
            if (map.containsKey(ParameterType.FORM)) {
                reportInvalidResourceMethod(ori.getMethodToInvoke(), "ONLY_FORM_ALLOWED");
            }
        }
        return map;
    }

//    private static Multipart getMultipart(OperationResourceInfo ori, int index) {
//        Method aMethod = ori.getAnnotatedMethod();
//        return aMethod != null ? AnnotationUtils.getAnnotation(
//                aMethod.getParameterAnnotations()[index], Multipart.class) : null;
//    }

    protected static void reportInvalidResourceMethod(Method m, String name) {
        Log.i(name, m.getDeclaringClass().getName() + "." +
                m.getName());
        throw new RuntimeException(name + ":" + m.getDeclaringClass().getName() + "." +
                m.getName());
    }

    private List<Object> getPathParamValues(MultivaluedMap<ParameterType, Parameter> map,
                                            Object[] params,
                                            OperationResourceInfo ori, boolean isRoot, ClassResourceInfo cri) {
        Map<String, Object> valuesMap = initValuesMap(isRoot, cri);
        List<Object> list = new LinkedList<Object>();
        if (isRoot) {
            list.addAll(valuesMap.values());
        }
        List<String> methodVars = ori.getURITemplate().getVariables();

        List<Parameter> paramsList =  getParameters(map, ParameterType.PATH);
        Map<String, Parameter> paramsMap = new LinkedHashMap<String, Parameter>();
        for (Parameter p : paramsList) {
            if (p.getName().length() == 0) {
                MultivaluedMap<String, Object> values =
                        InjectionUtils.extractValuesFromBean(params[p.getIndex()], "");
                for (String var : methodVars) {
                    list.addAll(values.get(var));
                }
            } else {
                paramsMap.put(p.getName(), p);
            }
        }

        for (String varName : methodVars) {
            Parameter p = paramsMap.remove(varName);
            if (p != null) {
                list.add(params[p.getIndex()]);
            }
        }

        for (Parameter p : paramsMap.values()) {
            if (valuesMap.containsKey(p.getName())) {
                int index = 0;
                for (Iterator<String> it = valuesMap.keySet().iterator(); it.hasNext(); index++) {
                    if (it.next().equals(p.getName()) && index < list.size()) {
                        list.remove(index);
                        list.add(index, params[p.getIndex()]);
                        break;
                    }
                }
            }
        }

        return list;
    }

    private static Map<String, Object> initValuesMap(boolean isRoot, ClassResourceInfo cri, Object... varValues) {
        Map<String, Object> valuesMap = Collections.emptyMap();
        if (isRoot) {
            List<String> vars = cri.getURITemplate().getVariables();
            valuesMap = new LinkedHashMap<String, Object>();
            for (int i = 0; i < vars.size(); i++) {
                if (varValues.length > 0) {
                    if (i < varValues.length) {
                        valuesMap.put(vars.get(i), varValues[i]);
                    } else {
                        Log.i("ROOT_VARS_MISMATCH", vars.size()+" and "+varValues.length);
                        break;
                    }
                } else {
                    valuesMap.put(vars.get(i), "");
                }
            }
        }
        return valuesMap;
    }

    private static List<Parameter> getParameters(MultivaluedMap<ParameterType, Parameter> map,
                                                 ParameterType key) {
        return  map.get(key) == null ? Collections.EMPTY_LIST : map.get(key);
    }

    private static int getBodyIndex(MultivaluedMap<ParameterType, Parameter> map,
                                    OperationResourceInfo ori) {
        List<Parameter> list = map.get(ParameterType.REQUEST_BODY);
        int index = list == null || list.size() > 1 ? -1 : list.get(0).getIndex();
        if (ori.isSubResourceLocator() && index != -1) {
            reportInvalidResourceMethod(ori.getMethodToInvoke(), "NO_BODY_IN_SUBRESOURCE");
        }
        return index;
    }

    private void addNonEmptyPath(UriBuilder builder, String pathValue) {
        if (!SLASH.equals(pathValue)) {
            builder.path(pathValue);
        }
    }

    private static void handleQueries(MultivaluedMap<ParameterType, Parameter> map,
                                      Object[] params,
                                      UriBuilder ub) {
        List<Parameter> qs = getParameters(map, ParameterType.QUERY);
        for (Parameter p : qs) {
            if (params[p.getIndex()] != null) {
                addParametersToBuilder(ub, p.getName(), params[p.getIndex()], ParameterType.QUERY);
            }
        }
    }

    // TODO : shall we just do the reflective invocation here ?
    protected static void addParametersToBuilder(UriBuilder ub, String paramName, Object pValue,
                                                 ParameterType pt) {
        if (pt != ParameterType.MATRIX && pt != ParameterType.QUERY) {
            throw new IllegalArgumentException("This method currently deal "
                    + "with matrix and query parameters only");
        }
        if (!"".equals(paramName)) {

            if (InjectionUtils.isSupportedCollectionOrArray(pValue.getClass())) {
                Collection<?> c = pValue.getClass().isArray()
                        ? Arrays.asList((Object[]) pValue) : (Collection<?>) pValue;
                for (Iterator<?> it = c.iterator(); it.hasNext();) {
                    addToBuilder(ub, paramName, it.next(), pt);
                }
            } else {
                addToBuilder(ub, paramName, pValue, pt);
            }


        } else {
            MultivaluedMap<String, Object> values =
                    InjectionUtils.extractValuesFromBean(pValue, "");
            for (Map.Entry<String, List<Object>> entry : values.entrySet()) {
                for (Object v : entry.getValue()) {
                    addToBuilder(ub, entry.getKey(), v, pt);
                }
            }
        }
    }

    private static void addToBuilder(UriBuilder ub, String paramName, Object pValue,
                                     ParameterType pt) {
        if (pt == ParameterType.MATRIX) {
            ub.matrixParam(paramName, pValue.toString());
        } else {
            ub.queryParam(paramName, pValue.toString());
        }
    }

    private void handleHeaders(MultivaluedMap<String, String> headers,
                               MultivaluedMap<ParameterType, Parameter> map, Object[] params) {
        List<Parameter> hs = getParameters(map, ParameterType.HEADER);
        for (Parameter p : hs) {
            if (params[p.getIndex()] != null) {
                headers.add(p.getName(), params[p.getIndex()].toString());
            }
        }
    }

    private void handleCookies(MultivaluedMap<String, String> headers,
                               MultivaluedMap<ParameterType, Parameter> map, Object[] params) {
        List<Parameter> cs = getParameters(map, ParameterType.COOKIE);
        for (Parameter p : cs) {
            if (params[p.getIndex()] != null) {
                headers.add(HttpHeaders.COOKIE, p.getName() + '=' + params[p.getIndex()].toString());
            }
        }
    }

    private MultivaluedMap<String, String> setRequestHeaders(MultivaluedMap<String, String> headers,
                                                             OperationResourceInfo ori,
                                                             boolean formParams,
                                                             Class<?> bodyClass,
                                                             Class<?> responseClass) {
        if (headers.getFirst(HttpHeaders.CONTENT_TYPE) == null) {
            if (formParams || bodyClass != null && MultivaluedMap.class.isAssignableFrom(bodyClass)) {
                headers.putSingle(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
            } else {
                String cType = ori.getConsumeTypes().isEmpty()
                        || ori.getConsumeTypes().get(0).equals(MediaType.WILDCARD_TYPE)
                        ? MediaType.APPLICATION_XML : ori.getConsumeTypes().get(0).toString();
                headers.putSingle(HttpHeaders.CONTENT_TYPE, cType);
            }
        }

        List<MediaType> accepts = getAccept(headers);
        if (accepts == null) {
            boolean produceWildcard = ori.getProduceTypes().size() == 0
                    || ori.getProduceTypes().get(0).equals(MediaType.WILDCARD_TYPE);
            if (produceWildcard) {
                accepts = InjectionUtils.isPrimitive(responseClass)
                        ? Collections.singletonList(MediaType.TEXT_PLAIN_TYPE)
                        : Collections.singletonList(MediaType.APPLICATION_XML_TYPE);
            } else if (responseClass == Void.class) {
                accepts = Collections.singletonList(MediaType.WILDCARD_TYPE);
            } else {
                accepts = ori.getProduceTypes();
            }

            for (MediaType mt : accepts) {
                headers.add(HttpHeaders.ACCEPT, mt.toString());
            }
        }

        return headers;
    }

    private List<MediaType> getAccept(MultivaluedMap<String, String> allHeaders) {
        List<String> headers = allHeaders.get(HttpHeaders.ACCEPT);
        if (headers == null || headers.size() == 0) {
            return null;
        }
        List<MediaType> types = new ArrayList<MediaType>();
        for (String s : headers) {
            types.add(MediaType.valueOf(s));
        }
        return types;
    }

    private RequestBody handleForm(MultivaluedMap<ParameterType, Parameter> map,
                                   Object[] params) {

        MultivaluedMap<String, String> form = new MetadataMap<String, String>();

        List<Parameter> fm = getParameters(map, ParameterType.FORM);
        for (Parameter p : fm) {
            Object pValue = params[p.getIndex()];
            if (pValue != null) {
                if (InjectionUtils.isSupportedCollectionOrArray(pValue.getClass())) {
                    Collection<?> c = pValue.getClass().isArray()
                            ? Arrays.asList((Object[]) pValue) : (Collection<?>) pValue;
                    for (Iterator<?> it = c.iterator(); it.hasNext();) {
                        addPropertyToForm(form, p.getName(), it.next());
                    }
                } else {
                    addPropertyToForm(form, p.getName(), pValue);
                }

            }
        }

        // add for okhttp
        FormBody.Builder formBuilder = new FormBody.Builder();
        for(String name:form.keySet()) {
            for(String value:form.get(name)) {
                formBuilder.add(name, value);
            }
        }

        return formBuilder.build();
    }

    public static void addPropertyToForm(MultivaluedMap<String, String> map, String name, Object value) {
        if (!"".equals(name)) {
            map.add(name, value.toString());
        } else {
            MultivaluedMap<String, Object> values =
                    InjectionUtils.extractValuesFromBean(value, "");
            for (Map.Entry<String, List<Object>> entry : values.entrySet()) {
                for (Object v : entry.getValue()) {
                    map.add(entry.getKey(), v.toString());
                }
            }
        }
    }
}
