package com.example.traceappproject_daram.data;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class Cons {//constants

    public static final int HEATMAP_FRAMES_NUM = 9;//Result.java에선 안 쓸거고 heatmap 폴더에서만 적용돼있음
    public static final int SENSOR_NUM_FOOT =8;
    public static final int BACK_SENSOR_NUM = 3;
    public static final int THRESH_ACTIVATED = 9;
    public static final int REP_START_SEC = 5;
    public static final int REP_END_SEC = 7;
    public static final int ARCH_SENSOR_NUM= SENSOR_NUM_FOOT -BACK_SENSOR_NUM;
    public static final int MEASURE_INTERVAL_MS = 10;//ms 단위
    public static final int MEASURE_NUM_1SEC= 1000 / MEASURE_INTERVAL_MS;
    public static final int MIN_MEASURE_SEC = 2;
    public static final int MAX_MEASURE_SEC = 5;
    public static final int MIN_FRAMES_NUM = MEASURE_NUM_1SEC*MIN_MEASURE_SEC;
    public static final int MAX_FRAMES_NUM = MEASURE_NUM_1SEC*MAX_MEASURE_SEC;//10ms마다 측정&20초 걷기
    /*
    public static final int MIN_RAWDATA_IDX = Result.calcSecToIdx(MIN_MEASURE_SEC);
    public static final int MAX_RAWDATA_IDX = Result.calcSecToIdx(MAX_MEASURE_SEC);
    public static final int REP_START_IDX = Result.calcSecToIdx(REP_START_SEC);
    public static final int REP_END_IDX = Result.calcSecToIdx(REP_END_SEC);
    public static final int REP_START_FRAME = Result.calcSecToFrame(REP_START_SEC);
    public static final int REP_END_FRAME = Result.calcSecToFrame(REP_END_SEC);

    */
    public static final String IMG_EXT = ".png";

}
