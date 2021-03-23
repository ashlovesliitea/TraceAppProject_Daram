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
    byte[] empt1;
    byte[] empt2;
    byte[] back1;
    byte[] back2 ;
    byte[] bami1;
    byte[] bami2;
    byte[] midd1;
    byte[] midd2;
    byte[] miar1;
    byte[] miar2;
    byte[] arch1;
    byte[] arch2;

    public FeetMultiFrames(){

    }
    //나중에 파라미터 받을 수 있게되면 동적할당버전 만들기

    public FeetMultiFrames(int tc){
        if(tc ==0) {
            //              0 1 2 3 4 5 6 7
            empt1 = new byte[]{6,5,4,0,0,4,5,5};
            empt2 = new byte[]{3,4,5,0,2,5,3,4};
            back1 = new byte[]{0,0,0,0,5,8,9,9};
            back2 = new byte[]{0,0,0,0,4,7,9,9};
            bami1 = new byte[]{7,1,5,6,2,9,8,9};
            bami2 = new byte[]{5,1,6,9,4,6,9,9};
            midd1 = new byte[]{3,8,7,7,7,9,6,9};
            midd2 = new byte[]{2,8,6,7,6,8,7,9};
            miar1 = new byte[]{9,9,8,5,4,7,7,9};
            miar2 = new byte[]{9,9,7,2,7,5,9,9};
            arch1 = new byte[]{9,9,9,7,5,1,3,3};
            arch2 = new byte[]{7,7,4,0,8,0,7,6};
        }
        else if(tc ==1){
            empt1 = new byte[]{7,8,1,2,3,1,9,8};
            empt2 = new byte[]{4,8,2,0,1,3,9,8};
            back1 = new byte[]{0,0,0,5,3,2,7,8};
            back2 = new byte[]{0,0,0,6,0,4,2,9};
            bami1 = new byte[]{9,1,2,6,2,7,8,8};
            bami2 = new byte[]{4,5,8,1,2,4,9,8};
            midd1 = new byte[]{4,4,2,8,1,9,7,9};
            midd2 = new byte[]{8,5,2,9,3,9,8,9};
            miar1 = new byte[]{9,9,7,9,2,3,8,2};
            miar2 = new byte[]{7,9,7,5,4,5,7,9};
            arch1 = new byte[]{8,9,9,3,4,2,0,8};
            arch2 = new byte[]{7,7,4,1,8,9,9,9};
        }
        else if(tc ==2){
            empt1 = new byte[]{7,5,2,0,1,3,5,5};
            empt2 = new byte[]{6,5,5,0,0,3,6,4};
            back1 = new byte[]{0,0,1,0,6,9,9,9};
            back2 = new byte[]{0,0,0,2,2,9,8,9};
            bami1 = new byte[]{6,1,5,6,1,9,8,8};
            bami2 = new byte[]{4,0,7,9,2,6,9,9};
            midd1 = new byte[]{3,8,7,9,7,9,6,9};
            midd2 = new byte[]{2,7,6,7,8,8,9,8};
            miar1 = new byte[]{9,9,6,5,3,7,8,9};
            miar2 = new byte[]{9,9,7,1,8,4,9,8};
            arch1 = new byte[]{9,9,8,7,7,2,2,2};
            arch2 = new byte[]{9,7,3,0,9,0,8,6};
        }
        initFramesForTest();
        frameNum = 9;
    }
    public void appendAFrame(FootOneFrame frame, int idx, boolean isRight){
        Log.i(TAG,"appending a frame : "+ idx+ " , "+isRight+" , "+frame.ratioH[0]+" , "+frame.ratioW[0]+" , "+frame.ps[0]);
        frames[idx][(isRight?1:0)] = frame;
    }
    /*
    public void appendFootFrame(FootOneFrame left, FootOneFrame right){ //시간적으로 일치해야만 append 가능
        //일단은 counter 2개 다 두면서 일치할 때만 넣는걸로
        //왼발은 실측값으로 오른 발은 그냥 더미로

        frames[insertIdx][0] = left;
        frames[insertIdx][1] = right;
        insertIdx++;
    }
    */
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
        Log.i(TAG,"first value of each foot "+frames[idx][0].ratioW[0] + " , " + frames[idx][1].ratioW[0]);
        Log.i(TAG,"first value of each foot "+frames[idx][0].ps[2]+" , "+frames[idx][1].ps[2]);
        return frames[idx];
    }
}
