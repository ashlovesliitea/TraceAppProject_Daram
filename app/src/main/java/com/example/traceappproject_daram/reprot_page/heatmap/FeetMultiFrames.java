package com.example.traceappproject_daram.reprot_page.heatmap;

import android.util.Log;

import com.example.traceappproject_daram.data.Cons;

import java.io.Serializable;

public class FeetMultiFrames implements Serializable {
    //frame이 상수로 박혀있는 건 좋지 않은 듯
    private FootOneFrame[][] frames= new FootOneFrame[Cons.HEATMAP_FRAMES_NUM][2];;//첫번째 인덱스는 시간, 두번째는 왼오
    private int insertIdx=0;
    public int frameNum;
    public String TAG = "FootMultiFrames";

    //              0 1 2 3 4 5 6 7

    byte[] empt1 = {5,5,5,0,0,5,5,5};
    byte[] empt2 = {3,3,5,0,2,5,3,3};
    byte[] back1 = {0,0,0,0,5,7,7,9};
    byte[] back2 = {0,0,0,0,3,7,7,9};
    byte[] bami1 = {8,0,5,7,3,9,9,9};
    byte[] bami2 = {5,0,5,9,5,7,9,9};
    byte[] midd1 = {3,8,7,8,5,9,6,9};
    byte[] midd2 = {7,5,7,7,6,6,6,6};
    byte[] miar1 = {9,9,9,5,5,7,7,9};
    byte[] miar2 = {9,9,7,3,9,5,9,9};
    byte[] arch1 = {9,9,9,7,5,1,3,3};
    byte[] arch2 = {9,7,5,0,9,0,7,7};


    public FeetMultiFrames(){

    }
    //나중에 파라미터 받을 수 있게되면 동적할당버전 만들기
    /*
    public FeetMultiFrames(){
        initFramesForTest();
    }
     */
    public void appendFootFrame(FootOneFrame left, FootOneFrame right){ //시간적으로 일치해야만 append 가능
        //일단은 counter 2개 다 두면서 일치할 때만 넣는걸로
        //왼발은 실측값으로 오른 발은 그냥 더미로

        frames[insertIdx][0] = left;
        frames[insertIdx][1] = right;
        insertIdx++;
    }

    //foot one frame을 어케 저장할까..
    public void initFramesForTest(){
        frames[0][0] = new FootOneFrame(back1,false);
        frames[0][1] = new FootOneFrame(empt1,true);
        frames[1][0] = new FootOneFrame(bami1,false);
        frames[1][1] = new FootOneFrame(empt2,true);
        frames[2][0] = new FootOneFrame(midd1,false);
        frames[2][1] = new FootOneFrame(empt1,true);
        frames[3][0] = new FootOneFrame(miar1,false);
        frames[3][1] = new FootOneFrame(empt1,true);
        frames[4][0] = new FootOneFrame(arch1,false);
        frames[4][1] = new FootOneFrame(empt2,true);

        frames[5][0] = new FootOneFrame(empt1,false);
        frames[5][1] = new FootOneFrame(back2,true);
        frames[6][0] = new FootOneFrame(empt1,false);
        frames[6][1] = new FootOneFrame(bami2,true);
        frames[6][0] = new FootOneFrame(empt1,false);
        frames[6][1] = new FootOneFrame(midd2,true);
        frames[7][0] = new FootOneFrame(empt1,false);
        frames[7][1] = new FootOneFrame(miar2,true);
        frames[8][0] = new FootOneFrame(empt1,false);
        frames[8][1] = new FootOneFrame(arch2,true);
    }

    public void setFrameNum(int frameNum) {
        this.frameNum = frameNum;
    }

    public int getFramesSz(){
        return frameNum;
        // 일케 말고 frame 계싼해야댐
        //return frames.length;
    }
    public FootOneFrame[] retrieveFrame(int idx){
        Log.i(TAG,"retrieving frame idx "+idx);
        Log.i(TAG,"first value of each foot "+frames[idx][0].ratioW[0]+" , "+frames[idx][1].ratioW[0]);
        Log.i(TAG,"first value of each foot "+frames[idx][0].ps[2]+" , "+frames[idx][1].ps[2]);
        return frames[idx];
    }
}
