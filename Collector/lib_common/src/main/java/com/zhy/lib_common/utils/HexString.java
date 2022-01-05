/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-12-19 下午4:35
 *  FileName:HexString.java
 */

package com.zhy.lib_common.utils;

public class HexString {
    private final static String PARTEN = "0x|0X|[\\sG-Zg-z,.:;| ?!@#$%^&*/\\()<>+=_-]+";
    private final static String HEX = "0123456789ABCDEF";
    private final static byte[] hex = HEX.getBytes();

    public static String byteToHexString(byte b) {
        byte[] buf = new byte[2];
        buf[0] = hex[(b >> 4) & 0x0f];
        buf[1] = hex[(b >> 0) & 0x0f];
        return "0x" + new String(buf);
    }

    public static String byteToHexString(byte[] b) {
        if (b == null) return "";
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return "0x" + new String(buff);
    }

    public static byte[] hexStringToByte(String hex) {
        if (hex == null) return null;
        hex = hex.toUpperCase().replaceAll(PARTEN, "");
        if (hex.length() == 0) return null;

        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX.indexOf(achar[pos]) << 4
                    | HEX.indexOf(achar[pos + 1]));
        }
        return result;
    }
}