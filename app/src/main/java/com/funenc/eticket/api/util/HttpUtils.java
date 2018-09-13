package com.funenc.eticket.api.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * from org.apache.cxf.jaxrs.utils.HttpUtils
 */
public class HttpUtils {
    private static final String[] RESERVED_CHARS = {"+"};
    private static final String[] ENCODED_CHARS = {"%2b"};
    private static final String QUERY_RESERVED_CHARACTERS = "?/";
    private static final Pattern ENCODE_PATTERN = Pattern.compile("%[0-9a-fA-F][0-9a-fA-F]");
    // there are more of such characters, ex, '*' but '*' is not affected by UrlEncode
    private static final String PATH_RESERVED_CHARACTERS = "=@/:!$&\'(),;~";

    // from org.apache.cxf.jaxrs.utils.HttpUtils
    public static String urlEncode(String value) {

        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // unlikely to happen
        }

        return value;
    }

    public static String pathDecode(String path) {
        // TODO: we actually need to do a proper URI analysis here according to
        // http://tools.ietf.org/html/rfc3986
        for (int i = 0; i < RESERVED_CHARS.length; i++) {
            if (path.indexOf(RESERVED_CHARS[i]) != -1) {
                path = path.replace(RESERVED_CHARS[i], ENCODED_CHARS[i]);
            }
        }
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.w("HttpUtils", "UTF-8 encoding can not be used to decode " + path);
        }
        return path;
    }

    public static String queryEncode(String value) {

        return componentEncode(QUERY_RESERVED_CHARACTERS, value);
    }

    private static String componentEncode(String reservedChars, String value) {

        StringBuilder buffer = new StringBuilder();
        StringBuilder bufferToEncode = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char currentChar = value.charAt(i);
            if (reservedChars.indexOf(currentChar) != -1) {
                if (bufferToEncode.length() > 0) {
                    buffer.append(urlEncode(bufferToEncode.toString()));
                    bufferToEncode.setLength(0);
                }
                buffer.append(currentChar);
            } else {
                bufferToEncode.append(currentChar);
            }
        }

        if (bufferToEncode.length() > 0) {
            buffer.append(urlEncode(bufferToEncode.toString()));
        }

        return buffer.toString();
    }

    /**
     * Encodes partially encoded string. Encode all values but those matching pattern
     * "percent char followed by two hexadecimal digits".
     *
     * @param encoded fully or partially encoded string.
     * @return fully encoded string
     */
    public static String encodePartiallyEncoded(String encoded, boolean query) {
        if (encoded.length() == 0) {
            return encoded;
        }
        Matcher m = ENCODE_PATTERN.matcher(encoded);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (m.find()) {
            String before = encoded.substring(i, m.start());
            sb.append(query ? queryEncode(before) : pathEncode(before));
            sb.append(m.group());
            i = m.end();
        }
        String tail = encoded.substring(i, encoded.length());
        sb.append(query ? queryEncode(tail) : pathEncode(tail));
        return sb.toString();
    }

    public static String pathEncode(String value) {

        String result = componentEncode(PATH_RESERVED_CHARACTERS, value);
        // URLEncoder will encode '+' to %2B but will turn ' ' into '+'
        // We need to retain '+' and encode ' ' as %20
        if (result.indexOf('+') != -1) {
            result = result.replace("+", "%20");
        }
        if (result.indexOf("%2B") != -1) {
            result = result.replace("%2B", "+");
        }

        return result;
    }

    public static String urlDecode(String value) {
        try {
            value = URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.w("HttpUtils", "UTF-8 encoding can not be used to decode " + value, e);
        }
        return value;
    }
}
