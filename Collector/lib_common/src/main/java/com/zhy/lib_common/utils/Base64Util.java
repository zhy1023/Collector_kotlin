package com.zhy.lib_common.utils;

import android.util.Base64;

/**
 * Created by zou on 2016/8/5.
 */
public class Base64Util {

    /**
     * Base64解密
     *
     * @param string
     * @return
     */
    public static String decode(String string) {
        String result = null;
        if (string != null) {
            result = new String(Base64.decode(string.getBytes(), Base64.DEFAULT));
        }
        return result;
    }

}
