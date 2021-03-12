package com.example.traceappproject_daram.data;

import com.example.traceappproject_daram.reprot_page.heatmap.FeetMultiFrames;
import com.example.traceappproject_daram.reprot_page.heatmap.FootOneFrame;

import java.util.Calendar;

public class Result {
    Calendar calendar;
    LoginInfo loginInfo;
    int archLevel;
    int backLevel;

    //string 합치는 작업이 자바는 O(N)이래서 byte[]로 하고 저장할 때 변환하겠습니다
    /*
    byte[] data;//deceperated
    int endData;
    */
    byte[] leftData;
    byte[] rightData;
    int leftidx;
    int rightidx;
    private int idxInput;
    /*
    public Result(byte[] rawData,int endData){
        data = rawData;
        this.endData = endData;
    }
    */
    public Result(LoginInfo loginInfo) {
        //dummy
        this.calendar = Calendar.getInstance();
        this.loginInfo = loginInfo;
        clearData();
    }
    public String getID(){
        return this.loginInfo.getId();
    }
    //data에다가 모드값을 포함한 모든 것들을 싹다 넣는다
    //가정은 그냥 300언저리에서 시간이 많이 지났음에도 시간적으로 같은 순간의 데이터를 양 발이 수집했을 것이다
    //시간적으로 일치하지 않을 가능성 있음
    public FeetMultiFrames parseRaw(){//validation을 하면서 유효한 frame 만건져서 띄우기
        FeetMultiFrames frames = new FeetMultiFrames();
        FootOneFrame left=null,right=null;
        for(int i = 300;i<500;i++){
            if(leftData[i] == no.nordicsemi.android.nrftoolbox.uart.Cons.MODE_MEASURE_LEFT&&left ==null){
                if(isFoot(i+1,leftData)){
                    left = new FootOneFrame(leftData,i+1);
                    i+=Cons.SENSOR_NUM_FOOT-1;//성공했을 시에는 확 뛰어버리기
                }
            }
            else if(rightData[i] == no.nordicsemi.android.nrftoolbox.uart.Cons.MODE_MEASURE_RIGHT&&right == null){
                if(isFoot(i+1,rightData)){
                    right = new FootOneFrame(rightData,i+1);
                    i+=Cons.SENSOR_NUM_FOOT-1;
                }
            }

            if(left!=null&&right!=null){
                frames.appendFootFrame(left,right);
                left = null;
                right=null;
            }
        }
        return frames;
    }

    public void setVersion(int v){
        archLevel = 3;
        backLevel=3;
    }
    public void clearData(){
        leftData = new byte[Cons.MAX_FRAMES_NUM];
        leftidx = 0;
        rightData = new byte[Cons.MAX_FRAMES_NUM];
        rightidx =0;
        //data = new byte[Cons.MAX_FRAMES_NUM];
        idxInput =0;
    }

    //append 시 맨앞 s랑 echo는 안 넣는 걸로
    public void appendLeft(byte[] leftSet){ //append multiple data
        // do not check validity
        int howmany = leftSet.length-2;
        System.arraycopy(leftSet,2,leftData,leftidx,leftidx+howmany);
        leftidx+=howmany;
    }
    public void appendRight(byte[] rightSet){ //append
        int howmany = rightSet.length-2;
        System.arraycopy(rightSet,2,rightData,rightidx,rightidx+howmany);
        rightidx+=howmany;
    }

    public void makeinvalid (int idx, boolean isRight){
        //0번센서만 0xff로 해도되긴한데
        if(isRight){
            for(int j =0;j<Cons.SENSOR_NUM_FOOT;j++){
                rightData[idx+j] = (byte)0xff;
            }
        }
        else{
            for(int j =0;j<Cons.SENSOR_NUM_FOOT;j++){
                leftData[idx+j] = (byte)0xff;
            }
        }
    }
    //여기까지만 쓸 듯

    public static boolean isFoot(int sidx){
        return (sidx%Cons.SENSOR_NUM_FOOT==0);
    }


    public static boolean isFoot(int sidx, byte[] rawOneFoot){
        //measure command 없는 정사이즈의 발인지
        boolean res = true;
        for(int i = sidx;i<Cons.SENSOR_NUM_FOOT;i++){
            if(isMeasure(rawOneFoot[i])){
                res = false;
            }
        }
        return res;
    }
    public static boolean isMeasure(byte b){
        return b!= no.nordicsemi.android.nrftoolbox.uart.Cons.MODE_MEASURE_LEFT&&b!= no.nordicsemi.android.nrftoolbox.uart.Cons.MODE_MEASURE_RIGHT;
    }
    public Calendar getCalendar() {
        return calendar;
    }
    /*
    //deceperated
    //왜냐면 raw는 그냥 저장하고 띄울때만 검사할 거기 때문에
    public void trimData(){ //만약 한쪽이라도 invalid 하다면 해당 시간에 있는 양 발을 다 버린다

        int[] newLeft = new int[leftData.length];
        int[] newRight = new int[rightData.length];
        int newIdx =0;
        for(int i = 0;i<leftData.length&&i<rightData.length;i+=Cons.SENSOR_NUM_FOOT){
            if(leftData[i]==(byte)0xff||rightData[i] == (byte)0xff){
                //버리는 frame의 경우
                continue;
            }
            for(int j = 0;j<Cons.SENSOR_NUM_FOOT;j++){//안버리는 frame의 경우 append
                newLeft[newIdx++] = leftData[i+j];
                newRight[newIdx++] = rightData[i+j];
            }
        }
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

    boolean isLeft(int idx){
        return (idx%16< Cons.SENSOR_NUM_FOOT);
    }

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

    public static boolean isMeasure(byte b){
        return b!=Constants.MODE_MEASURE_LEFT&&b!=Constants.MODE_MEASURE_RIGHT;
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
            /*
            FootOneFrame left = new FootOneFrame(this.data,calcIdx(fidx,false));
            FootOneFrame right = new FootOneFrame(data, calcIdx(fidx, true));

            frames.appendFootFrame(left,right);


        }
        return frames;
    }
    */
}
