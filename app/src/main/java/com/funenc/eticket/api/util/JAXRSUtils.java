package com.funenc.eticket.api.util;

import com.funenc.eticket.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;

class JAXRSUtils {
    public static final MediaType ALL_TYPES = new MediaType();

    public static List<MediaType> sortMediaTypes(String mediaTypes) {
        return sortMediaTypes(JAXRSUtils.parseMediaTypes(mediaTypes));
    }

    public static List<MediaType> sortMediaTypes(List<MediaType> types) {
        if (types.size() > 1) {
            Collections.sort(types, new Comparator<MediaType>() {

                public int compare(MediaType mt1, MediaType mt2) {
                    return JAXRSUtils.compareMediaTypes(mt1, mt2);
                }

            });
        }
        return types;
    }

    public static List<MediaType> parseMediaTypes(String types) {
        List<MediaType> acceptValues = new ArrayList<MediaType>();

        if (types != null) {
            while (types.length() > 0) {
                String tp = types;
                int index = types.indexOf(',');
                if (index != -1) {
                    tp = types.substring(0, index);
                    types = types.substring(index + 1).trim();
                } else {
                    types = "";
                }
                acceptValues.add(MediaType.valueOf(tp));
            }
        } else {
            acceptValues.add(ALL_TYPES);
        }

        return acceptValues;
    }

    public static int compareMediaTypes(MediaType mt1, MediaType mt2) {

        if (mt1.isWildcardType() && !mt2.isWildcardType()) {
            return 1;
        }
        if (!mt1.isWildcardType() && mt2.isWildcardType()) {
            return -1;
        }


        if (mt1.isWildcardSubtype() && !mt2.isWildcardSubtype()) {
            return 1;
        }
        if (!mt1.isWildcardSubtype() && mt2.isWildcardSubtype()) {
            return -1;
        }

        return compareMediaTypesQualityFactors(mt1, mt2);
    }

    public static int compareMediaTypesQualityFactors(MediaType mt1, MediaType mt2) {
        float q1 = getMediaTypeQualityFactor(mt1.getParameters().get("q"));
        float q2 = getMediaTypeQualityFactor(mt2.getParameters().get("q"));
        return Float.compare(q1, q2) * -1;
    }


    public static float getMediaTypeQualityFactor(String q) {
        if (q == null) {
            return 1;
        }
        if (q.charAt(0) == '.') {
            q = '0' + q;
        }
        try {
            return Float.parseFloat(q);
        } catch (NumberFormatException ex) {
            // default value will do
        }
        return 1;
    }

    public static List<MediaType> getMediaTypes(String[] values) {
        List<MediaType> supportedMimeTypes = new ArrayList<MediaType>(values.length);
        for (int i = 0; i < values.length; i++) {
            supportedMimeTypes.add(MediaType.valueOf(values[i]));
        }
        return supportedMimeTypes;
    }

    public static List<MediaType> getConsumeTypes(Consumes cm) {
        return cm == null ? Collections.singletonList(ALL_TYPES)
                : getMediaTypes(cm.value());
    }

    public static List<MediaType> getProduceTypes(Produces pm) {
        return pm == null ? Collections.singletonList(ALL_TYPES)
                : getMediaTypes(pm.value());
    }

    public static List<PathSegment> getPathSegments(String thePath, boolean decode,
                                                    boolean ignoreLastSlash) {
        String[] segments = thePath.split("/");
        List<PathSegment> theList = new ArrayList<PathSegment>();
        for (String path : segments) {
            if (!StringUtils.isEmpty(path)) {
                theList.add(new PathSegmentImpl(path, decode));
            }
        }
        int len = thePath.length();
        if (len > 0 && thePath.charAt(len - 1) == '/') {
            String value = ignoreLastSlash ? "" : "/";
            theList.add(new PathSegmentImpl(value, false));
        }
        return theList;
    }

    /**
     * Retrieve map of query parameters from the passed in message
     * @param message
     * @return a Map of query parameters.
     */
    public static MultivaluedMap<String, String> getStructuredParams(String query,
                                                                     String sep,
                                                                     boolean decode,
                                                                     boolean decodePlus) {
        MultivaluedMap<String, String> map =
                new MetadataMap<String, String>(new LinkedHashMap<String, List<String>>());

        getStructuredParams(map, query, sep, decode, decodePlus);

        return map;
    }

    private static void getStructuredParams(MultivaluedMap<String, String> queries,
                                           String query,
                                           String sep,
                                           boolean decode,
                                           boolean decodePlus) {
        if (!StringUtils.isEmpty(query)) {
            List<String> parts = Arrays.asList(query.split(sep));
            for (String part : parts) {
                int index = part.indexOf('=');
                String name = null;
                String value = null;
                if (index == -1) {
                    name = part;
                    value = "";
                } else {
                    name = part.substring(0, index);
                    value =  index < part.length() ? part.substring(index + 1) : "";
                    if (decode || (decodePlus && value.contains("+"))) {
                        value = (";".equals(sep))
                                ? HttpUtils.pathDecode(value) : HttpUtils.urlDecode(value);
                    }
                }
                queries.add(HttpUtils.urlDecode(name), value);
            }
        }
    }
}
