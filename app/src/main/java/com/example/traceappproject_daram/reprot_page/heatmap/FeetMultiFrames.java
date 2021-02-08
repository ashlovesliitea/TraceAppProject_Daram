package com.example.traceappproject_daram.reprot_page.heatmap;

import android.util.Log;

import com.example.traceappproject_daram.data.Cons;

public class FeetMultiFrames {
    //frame이 상수로 박혀있는 건 좋지 않은 듯
    private FootOneFrame[][] frames= new FootOneFrame[Cons.HEATMAP_FRAMES_NUM][2];;//첫번째 인덱스는 시간, 두번째는 왼오
    private int insertIdx=0;
    public String TAG = "FootMultiFrames";

    //              0 1 2 3 4 5 6 7 8
    byte[] empty = {5,5,0,0,0,5,3,5};
    byte[] back1 = {0,0,0,0,5,7,7,9};
    byte[] back2 = {0,0,0,0,0,0,7,9};
    byte[] bami1 = {0,0,0,0,0,7,7,9};
    byte[] bami2 = {0,0,0,0,0,7,7,9};
    byte[] midd1 = {3,3,7,3,6,6,9,9};
    byte[] midd2 = {4,0,0,0,6,6,6,6};
    byte[] miar1 = {0,0,0,0,0,7,7,9};
    byte[] miar2 = {0,0,0,0,0,7,7,9};
    byte[] arch1 = {7,7,7,0,0,0,1,1};
    byte[] arch2 = {9,7,5,0,0,0,3,3};



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
        frames[0][0] = new FootOneFrame(back1,false);
        frames[0][1] = new FootOneFrame(empty,true);
        frames[1][0] = new FootOneFrame(midd1,false);
        frames[1][1] = new FootOneFrame(empty,true);
        frames[2][0] = new FootOneFrame(arch1,false);
        frames[2][1] = new FootOneFrame(empty,true);
        frames[3][0] = new FootOneFrame(empty,false);
        frames[3][1] = new FootOneFrame(back2,true);
        frames[4][0] = new FootOneFrame(empty,false);
        frames[4][1] = new FootOneFrame(midd2,true);
        frames[5][0] = new FootOneFrame(empty,false);
        frames[5][1] = new FootOneFrame(arch2,true);
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
