package no.nordicsemi.android.nrftoolbox.uart;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Cons {//constants
    public static final int HEATMAP_FRAMES_NUM = 5;//Result.java에선 안 쓸거고 heatmap 폴더에서만 적용돼있음
    public static final int SENSOR_NUM_FOOT =8;
    public static final int BACK_SENSOR_NUM = 3;
    public static final int THRESH_ACTIVATED = 9;
    public static final int REP_START_SEC = 5;
    public static final int REP_END_SEC = 7;
    public static final int ARCH_SENSOR_NUM= SENSOR_NUM_FOOT -BACK_SENSOR_NUM;
    public static final int MEASURE_INTERVAL_MS = 15;//ms 단위
    public static final int MEASURE_NUM_1SEC= 1000 / MEASURE_INTERVAL_MS;
    public static final int MIN_MEASURE_SEC = 2;
    public static final int MAX_MEASURE_SEC = 30;
    public static final int MIN_FRAMES_NUM = MEASURE_NUM_1SEC*MIN_MEASURE_SEC;
    public static final int MAX_FRAMES_NUM = MEASURE_NUM_1SEC*MAX_MEASURE_SEC;//10ms마다 측정&20초 걷기
    public static final byte DEL = 0x2C;//','

    /*

    public static final int MIN_RAWDATA_IDX = Result.calcSecToIdx(MIN_MEASURE_SEC);
    public static final int MAX_RAWDATA_IDX = Result.calcSecToIdx(MAX_MEASURE_SEC);
    public static final int REP_START_IDX = Result.calcSecToIdx(REP_START_SEC);
    public static final int REP_END_IDX = Result.calcSecToIdx(REP_END_SEC);
    public static final int REP_START_FRAME = Result.calcSecToFrame(REP_START_SEC);
    public static final int REP_END_FRAME = Result.calcSecToFrame(REP_END_SEC);

     */
    public static final String IMG_EXT = ".jpg";

    // used to request fine location permission
    public final static int REQUEST_FINE_LOCATION = 3055;
    // used to identify adding bluetooth names
    public final static int REQUEST_ENABLE_BT = 3054;
    // scan period in milliseconds
    public final static int SCAN_PERIOD = 5000;




    //여기는 하드웨어와의 모드 명령어
    //broadcast 명령어는 Bleprofileservice.java에 정의

    //Todo: 하드웨어 구현된 거 보고 다시 맞추기
    public final static byte MODE_RUN = 0X31;
    public final static byte MODE_STOP = 0X32;
    public final static byte MODE_MOLD = 0X36;

    public final static byte MODE_MEASURE_LEFT = 0X34; //양 발에 대해 센서값을 다
    public final static byte MODE_MEASURE_RIGHT = 0X35;
    public final static byte MODE_SENSOR = 0X35;
    public static String SERVICE_STRING = "CB660002-4339-FF22-A1ED-DEBFED27BDB4";
    public static final UUID SERVICE_UUID = UUID.fromString(SERVICE_STRING);
    public static String CHARACTERISTIC_UUID = "CB660004-4339-FF22-A1ED-DEBFED27BDB4";
    public static String CONFIG_UUID = "00005609-0000-1001-8080-00705c9b34cb";
}
