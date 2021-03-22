package no.nordicsemi.android.nrftoolbox.scanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.profile.BleProfileServiceReadyActivity;
import no.nordicsemi.android.nrftoolbox.uart.UARTActivity;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class ScannerNoUI {
    private final static String TAG = "ScannerNoUI";

    private final static String PARAM_UUID = "param_uuid";
    private final static long SCAN_DURATION = 7000;

    private final static int REQUEST_PERMISSION_REQ_CODE = 34; // any 8-bit number

    private BluetoothAdapter bluetoothAdapter;
    private ScannerFragment.OnDeviceSelectedListener listener;
    private DeviceListAdapter adapter;
    private final Handler handler = new Handler();


    private View permissionRationale;

    private ParcelUuid uuid;
    private int connectionMode;
    private boolean scanning = false;
    private BleProfileServiceReadyActivity mother;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public ScannerNoUI(final UUID uuid, BleProfileServiceReadyActivity mother, int connectionMode) {
        this.uuid = new ParcelUuid(uuid);
        final Bundle args = new Bundle();
        if (uuid != null)
            args.putParcelable(PARAM_UUID, new ParcelUuid(uuid));
        this.mother = mother;
        this.connectionMode = connectionMode;
        this.listener = (ScannerFragment.OnDeviceSelectedListener) mother;
        //uuid 관련된 작업들
        //

        if (args != null && args.containsKey(PARAM_UUID)) {
            Log.i(TAG,"uuid = args.getParcelable");
            this.uuid = args.getParcelable(PARAM_UUID);
        }

        final BluetoothManager manager = (BluetoothManager) mother.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            bluetoothAdapter = manager.getAdapter();
        }
        startScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have been granted the Manifest.permission.ACCESS_FINE_LOCATION permission. Now we may proceed with scanning.
                    startScan();
                } else {
                    permissionRationale.setVisibility(View.VISIBLE);
                    Toast.makeText(mother, R.string.no_required_permission, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    private String DEVICE_NAMES[] = {"HD-CS-02L","HD-CS-02R","HD-CS-02L","HD-CS-02R"};
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, @NonNull final ScanResult result) {
            // do nothing
            Log.i(TAG,"onScanResult called : "+ result.getDevice().getAddress());
            //여기서 나중에 sacn result 중에 내가 원하는 기기 있는지 체크 후 자동연결
            //result에는 하나의 bluetooth device밖에 없음
            BluetoothDevice device = result.getDevice();

            if(device.getName()!=null&&device.getName().equals(DEVICE_NAMES[connectionMode])){
                //do connection work by mother
                Log.i(TAG,"HD device : " + device.getName()+" , connection mode : "+connectionMode);
                mother.onDeviceSelected(device, device.getName());
            }
            else {
                //do nothing
            }

        }
        //connectionMode에 따라 선택해야하는 기기가 달라짐



        @Override
        public void onBatchScanResults(@NonNull final List<ScanResult> results) {
            //이게 실질적으로 호출됨
            //adapter.update(results);
            Log.i(TAG,"onBatchScanResult called");

            Log.i(TAG,"update called");
            for (final ScanResult result : results) {
                //final ExtendedBluetoothDevice device = findDevice(result);
                final ExtendedBluetoothDevice device = new ExtendedBluetoothDevice(result);
                device.name = result.getScanRecord() != null ? result.getScanRecord().getDeviceName() : null;
                device.rssi = result.getRssi();

                Log.i("DeviceListAdapter","linear search : "+(result.getDevice().getName()==null ? "null" : device.name));
                if(device.name!=null&&device.name.startsWith("HD")){
                    Log.i(TAG,"found HD device , "+device.name+" , "+device.device.getAddress());
                }
                if(device.name!=null&&device.name.equals(DEVICE_NAMES[connectionMode])){
                    //HD면 바로 연결
                    Log.i("DeviceListAdapter","found correct device , connectionMode , name"+connectionMode+device.name);
                    // 클릭한 효과주기
                    mother.onDeviceSelected(device.device,device.name);
                    break;
                }
            }

        }

        @Override
        public void onScanFailed(final int errorCode) {
            // should never be called
        }
    };

    private void addBoundDevices() {
        final Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        adapter.addBondedDevices(devices);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startScan() {
        Log.i(TAG,"startScan called");

        // Since Android 6.0 we need to obtain Manifest.permission.ACCESS_FINE_LOCATION to be able to scan for
        // Bluetooth LE devices. This is related to beacons as proximity devices.
        // On API older than Marshmallow the following code does nothing.
        if (ContextCompat.checkSelfPermission(mother, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // When user pressed Deny and still wants to use this functionality, show the rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(mother, Manifest.permission.ACCESS_FINE_LOCATION) && permissionRationale.getVisibility() == View.GONE) {
                permissionRationale.setVisibility(View.VISIBLE);
                return;
            }

            mother.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
        /*
        // Hide the rationale message, we don't need it anymore.
        if (permissionRationale != null)
            permissionRationale.setVisibility(View.GONE);
        */

        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        Log.i(TAG,"getScanner called");

        final ScanSettings settings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(1000).setUseHardwareBatchingIfSupported(false).build();
        scanner.startScan(scanCallback);
        Log.i(TAG,"getSettingss called");
        /*
        final List<ScanFilter> filters = new ArrayList<>();

        filters.add(new ScanFilter.Builder().setServiceUuid(uuid).build());
        Log.i(TAG,"filter add called");
        scanner.startScan(filters, settings, scanCallback);

         */
        Log.i(TAG,"ble scanner.startscan called");
        scanning = true;
        handler.postDelayed(() -> {
            if (scanning) {
                stopScan();
            }
        }, SCAN_DURATION);

    }
    private void stopScan() {
        if (scanning) {
            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.stopScan(scanCallback);

            scanning = false;
        }
    }


}
