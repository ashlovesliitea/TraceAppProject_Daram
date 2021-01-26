package com.example.traceappproject_daram;

import android.util.Log;

public class FootMultiFrames {
    private FootOneFrame[][] frames= new FootOneFrame[NUM_FRAMES][2];;//첫번째 인덱스는 시간, 두번째는 오왼
    private int insertIdx=0;
    public static final int NUM_FRAMES = 5;
    public String TAG = "FootMultiFrames";
    public FootMultiFrames(){
        initFramesForTest();
    }
    //foot one frame을 어케 저장할까..
    public void initFramesForTest(){
        for(int i = 0;i<NUM_FRAMES;i++){
            if(i%2 ==0){
                frames[i][0] = new FootOneFrame(false,0);
                frames[i][1] = new FootOneFrame(true,0);
            }
            else{
                frames[i][0] = new FootOneFrame(false,1);
                frames[i][1] = new FootOneFrame(true,1);
            }
        }
    }
    //이게 될지 모르겠다
    public FootOneFrame[] retrieveFrame(int idx){
        Log.i(TAG,"retrieving frame idx "+idx);
        Log.i(TAG,"first value of each foot "+frames[idx][0].ratioW[0]+" , "+frames[idx][1].ratioW[0]);
        Log.i(TAG,"first value of each foot "+frames[idx][0].ps[2]+" , "+frames[idx][1].ps[2]);
        return frames[idx];
    }
}
