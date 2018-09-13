package com.funenc.eticket.api.util;

import java.util.List;

class CastUtils {
    public static <T> List<T> cast(List<?> p) {
        return (List<T>)p;
    }
}
