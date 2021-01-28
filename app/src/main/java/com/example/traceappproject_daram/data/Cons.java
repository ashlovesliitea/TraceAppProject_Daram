package com.example.traceappproject_daram.data;

public class Cons {
    public static final int NUM_FRAMES = 5; //constants
    public static final int SENSOR_PER_FOOT =9;
    public static final int MAX_RAWDATA_IDX = SENSOR_PER_FOOT * NUM_FRAMES *2;
    public static final int BACK_SENSOR_NUM = 3;
    public static final int ARCH_SENSOR_NUM= SENSOR_PER_FOOT -BACK_SENSOR_NUM;
    public static final int MEASURE_INTERVAL= 10;//ms 단위
    public static final int NUM_MIN_FRAMES = (1000-MEASURE_INTERVAL)*5;
    public static final int NUM_MAX_FRAMES=(1000-MEASURE_INTERVAL)*20;//10ms마다 측정&20초 걷기
    public static final int THRESH_ACTIVATED = 9;
}
