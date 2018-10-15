package com.cx.logclient.util;

import java.util.Random;
public class StringUtil {
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String blankString(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }
}
