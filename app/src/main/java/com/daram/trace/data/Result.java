package com.daram.trace.data;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.daram.trace.reprot_page.heatmap.FeetMultiFrames;
import com.daram.trace.reprot_page.heatmap.FootOneFrame;

import java.io.Serializable;
import java.util.Calendar;

public class Result implements Serializable {
    Calendar calendar;
    LoginInfo loginInfo;
    int leftArchLevel;
    int leftBackLevel;



    int rightArchLevel;
    int rightBackLevel;

    //string 합치는 작업이 자바는 O(N)이래서 byte[]로 하고 저장할 때 변환하겠습니다
    /*
    byte[] data;//deceperated
    int endData;
    */
    byte[] leftData;
    byte[] rightData;
    int total_len= 225;
    int leftidx;
    int rightidx;
    private int idxInput;
    private int period=300;
    private int threshold=32;
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
        this.leftData = new byte[3000];
        this.rightData = new byte[3000];

        clearData();
    }
    public void setLeftAsDummy(){
        for(int i = 0;i<230;i++){
            if(i%8 == 0){
                leftData[i] = (byte)',';
                continue;
            }
            leftData[i] = (byte)i;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setRightAsDummy(){
        for(byte i = (byte)0xff;i>=0;i--){
            if(i%8==0){
                rightData[i] = (byte) ',';
                continue;
            }
            rightData[i] = (byte)i;
        }
        String str = "";
        for(int i =100;i>=0;i--){
            str+= Integer.toHexString(Byte.toUnsignedInt(rightData[i]))+" ";
        }
        Log.i(TAG,"RIGHT DATA : "+str);
    }
    public void setLeftData(byte[] arr,int len){
        System.arraycopy(arr, 0, leftData, 0, len);
        leftidx = len;
        //index의 limit을 어떻게 할것인가
    }
    public void setRightData(byte[] arr, int len){
//        Log.i(TAG,"setting right data : "+len+", "+arr[0]+","+arr[1]+","+arr[2]);
        System.arraycopy(arr, 0, rightData, 0, len);
//        Log.i(TAG,"setting right data after copy : "+len+", "+rightData[0]+","+rightData[1]+","+rightData[2]);
        rightidx = len;
    }
    //data에다가 모드값을 포함한 모든 것들을 싹다 넣는다
    //가정은 그냥 300언저리에서 시간이 많이 지났음에도 시간적으로 같은 순간의 데이터를 양 발이 수집했을 것이다
    //시간적으로 일치하지 않을 가능성 있음
    enum FootState{
        Front,
        Back,
        Empty
    }
    enum FootName{
        Right,
        Left
    }
//    true: a, false: b
    boolean get_mini(byte a, byte b){
        if (a==0){
            return false;
        }
        if (b==0){
            return true;
        }
        return a/b>0;
    }
    FootState genFootState(byte[] one_frame, FootName foot){
        if (foot==FootName.Right){
            if (one_frame[1]<threshold&&one_frame[6]<threshold){
                return FootState.Empty;
            }
            if (get_mini(one_frame[1],one_frame[6])){
                return FootState.Front;
            }else{
                return FootState.Back;
            }
        }else{
            if (one_frame[2]<threshold&&one_frame[7]<threshold){
                return FootState.Empty;
            }
            if (get_mini(one_frame[2],one_frame[7])){
                return FootState.Front;
            }else{
                return FootState.Back;
            }
        }
    }

    public static String TAG = "Result";
    @RequiresApi(api = Build.VERSION_CODES.O)
    public FeetMultiFrames parseRaw(){//validation을 하면서 유효한 frame 만건져서 띄우기
        FeetMultiFrames frames = new FeetMultiFrames();
        FootOneFrame left=null,right=null;

        //left raw와 right raw를 전부 출력하자.
//        Log.e(TAG,"parseRaw : "+leftData.length+" , "+rightData.length);
//        int leftBack = calcBackIdx(leftData);
//        int leftArch = calcArchIdx(leftData);
//        Log.e(TAG,"leftback : "+leftBack+" ,  rightEmpty: "+rightEmpty);

//        int rightEmpty = calcEmptyIdx(rightData);
//        int rightBack = calcBackIdx(rightData);
//        int rightArch = calcArchIdx(rightData);
//        Log.e(TAG,"나머지 지점 : "+leftArch+" , "+rightBack+" , "+rightArch);
        int numLFrame=0;
//        25set
        int sets_Frame=25;
        int skip_index=-1;
        int term=0;
        // true: right , false: left
        boolean whofirst=false;
        for(int i=0; i<sets_Frame; i++){
            byte[] left_one=new byte[8];
            System.arraycopy(leftData, i*9,left_one , 0, 8);
            byte[] right_one=new byte[8];
            System.arraycopy(rightData, i*9,right_one,0,8);

            FootState rstate=genFootState(right_one,FootName.Right);
            FootState lstate=genFootState(left_one,FootName.Left);
            if (rstate!=lstate){
                if (rstate==FootState.Empty||lstate==FootState.Empty){
                    if (skip_index>=0){
                        term=i-skip_index;
                    }
                    whofirst= rstate == FootState.Empty;
                    break;
                }
            }else{
               if (skip_index<0){
                   skip_index=i;
               }
            }
        }
        for (int i=0; i<sets_Frame; i++){
            if (whofirst){
//                right
                left = new FootOneFrame(leftData,((i+term)%sets_Frame)*9,false);
                right = new FootOneFrame(rightData,i*9,true);
            }else{
//                left
                left = new FootOneFrame(leftData,i*9,false);
                right = new FootOneFrame(rightData,((i+term)%sets_Frame)*9,true);
            }

            frames.appendAFrame(left,false);
            frames.appendAFrame(right,true);
        }
//        for(int i = 0;i<total_len;i++){
//            if(leftData[i] == no.nordicsemi.android.nrftoolbox.uart.Cons.DEL){
////                Log.e(TAG,"appending left data at : "+i+" ,,, " +leftData[i]+","+leftData[i+1]+","+leftData[i+2]);
//                left = new FootOneFrame(leftData,i+1,false);
//                frames.appendAFrame(left,numLFrame,false);
//                numLFrame++;
//            }
//        }
//        int numRFrame = 0;
//        for(int i = 0; i<total_len && numRFrame<15 ;i++){
//            if(rightData[i] == no.nordicsemi.android.nrftoolbox.uart.Cons.DEL){
////                Log.e(TAG,"appending right data at : " + i + " ,,, " + rightData[i] + "," + rightData[i + 1]+","+rightData[i+2]);
//                right = new FootOneFrame(rightData,i+1,true);
//                frames.appendAFrame(right,numRFrame,true);
//                numRFrame++;
//            }
//        }

        /*
        for(int i = 0;i<200&&numLFrame<10;i++) {
            if(leftData[i] == no.nordicsemi.android.nrftoolbox.uart.Cons.DEL){
                Log.i(TAG,"appending left data at : "+i+" ,,, " +leftData[i]+","+leftData[i+1]+","+leftData[i+2]);
                left = new FootOneFrame(leftData,i+1,false);
                frames.appendAFrame(left,numLFrame,false);
                numLFrame++;
            }
        }

        int numRFrame = 0;
        for(int i = 0 ; i<200 && numRFrame<10 ;i++){
            if(rightData[i] == no.nordicsemi.android.nrftoolbox.uart.Cons.DEL){
                Log.i(TAG,"appending right data at : " + i + " ,,, " + rightData[i] + "," + rightData[i + 1]+","+rightData[i+2]);
                right = new FootOneFrame(rightData,i+1,true);
                frames.appendAFrame(right,numRFrame,true);
                numRFrame++;
            }
        }
        */
        frames.setFrameNum();
//        Log.e(TAG,"MIN FRAME NUM"+frames.getFramesSz()+","+Math.min(numLFrame,numRFrame));


//        Log.e(TAG,"setting FrameNumber : "+frames.getFramesSz());
        return frames;
    }

    public int parseBackOneHot(int n){
        if(n == 0b011) return 1;
        if(n == 0b101) return 2;
        if(n == 0b110) return 3;
        return 0;
    }
    public int parseArchOneHot(int n){
        if(n == 0b110) return 1;
        if(n == 0b011) return 2;
        if(n == 0b101) return 3;
        return 0;
    }

    //              0 1 2 3 4 5 6 7 8
    /*
    int revBack[] = {0,1,2,0,3};
    int revArch[] = {0,0,0,0,0};

     */
    //TODO:setVersion 함수 수정한 거 디버깅 하기!
    public void setVersion(int v,boolean isRight){
        //아치 위에서 3개 뒷꿈 왼에서 3개 순서
        int arch = (v&0b111000)>>3;
        arch = parseArchOneHot(arch);
        int back= v&0b111;
        back = parseBackOneHot(back);
        Log.i(TAG,"version parsing  : "+isRight+","+arch+","+back);
        if(isRight){
            rightArchLevel = arch;
            rightBackLevel = back;
        }
        else{
            leftArchLevel = arch;
            leftBackLevel = back;
        }
        Log.i(TAG,"version parsing  : "+isRight+","+arch+","+back+","+ leftArchLevel +","+ leftBackLevel);
    }
    public void clearData(){
        leftData = new byte[Cons.MAX_FRAMES_NUM];
        leftidx = 0;
        rightData = new byte[Cons.MAX_FRAMES_NUM];
        rightidx =0;
        //data = new byte[Cons.MAX_FRAMES_NUM];
        idxInput =0;
    }
    public byte[] getRightData(){
        return rightData;
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
    public boolean isBack(byte data[],int firstidx){
        boolean allActivated = false;
        //아치가 먼저임
        for(int sensorPos = Cons.ARCH_SENSOR_NUM; sensorPos< Cons.SENSOR_NUM_FOOT; sensorPos++){
            if(data[firstidx+sensorPos]> Cons.THRESH_ACTIVATED){
                allActivated = true;
                break;
            }
        }
        return allActivated;
    }
    //BACK을 제외한 모든 센서가 찍어야 하는데 이런 경우는 거의 없을 거같음
    public boolean isArch(byte data[],int firstidx){
        boolean allActivated = true;
        for(int sensorPos = 0; sensorPos< Cons.ARCH_SENSOR_NUM; sensorPos++){
            if(data[firstidx+sensorPos]< Cons.THRESH_ACTIVATED){
                allActivated = false;
                break;
            }
        }
        return allActivated;
    }
    public boolean isEmpty(boolean isright){
        if(isright) return rightData[0]==0x00;
        else return leftData[0] ==0x00;
    }

    public boolean isEmpty(byte data[], int firstidx){
        boolean allDeactivated = true;
        for(int sensorPos = 0; sensorPos< Cons.SENSOR_NUM_FOOT; sensorPos++){
            if(data[firstidx+sensorPos]> 0x70){

                allDeactivated = false;
                break;
            }
        }
        return allDeactivated;
    }
    public int calcBackIdx(byte data[]){
        for(int i = 0;i<data.length;i++){
            if(data[i] == (byte)',') {
                if (isBack(data, i+1)) {
                    Log.i(TAG,"back idx : "+(i+1));
                    return i;
                }
            }
        }
        Log.i(TAG,"back idx : fail");
        return 2;
    }

    public int calcArchIdx(byte data[]){
        for(int i = 0;i<data.length;i++){
            if(data[i] == (byte)',') {
                if (isArch(data, i+1)) {
                    Log.i(TAG,"arch idx : found "+i+1);
                    return i;
                }
            }
        }
        Log.i(TAG,"arch idx : fail ");
        return 2;
    }

    public boolean isUnFilled(boolean isRight){
        Log.i(TAG,"is unfilled running"+(int)rightData[0]+","+(int)rightData[1]+","+(int)leftData[0]+","+(int)leftData[1]);

        if(isRight){
            return rightData[0] ==0;
        }
        else{
            return leftData[0] ==0;
        }
    }
    public int calcEmptyIdx(byte data[]){
        for(int i = 0;i<data.length;i++){
            if(data[i] == (byte)',') {
                if (isEmpty(data, i+1)) {
                    Log.i(TAG,"empty idx : found "+i+1);
                    return i;
                }
            }
        }

        Log.i(TAG,"empty idx : fail ");
        return 2;
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
    */
    //data 배열을 누구를 주느냐에 따라 왼발/오른발 결정
    /*
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
    /*
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
    public int getLeftArchLevel(){
        return leftArchLevel;
    }
    public int getLeftBackLevel(){
        return leftBackLevel;
    }
    public int getRightArchLevel() {
        return rightArchLevel;
    }

    public int getRightBackLevel() {
        return rightBackLevel;
    }
}
