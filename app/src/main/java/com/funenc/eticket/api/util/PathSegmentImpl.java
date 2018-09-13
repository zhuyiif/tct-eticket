package com.funenc.eticket.api.util;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.ext.Provider;

@Provider
public class PathSegmentImpl implements PathSegment {

    private String path;

    public PathSegmentImpl(String path) {
        this(path, true);
    }

    public PathSegmentImpl(String path, boolean decode) {
        this.path = decode ? HttpUtils.pathDecode(path) : path;
    }

    public MultivaluedMap<String, String> getMatrixParameters() {
        // ignore
//        return JAXRSUtils.getMatrixParams(path, false);
        return new MetadataMap<String, String>();
    }

    public String getPath() {
        int index = path.indexOf(';');
        String value = index != -1 ? path.substring(0, index) : path;
        if (value.startsWith("/")) {
            value = value.length() == 1 ? "" : value.substring(1);
        }
        return value;
    }

    public String getOriginalPath() {
        return path;
    }

    public String toString() {
        return path;
    }

}
