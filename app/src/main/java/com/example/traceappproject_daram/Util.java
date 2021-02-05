package com.example.traceappproject_daram;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.traceappproject_daram.data.Cons.IMG_EXT;

public class Util {
    public static String cvtBytesToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes,"UTF-8");
    }
    public static byte[] cvtStringToByte(String s){
        return s.getBytes();
    }

    public static final String makeFolderPath(Context context,Calendar calendar){
        String upperPath = context.getFilesDir().getPath().toString();//그냥 캐시 path
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");//날짜(시간까지)
        String strDate = sdf.format(calendar.getTime());
        return upperPath+"/"+strDate;
    }
    //context가 같으면 filedir도 같을 것이다.
    public static final String make_path(Context context, Calendar calendar, int idx){//full path
        String upperPath = context.getFilesDir().getPath().toString();//그냥 캐시 path
        String pureName = Integer.toString(idx);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");//날짜(시간까지)
        String strDate = sdf.format(calendar.getTime());
        return upperPath+"/"+strDate+"/"+pureName+IMG_EXT;
    }
}
