package com.example.traceappproject_daram;
//왼발 오른발 관계 없이
//발 한 짝
public class Feet {
    public static final int SENSOR_NUM =9;
    public double[] ps = new double[SENSOR_NUM];
    //일단 거의 고정돼있음
    public float[] ratioW = new float[SENSOR_NUM];
    public float[] ratioH = new float[SENSOR_NUM];
    //왼발 기준 좌표
    //사진으로부터 센서 위치를 정확하게 매핑했었는데 안 예뻐요 ㅠㅠ
    /*
    public static final int[] EX_WIDTH = {1650,1850,1950,1450,1500,1700,1500,1900,1750};
    public static final int[] EX_HEIGHT = {1000,1000,1400,1650,2450,2400,3250,3400,3600};
    */
    public static final int[] EX_WIDTH ={1550,1900, 1900,1475,1500,1550,1575,1725,1900};
    public static final int[] EX_HEIGHT = {1250,1200,1700,1625,2200,2600,3125,3425,3100};

    public static final int EX_FULL_WIDTH = 4521;
    public static final int EX_FULL_HEIGHT = 4418;
    public boolean isleft = true;

    public Feet(boolean isleft){
        calcRatio();
        setPsAsExample();
        this.isleft = isleft;
        if(!isleft){
            makeMeRight();
            setPsAsExample2();
        }
    }
    public void setPtIdx(int idx, double p){//assign pressure to points
        ps[idx] = p;
    }
    public void makeMeRight(){
        //양발이 같은 높이이기 때문에 w만 대칭
        for(int i = 0;i<SENSOR_NUM;i++) {
            ratioW[i] = (float) (0.5+(0.5-ratioW[i]));
        }
    }
    //calc ratio of the picture
    public void calcRatio(){
        for(int i = 0;i<SENSOR_NUM;i++){
            ratioW[i] = (float) EX_WIDTH[i]/(float) EX_FULL_WIDTH;
            ratioH[i] = (float) EX_HEIGHT[i]/(float) EX_FULL_HEIGHT;

        }
    }
    public void setPsAsExample2() {
        //어떻게 하면 툭 튀어나오는 걸 막을 수 있을까.. 강도를 gradient 끝에보다 약하게 하면 좋을 텐데
        double ds[] = {7, 9, 9, 5, 4, 3, 7, 8, 8};
        for (int i = 0; i < SENSOR_NUM; i++) {
            ps[i] = ds[i];
        }
    }
    public void setPsAsExample(){
        //어떻게 하면 툭 튀어나오는 걸 막을 수 있을까.. 강도를 gradient 끝에보다 약하게 하면 좋을 텐데
        double ds[] = {7,9,1,9,9,1,8,8,5};
        for(int i = 0;i<SENSOR_NUM;i++){
            ps[i] = ds[i];
        }
    }
}
