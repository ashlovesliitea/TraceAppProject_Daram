package com.example.traceappproject_daram.reprot_page.heatmap;

import android.util.Log;

import com.example.traceappproject_daram.data.Cons;

public class FeetMultiFrames {
    //frame이 상수로 박혀있는 건 좋지 않은 듯
    private FootOneFrame[][] frames= new FootOneFrame[Cons.HEATMAP_FRAMES_NUM][2];;//첫번째 인덱스는 시간, 두번째는 왼오
    private int insertIdx=0;
    public String TAG = "FootMultiFrames";



    //나중에 파라미터 받을 수 있게되면 동적할당버전 만들기
    public FeetMultiFrames(){
        initFramesForTest();
    }
    public void appendFootFrame(FootOneFrame left, FootOneFrame right){ //시간적으로 일치해야만 append 가능
        //일단은 counter 2개 다 두면서 일치할 때만 넣는걸로
        //왼발은 실측값으로 오른 발은 그냥 더미로

        frames[insertIdx][0] = left;
        frames[insertIdx][1] = right;
        insertIdx++;
    }

    //foot one frame을 어케 저장할까..
    public void initFramesForTest(){
        for(int i = 0; i< Cons.HEATMAP_FRAMES_NUM; i++){
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
