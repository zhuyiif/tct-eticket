package com.funenc.eticket.util;

public class StringUtils {
    public static boolean isBlank(String str){
        return str==null || str.trim().isEmpty();
    }
    public static boolean isEmpty(String str){
        return str==null || str.isEmpty();
    }
}
