package com.canyue.mqtt.ui.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author: canyue
 * @Date: 2020/5/7 22:03
 */
public class Encoder {
    private Encoder() {
    }

    ;

    public static String encode(byte[] raw, String encode) throws UnsupportedEncodingException {
        if ("hex".equals(encode)) {
            return encodeHex(raw);
        } else if ("bin".equals(encode)) {
            return encodeBinary(raw);
        } else {
            return new String(raw, encode);
        }
    }

    private static String encodeHex(byte[] raw) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(raw.length * 2);
        for (int i = 0; i < raw.length; i++) {
            sb.append(fromNumber2Alphabet((raw[i] >>> 4) & 0x0f));
            sb.append(fromNumber2Alphabet(raw[i] & 0x0f));
        }
        return sb.toString();
    }

    private static String encodeBinary(byte[] raw) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(raw.length * 8);
        for (int i = 0; i < raw.length; i++) {
            for (int j = 7; j >= 0; j--) {
                sb.append((raw[i] >>> j) & 0x01);
            }
        }
        return sb.toString();
    }


    public static String encode(byte[] raw) throws UnsupportedEncodingException {
        return encode(raw, "utf8");
    }

    private static char fromNumber2Alphabet(int num) {
        if (num < 10) {
            return (char) ('0' + num);
        } else {
            return (char) ('a' + num - 10);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        byte[] bytes = "你好，我是canyue".getBytes("UTF8");
        System.out.println(encode(bytes, "GBK"));
        System.out.println("-------------------------------------------");
        System.out.println(encode(bytes, "utf8"));
        System.out.println("-------------------------------------------");
        System.out.println(encode(bytes, "ascii"));
        System.out.println("-------------------------------------------");
        System.out.println(encode(bytes, "hex"));
        System.out.println("-------------------------------------------");
        System.out.println(encode(bytes, "bin"));
    }
}
