package com.example.traceappproject_daram.data;

import com.example.traceappproject_daram.reprot_page.heatmap.FeetMultiFrames;
import com.example.traceappproject_daram.reprot_page.heatmap.FootOneFrame;

import java.util.Calendar;
import java.util.Date;

public class Result {
    Calendar calendar;
    LoginInfo loginInfo;
    int archLevel;
    int backLevel;
    //string 합치는 작업이 자바는 O(N)이래서 byte[]로 하고 저장할 때 변환하겠습니다
    byte[] data;
    private int idxInput;
    public Result(LoginInfo loginInfo, int archLevel, int backLevel) {
        this.calendar = Calendar.getInstance();
        this.loginInfo = loginInfo;
        this.archLevel = archLevel;
        this.backLevel = backLevel;
        data= new byte[Cons.MAX_FRAMES_NUM];
        idxInput=0;
    }
    public void clearData(){
        data=new byte[Cons.MAX_FRAMES_NUM];
        idxInput =0;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public byte[] getData(){
        return data;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public boolean appendOneFrame(byte[] b){//양발 "온전한" 센서값 받기
        //그냥 직렬적으로 data에 쌓아두기
        //왼발 오른발 순으로
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
    public boolean isValidFrames(){
        return idxInput>= Cons.MIN_FRAMES_NUM &&idxInput<= Cons.MAX_FRAMES_NUM;
    }
    /*
    boolean isLeft(int idx){
        return (idx%16< Cons.SENSOR_NUM_FOOT);
    }
    */
    int calcIdx(int frameIdx, boolean isRight,int sensorPos){ //calc idx by frame idx
        return frameIdx* Cons.SENSOR_NUM_FOOT*2+(isRight?Cons.SENSOR_NUM_FOOT:0) +sensorPos;
    }
    int calcIdx(int frameIdx){
        return calcIdx(frameIdx,false, 0);
    }
    int calcIdx(int frameIdx, boolean isRight){
        return calcIdx(frameIdx, isRight, 0);
    }
    //딥러닝 써서 할 수도 있겠지만 일단 산술적인 코드
    public boolean isBack(int frameIdx,boolean isRight){
        boolean allActivated = true;
        //아치가 먼저임
        for(int sensorPos = Cons.ARCH_SENSOR_NUM; sensorPos< Cons.BACK_SENSOR_NUM; sensorPos++){
            if(data[calcIdx(frameIdx,isRight,sensorPos)]< Cons.THRESH_ACTIVATED){
                allActivated = false;
                break;
            }
        }
        return allActivated;
    }
    //BACK을 제외한 모든 센서가 9찍어야 하는데 이런 경우는 거의 없을 거같음
    public boolean isArch(int frameIdx,boolean isRight){
        boolean allActivated = true;
        for(int sensorPos = 0; sensorPos< Cons.ARCH_SENSOR_NUM; sensorPos++){
            if(data[calcIdx(frameIdx,isRight,sensorPos)]< Cons.THRESH_ACTIVATED){
                allActivated = false;
                break;
            }
        }
        return allActivated;
    }
    public static boolean isFoot(int sidx){
        return (sidx%Cons.SENSOR_NUM_FOOT==0);
    }
    public int calcEndFSize(){
        return idxInput/(Cons.SENSOR_NUM_FOOT *2);
    }
    public static int calcSecToIdx(int sec){
        return Cons.MEASURE_NUM_1SEC*Cons.SENSOR_NUM_FOOT*2*sec;
    }
    public static int calcSecToFrame(int sec){
        return Cons.MEASURE_NUM_1SEC*sec;
    }
    //대표할 수 있는 한 걸음을 추출하기
    //알고리즘 선택해야됨
    //구현할 땐 index range로 multi frames로 변환하기

    public FeetMultiFrames extractRepresentive(){
        if(!isValidFrames()) return null;
        //걍 특정 시간대로 고정시키기
        FeetMultiFrames frames = new FeetMultiFrames();

        //int ctrLeftBack=0, ctrLeftArch=0,ctrRightBack = 0, ctrRightArch =0;
        for(int fidx = Cons.REP_START_FRAME;fidx<Cons.REP_END_FRAME;fidx++){
            //복사
            FootOneFrame left = new FootOneFrame(this.data,calcIdx(fidx,false));
            FootOneFrame right = new FootOneFrame(data, calcIdx(fidx, true));
            frames.appendFootFrame(left,right);
        }
        return frames;
    }




}
