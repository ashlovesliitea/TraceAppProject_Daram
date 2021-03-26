package com.daram.trace;

import android.util.Log;

import java.io.Serializable;

public class Picker implements Serializable {
    private int i;
    private static String fileName = "picker.bin";
    private String TAG  ="Picker";
    public Picker(){
        i=0;
    }
    public int getI(){
        Log.i(TAG,"current picker : "+i);
        return i;
    }
    public void up(){
        i++;
        if(i ==3){
            i=0;
        }
    }
}
