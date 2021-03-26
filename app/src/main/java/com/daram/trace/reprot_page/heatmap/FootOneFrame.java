package com.daram.trace.reprot_page.heatmap;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.daram.trace.data.Cons;

import java.io.Serializable;

//왼발 오른발 관계 없이
//발 한 짝
public class FootOneFrame implements Serializable {
    public double[] ps = new double[Cons.SENSOR_NUM_FOOT];
    //일단 거의 고정돼있음
    //얘네는 static 하면 지금은 안됨! 왼발 오른발에 따라 계산하고 있어서
    //나중에 static 처리된 왼발 오른발 페어 만드셈
    public float[] ratioW = new float[Cons.SENSOR_NUM_FOOT];
    public float[] ratioH = new float[Cons.SENSOR_NUM_FOOT];
    //왼발 기준 좌표
    //사진으로부터 센서 위치를 정확하게 매핑했었는데 안 예뻐요 ㅠㅠ
    /*
    public static final int[] EX_WIDTH = {1650,1850,1950,1450,1500,1700,1500,1900,1750};
    public static final int[] EX_HEIGHT = {1000,1000,1400,1650,2450,2400,3250,3400,3600};
    */
    //밑에건 그냥 임의로 예쁘게 매핑한거
    //사진 크롭하게 되면서 FINAL 이 빠지게됨
    public static int[] EX_WIDTH =  {600,500,750,400,700,600,500,750};
    public static int[] EX_HEIGHT = {500,500,850,1200,1200,1800,2350,2350};

    //crop후 width
    public static final int EX_FULL_WIDTH = 2080;
    public static final int EX_FULL_HEIGHT = 2863;

    public boolean isRight = false;

    public String getVals(){
        String ret= "";
        for(int i = 0;i<Cons.SENSOR_NUM_FOOT;i++){
            ret+=Double.toString(this.ps[i])+" ";
        }
        return ret;
    }

    /*
    public FootOneFrame(boolean isRight){
        //calcRatio();//이건 static 하게 만들어서 처음에 한 번만 호출되게 하자
        calcRatio();
        setPsAsExample();
        this.isRight = isRight;
        if(isRight){
            makeMeRight();
            setPsAsExample2();
        }
    }

    public FootOneFrame(boolean isRight, int version){
        calcRatio();
        this.isRight = isRight;
        if(!isRight){
            makeMeRight();
        }

    }
     */
    public String TAG = "FootOneFrame";
    @RequiresApi(api = Build.VERSION_CODES.O)
    public FootOneFrame(byte[] rawData, int sidx, boolean isRight){
        Log.i(TAG,"foot one frame called"+Cons.SENSOR_NUM_FOOT+" , "+sidx+" , "+rawData[sidx]+","+rawData[sidx+1]+rawData[sidx+2]);
        for(int i = 0;i<Cons.SENSOR_NUM_FOOT;i++){
            ps[i] = (double)Byte.toUnsignedInt(rawData[sidx+i]);
            //Log.i(TAG,"RAW DATA CONVERSION : "+rawData[sidx+i]+","+(int) rawData[sidx+i]+","+(double)(int)rawData[sidx+i]);
        }
        calcRatio();
        Log.i(TAG,"after calcRatio : "+ ratioW[0]+","+ratioH[0]+","+ps[0]+","+ps[1]);
        if(isRight){
            //이게 맞는 거 같은데 반대일수도있음
            makeMeRight();
        }
    }

    public FootOneFrame(byte[] rawData,boolean isRight){
        calcRatio();
        if(isRight){
            makeMeRight();
        }
        for(int i = 0;i<Cons.SENSOR_NUM_FOOT;i++){
            ps[i] = (double)rawData[i];
        }
    }


      //  calcRatio();
    public void setPtIdx(int idx, double p){//assign pressure to points
        ps[idx] = p;
    }
    /*
    public void makeMeRight() {
        //양발이 같은 높이이기 때문에 w만 대칭
        for (int i = 0; i < Cons.SENSOR_NUM_FOOT; i++) {
            ratioW[i] = (float) ratioW[i] + (float) ((float) 800 / (double) EX_FULL_WIDTH);
        }
        calcRatio();
    }
    */

    public void makeMeRight(){
        //양발이 같은 높이이기 때문에 w만 대칭
        //데모는 대칭으로 해야 편한 건 맞는데
        //실측값은 그대로 0-7센서가 왼발이랑 똑같이 들어오니까 평행이동으로 처리해야댐
        this.isRight =true;
        for(int i = 0; i< Cons.SENSOR_NUM_FOOT; i++) {
            ratioW[i] = (float) ratioW[i] + (float)((float)800/ (double) EX_FULL_WIDTH);
        }
    }


    //calc ratio of the picture
    public void calcRatio(){
        for(int i = 0; i< Cons.SENSOR_NUM_FOOT; i++){
            ratioW[i] = (float) EX_WIDTH[i]/(float) EX_FULL_WIDTH;
            ratioH[i] = (float) EX_HEIGHT[i]/(float) EX_FULL_HEIGHT;
        }
    }
    /*
    public void setPsAsExample2() {
        //어떻게 하면 툭 튀어나오는 걸 막을 수 있을까.. 강도를 gradient 끝에보다 약하게 하면 좋을 텐데
        double ds[] = {7, 9, 9, 5, 4, 3, 7, 8, 8};
        for (int i = 0; i < Cons.SENSOR_NUM_FOOT; i++) {
            ps[i] = ds[i];
        }
    }
    public void setPsAsExample(){
        //어떻게 하면 툭 튀어나오는 걸 막을 수 있을까.. 강도를 gradient 끝에보다 약하게 하면 좋을 텐데
        double ds[] = {7,9,1,9,9,1,8,8,5};
        for(int i = 0; i< Cons.SENSOR_NUM_FOOT; i++){
            ps[i] = ds[i];
        }
    }
     */
}
