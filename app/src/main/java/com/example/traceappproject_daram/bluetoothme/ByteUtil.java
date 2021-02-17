package com.example.traceappproject_daram.bluetoothme;

import android.util.Log;

public class ByteUtil {
    private static final String TAG ="BYTE UTIL";
    public static String byteToString(byte[] bytes){
        return new String(bytes);
    }
    public static byte[] stringToBytes(String s){
        return s.getBytes();
    }
    public static void logByteAskii(byte[] bytes){
        String res = "";
        for(int i = 0;i<bytes.length;i++){
            int cur = (int)bytes[i];
            res+= Integer.toBinaryString(cur)+" ";
        }
        Log.i(TAG,res);
    }

    public static void logStringAskii(String s){
        logByteAskii(s.getBytes());
    }
}
