package com.example.traceappproject_daram.data;

import com.example.traceappproject_daram.reprot_page.heatmap.FeettMultiFrames;
import com.example.traceappproject_daram.reprot_page.heatmap.FootOneFrame;

import java.util.Date;

public class Result {
    Date date;
    LoginInfo loginInfo;
    int archLevel;
    int backLevel;
    //string 합치는 작업이 자바는 O(N)이래서 byte[]로 하고 저장할 때 변환하겠습니다
    public static final int MEASURE_INTERVAL= 10;//ms 단위
    public static final int NUM_MAX_FRAMES=(1000-MEASURE_INTERVAL)*20;//10ms마다 측정&20초 걷기
    public static final int NUM_MIN_FRAMES = (1000-MEASURE_INTERVAL)*5;
    public static final int maxIdx = FootOneFrame.SENSOR_NUM* FeettMultiFrames.NUM_FRAMES*2;
    byte[] data;
    private int idxInput;
    public Result(Date date, LoginInfo loginInfo, int archLevel, int backLevel) {
        this.date = date;
        this.loginInfo = loginInfo;
        this.archLevel = archLevel;
        this.backLevel = backLevel;
        data= new byte[NUM_MAX_FRAMES];
        idxInput=0;
    }
    public void cleatData(){
        data=new byte[NUM_MAX_FRAMES];
        idxInput =0;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public byte[] getData(){
        return data;
    }
    /*
    public boolean appendOneFrame(byte[] b){//양발 센서값 받기
        //왼발 오른발은 받으면서 판별하기
        //b는 0-9값
        //리턴은 append할 수 있었는지의 여부
        if(idxInput+b.length<idxInput){
            return false;
        }
        for(int i = 0;i<b.length;i++){
            data[idxInput++] = b[i];
        }
        return true;
    }
    public boolean isValid(){
        return idxInput>=NUM_MIN_FRAMES&&idxInput<=NUM_MAX_FRAMES;
    }
    */


    /*
        //대표할 수 있는 한 걸음을 추출하기
    public boolean isBack(int idx){
        for(int i = 0;i)
    }
    public FeettMultiFrames extractRepresentive(){
        if(!isValid()) return null;
        //representive한 거 뽑는 방법을 모르겠지만 일단 구현
        int ctrback=0, ctrarch=0;

    }

     */

}
