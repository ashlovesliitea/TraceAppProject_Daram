package com.example.traceappproject_daram;

import java.io.UnsupportedEncodingException;

public class Util {
    public static String cvtBytesToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes,"UTF-8");
    }
    public static byte[] cvtStringToByte(String s){
        return s.getBytes();
    }
}
