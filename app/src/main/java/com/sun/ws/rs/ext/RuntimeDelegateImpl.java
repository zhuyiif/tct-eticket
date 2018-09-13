package com.sun.ws.rs.ext;

import com.funenc.eticket.api.util.UriBuilderImpl;
import com.funenc.eticket.api.util.ext.MediaTypeHeaderProvider;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.RuntimeDelegate;

public class RuntimeDelegateImpl extends RuntimeDelegate {
    protected Map<Class<?>, HeaderDelegate<?>> headerProviders
            = new HashMap<Class<?>, HeaderDelegate<?>>();

    public RuntimeDelegateImpl() {
        headerProviders.put(MediaType.class, new MediaTypeHeaderProvider());
    }

    @Override
    public UriBuilder createUriBuilder() {
        return new UriBuilderImpl();
    }

    @Override
    public Response.ResponseBuilder createResponseBuilder() {
        return null;
    }

    @Override
    public Variant.VariantListBuilder createVariantListBuilder() {
        return null;
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {
        return null;
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
        return (HeaderDelegate<T>)headerProviders.get(type);
    }
}
