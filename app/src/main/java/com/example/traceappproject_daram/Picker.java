package com.example.traceappproject_daram;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static com.example.traceappproject_daram.data.Cons.NAME_EXT;

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
