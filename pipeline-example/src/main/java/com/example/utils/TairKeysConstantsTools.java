package com.example.utils;

import com.example.enums.CommonCharEnum;

public class TairKeysConstantsTools {
    public static String generateKey(String separator,String str1, String str2, String str3) {
        return str1 + separator + str2 + separator + str3;
    }
}
