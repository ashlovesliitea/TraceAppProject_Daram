package com.example.traceappproject_daram.reprot_page.heatmap;

import android.util.Log;

import com.example.traceappproject_daram.data.Cons;

public class FeetMultiFrames {
    //frame이 상수로 박혀있는 건 좋지 않은 듯
    private FootOneFrame[][] frames= new FootOneFrame[Cons.HEATMAP_FRAMES_NUM][2];;//첫번째 인덱스는 시간, 두번째는 왼오
    private int insertIdx=0;
    public String TAG = "FootMultiFrames";

    //             0 1 2 3 4 5 6 7 8
    int[] empty = {0,0,0,0,0,0,0,0,0};
    int[] back1 = {0,0,0,0,0,0,7,7,9};
    int[] back2 = {0,0,0,0,0,0,0,7,9};
    int[] midd1 = {0,0,0,0,0,6,6,6,6};
    int[] midd2 = {0,0,0,0,0,6,6,6,6};
    int[] arch1 = {7,7,7,0,0,0,0,1,1};
    int[] arch2 = {9,7,5,0,0,0,0,3,3};



    //나중에 파라미터 받을 수 있게되면 동적할당버전 만들기
    public FeetMultiFrames(){
        initFramesForTest();
    }
    public void appendFootFrame(FootOneFrame left, FootOneFrame right){
        frames[insertIdx][0] = left;
        frames[insertIdx][1] = right;
        insertIdx++;
    }

    //foot one frame을 어케 저장할까..
    public void initFramesForTest(){
        frames[0][0] = new FootOneFrame(back1,0);
    }
    public int getFramesSz(){
        return frames.length;
    }
    public FootOneFrame[] retrieveFrame(int idx){
        Log.i(TAG,"retrieving frame idx "+idx);
        Log.i(TAG,"first value of each foot "+frames[idx][0].ratioW[0]+" , "+frames[idx][1].ratioW[0]);
        Log.i(TAG,"first value of each foot "+frames[idx][0].ps[2]+" , "+frames[idx][1].ps[2]);
        return frames[idx];
    }
}
