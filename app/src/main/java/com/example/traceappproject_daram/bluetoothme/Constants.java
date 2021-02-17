package com.example.traceappproject_daram.bluetoothme;

import java.util.UUID;

public class Constants {
    // used to identify adding bluetooth names
    public final static int REQUEST_ENABLE_BT = 3054;
    // used to request fine location permission
    public final static int REQUEST_FINE_LOCATION = 3055;
    // scan period in milliseconds
    public final static int SCAN_PERIOD = 5000;
    public static String SERVICE_STRING = "CB660002-4339-FF22-A1ED-DEBFED27BDB4";
    public static final UUID SERVICE_UUID = UUID.fromString(SERVICE_STRING);
    public static String CHARACTERISTIC_UUID = "CB660004-4339-FF22-A1ED-DEBFED27BDB4";
    public static String CONFIG_UUID = "00005609-0000-1001-8080-00705c9b34cb";
}
