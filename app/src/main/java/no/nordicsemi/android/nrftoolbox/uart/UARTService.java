/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.nrftoolbox.uart;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.daram.trace.Util;
import com.daram.trace.data.Result;
import com.daram.trace.measure_page.AnalyzeActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import no.nordicsemi.android.log.Logger;
import no.nordicsemi.android.nrftoolbox.FeaturesActivity;
import no.nordicsemi.android.nrftoolbox.R;
import no.nordicsemi.android.nrftoolbox.ToolboxApplication;
import no.nordicsemi.android.nrftoolbox.profile.BleProfileService;
import no.nordicsemi.android.nrftoolbox.profile.LoggableBleManager;
import no.nordicsemi.android.nrftoolbox.wearable.common.Constants;

public class UARTService extends BleProfileService implements UARTManagerCallbacks {
    private static final String TAG = "UARTService";

    public static final String BROADCAST_UART_TX = "no.nordicsemi.android.nrftoolbox.uart.BROADCAST_UART_TX";
    public static final String BROADCAST_UART_RX = "no.nordicsemi.android.nrftoolbox.uart.BROADCAST_UART_RX";
    public static final String EXTRA_DATA = "no.nordicsemi.android.nrftoolbox.uart.EXTRA_DATA";
    /**
     * A broadcast message with this action and the message in {@link Intent#EXTRA_TEXT} will be sent t the UART device.
     */
    public final static String ACTION_SEND = "no.nordicsemi.android.nrftoolbox.uart.ACTION_SEND";
    /**
     * A broadcast message with this action is triggered when a message is received from the UART device.
     */
    private final static String ACTION_RECEIVE = "no.nordicsemi.android.nrftoolbox.uart.ACTION_RECEIVE";
    /**
     * Action send when user press the DISCONNECT button on the notification.
     */
    public final static String ACTION_DISCONNECT = "no.nordicsemi.android.nrftoolbox.uart.ACTION_DISCONNECT";
    /**
     * A source of an action.
     */
    public final static String EXTRA_SOURCE = "no.nordicsemi.android.nrftoolbox.uart.EXTRA_SOURCE";
    public final static int SOURCE_NOTIFICATION = 0;
    public final static int SOURCE_WEARABLE = 1;
    public final static int SOURCE_3RD_PARTY = 2;

    private final static int NOTIFICATION_ID = 349; // random
    private final static int OPEN_ACTIVITY_REQ = 67; // random
    private final static int DISCONNECT_REQ = 97; // random

    private GoogleApiClient googleApiClient;
    private UARTManager manager;

    private final LocalBinder binder = new UARTBinder();
    int howManySent=0;
    public class UARTBinder extends LocalBinder implements UARTInterface {
        @Override
        public void send(final String text) {
            manager.send(text);
            howManySent++;
            Log.i("UARTService","sent idx : "+howManySent);
        }
    }

    @Override
    protected LocalBinder getBinder() {
        return binder;
    }

    @Override
    protected LoggableBleManager<UARTManagerCallbacks> initializeManager() {

        return manager = new UARTManager(this);
    }

    @Override
    protected boolean shouldAutoConnect() {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerReceiver(disconnectActionBroadcastReceiver, new IntentFilter(ACTION_DISCONNECT));
        registerReceiver(intentBroadcastReceiver, new IntentFilter(ACTION_SEND));

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        // when user has disconnected from the sensor, we have to cancel the notification that we've created some milliseconds before using unbindService
        stopForegroundService();
        unregisterReceiver(disconnectActionBroadcastReceiver);
        unregisterReceiver(intentBroadcastReceiver);

        googleApiClient.disconnect();

        super.onDestroy();
    }

    @Override
    protected void onRebind() {
        stopForegroundService();
    }

    @Override
    protected void onUnbind() {
        startForegroundService();
    }

    @Override
    public void onDeviceConnected(@NonNull final BluetoothDevice device) {
        super.onDeviceConnected(device);
        sendMessageToWearables(Constants.UART.DEVICE_CONNECTED, notNull(getDeviceName()));
    }

    @Override
    public void onBondingFailed(@NonNull BluetoothDevice device) {
        super.onBondingFailed(device);
        Log.i(TAG,"onBondingFailed : "+device.getName()+" , "+UARTConnector.connectionMode);
    }

    @Override
    protected boolean stopWhenDisconnected() {
        return false;
    }

    @Override
    public void onDeviceDisconnected(@NonNull final BluetoothDevice device) {
        super.onDeviceDisconnected(device);
        sendMessageToWearables(Constants.UART.DEVICE_DISCONNECTED, notNull(getDeviceName()));
    }

    @Override
    public void onLinkLossOccurred(@NonNull final BluetoothDevice device) {
        super.onLinkLossOccurred(device);
        sendMessageToWearables(Constants.UART.DEVICE_LINKLOSS, notNull(getDeviceName()));
    }

    private String notNull(final String name) {
        if (!TextUtils.isEmpty(name))
            return name;
        return getString(R.string.not_available);
    }
    private boolean isCon(char b1,char b2){
        if((b2 ==b1+1)||(b1 ==0xff&&b2 ==0x00)){
            return true;
        }
        else{
            return false;
        }
    }
    long beforetime;
    int howManyRcv = 0;
    private Result result;//일단 있다 치고 나중에쓰기
    //arr에 저장한 뒤 나중에 매핑
    public void appendToArr(byte[] byteSet){ //append multiple data
        // do not check validity
        int howmany = byteSet.length-2;
        System.arraycopy(byteSet,2,UARTConnector.arr, Util.idx,howmany);
        Util.idx+=howmany;
        Log.i(TAG,"appendToArr에서의 idx 변화 : "+ Util.idx);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDataReceived(@NonNull final BluetoothDevice device, final String data) {

        final Intent broadcast = new Intent(BROADCAST_UART_RX);
        broadcast.putExtra(EXTRA_DEVICE, getBluetoothDevice());
        broadcast.putExtra(EXTRA_DATA, data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);

        Log.i(TAG,"onDataReceived : "+data.length()+" , "+ Util.idx);

        //version일 때 UARTConnector 변수에 저장해둬야함
        if(data.charAt(1)==Cons.MODE_MOLD){
            AnalyzeActivity.result.setVersion((int)data.charAt(2));
            Log.i(TAG,"깔창 종류 파싱 : "+String.format("%8s", Integer.toBinaryString((int)data.charAt(2) & 0xFF)).replace(' ', '0'));
            if(UARTConnector.connectionMode == 2) {
                final Intent broadcast2 = new Intent(BleProfileService.BROADCAST_CONNECTION_STATE);
                //broadcast2.setAction(BleProfileService.BROADCAST_CONNECTION_STATE);
                broadcast2.putExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.CUSTOM_LEFT_DATA_DONE);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast2);
            }
            if(UARTConnector.connectionMode == 3){
                final Intent rightInitDone = new Intent(BleProfileService.BROADCAST_CONNECTION_STATE);
                //broadcast2.setAction(BleProfileService.BROADCAST_CONNECTION_STATE);
                rightInitDone.putExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.CUSTOM_RIGHT_DATA_DONE);
                LocalBroadcastManager.getInstance(this).sendBroadcast(rightInitDone);
            }
            //일단은 disconnect
            return;
        }

        if(howManyRcv==0) {
            beforetime = System.currentTimeMillis();
            Log.i("UARTService", "first data recieved : " + beforetime);
        }
        //없앨 예정
        if(data.length() != 237){ //마지막거는 똑같이 mtu 237 받을 거 같은데?
            Log.i("UARTService","wrong length data "+ data.length());
            //나중에 시간 여건되면 validity 여기서 체크해도 댐
        }
        //하드웨어가 달라져서 넣는 코드

        boolean isMeasure = (int)data.charAt(1) == (int) 0||
                (int)data.charAt(1) == (int) 1||(int) data.charAt(1) ==(int) Cons.MODE_MEASURE_RIGHT;
        Log.i("UARTService","how many rcvd = "+howManyRcv+data+"\n is measure?"+isMeasure);
        Log.i("UARTService","difference in int : "+(int)data.charAt(1)+" , "+ (int)Cons.MODE_MEASURE_LEFT+ ","+ (int) Cons.MODE_MEASURE_RIGHT);
        if(isMeasure&& Util.idx < 1200){
            //테스트를 위해 한계 걸어둠
            //첫번째 세트 delimeter
            if(Util.idx <300) {
                //Log.i(TAG,"not 10th char : "+(data.length()>=11?data.charAt(10):"there is no 10th char"));
                //다시 보내기는 잠시 주석
                manager.send("" + data.charAt(1));

                appendToArr(data.getBytes());

                Log.i(TAG,"measure reply 10th char : "+(data.length()>=11?(data.charAt(10)):("less then 10 : "+data.length())));
                //manager.send("" + (char) Cons.);
                //한쪽발에 대해서 measure 종료됐다는 broadcast
                //result에 arr를 전달해야한다.

                final Intent broadcast3 = new Intent(BleProfileService.BROADCAST_CONNECTION_STATE);
                Log.i(TAG,"setting data to result : "+Util.idx+" , ");
                if(UARTConnector.connectionMode ==2){
                    AnalyzeActivity.result.setLeftData(UARTConnector.arr, Util.idx);
                    //TODO: 더미로 단순 증가하게 설정한 거 지워야댐
                    //AnalyzeActivity.result.setLeftAsDummy();
                    //broadcast3.putExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.CUSTOM_LEFT_DATA_DONE);
                }
                if(UARTConnector.connectionMode ==3) {
                    AnalyzeActivity.result.setRightData(UARTConnector.arr, Util.idx);
                    //TODO: 더미로 단순 증가하게 설정한 거 지워야댐
                    //AnalyzeActivity.result.setRightAsDummy();
                    //broadcast3.putExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.CUSTOM_RIGHT_DATA_DONE);
                }
                //LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast3);
                manager.send(""+(char)Cons.MODE_MOLD);
                Log.i(TAG,"sending broadcast : ");

            }
            else{
                Log.i(TAG,"measure reply 10th char else: "+(data.length()>=11?(data.charAt(10)):" less then 10 : "+data.length()));
                //manager.send("" + (char) Cons.MODE_STOP);
                //한쪽발에 대해서 measure 종료됐다는 broadcast
                //result에 arr를 전달해야한다.

                final Intent broadcast3 = new Intent(BleProfileService.BROADCAST_CONNECTION_STATE);
                if(UARTConnector.connectionMode ==2){
                    AnalyzeActivity.result.setLeftData(UARTConnector.arr, Util.idx);
                    broadcast3.putExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.CUSTOM_LEFT_DATA_DONE);

                }
                if(UARTConnector.connectionMode ==3){
                    AnalyzeActivity.result.setRightData(UARTConnector.arr, Util.idx);
                    broadcast3.putExtra(BleProfileService.EXTRA_CONNECTION_STATE, BleProfileService.CUSTOM_RIGHT_DATA_DONE);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast3);

            }
        }
        /*
        // send the data received to other apps, e.g. the Tasker
        final Intent globalBroadcast = new Intent(ACTION_RECEIVE);
        globalBroadcast.putExtra(BluetoothDevice.EXTRA_DEVICE, getBluetoothDevice());
        globalBroadcast.putExtra(Intent.EXTRA_TEXT, data);
        sendBroadcast(globalBroadcast);
        */
        long aftertime = System.currentTimeMillis();
        /*
        Log.i("UARTService","몇 개 보냈나 : "+howManySent+" 몇번째로 받는가 : "+howManyRcv+
                ", 길이는 얼마인가 : "+data.length()+" 첨 받을때로부터의 시간차ms: "+((aftertime-beforetime)));
        Log.i("UARTService","데이터 출력"+data);
        String logAppend = "";
        for(int id = 0;id<data.length();id++){
            logAppend +=id+"/" + (int)data.charAt(id)+",";
        }
        Log.i("UARTService",logAppend);
        */

        howManyRcv++;
    }

    @Override
    public void onDataSent(@NonNull final BluetoothDevice device, final String data) {

        final Intent broadcast = new Intent(BROADCAST_UART_TX);
        broadcast.putExtra(EXTRA_DEVICE, getBluetoothDevice());
        broadcast.putExtra(EXTRA_DATA, data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    /**
     * Sends the given message to all connected wearables. If the path is equal to {@link Constants.UART#DEVICE_DISCONNECTED} the service will be stopped afterwards.
     *
     * @param path    message path
     * @param message the message
     */
    private void sendMessageToWearables(@NonNull final String path, @NonNull final String message) {
        if (googleApiClient.isConnected()) {
            new Thread(() -> {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
                for (Node node : nodes.getNodes()) {
                    Logger.v(getLogSession(), "[WEAR] Sending message '" + path + "' to " + node.getDisplayName());
                    final MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), path, message.getBytes()).await();
                    if (result.getStatus().isSuccess()) {
                        Logger.i(getLogSession(), "[WEAR] Message sent");
                    } else {
                        Logger.w(getLogSession(), "[WEAR] Sending message failed: " + result.getStatus().getStatusMessage());
                        Log.w(TAG, "Failed to send " + path + " to " + node.getDisplayName());
                    }
                }
                if (Constants.UART.DEVICE_DISCONNECTED.equals(path))
                    stopService();
            }).start();
        } else {
            if (Constants.UART.DEVICE_DISCONNECTED.equals(path))
                stopService();
        }
    }

    /**
     * Sets the service as a foreground service
     */
    private void startForegroundService() {
        // when the activity closes we need to show the notification that user is connected to the peripheral sensor
        // We start the service as a foreground service as Android 8.0 (Oreo) onwards kills any running background services
        final Notification notification = createNotification(R.string.uart_notification_connected_message, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, notification);
        } else {
            final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * Stops the service as a foreground service
     */
    private void stopForegroundService() {
        // when the activity rebinds to the service, remove the notification and stop the foreground service
        // on devices running Android 8.0 (Oreo) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            cancelNotification();
        }
    }

    /**
     * Creates the notification
     *
     * @param messageResId message resource id. The message must have one String parameter,<br />
     *                     f.e. <code>&lt;string name="name"&gt;%s is connected&lt;/string&gt;</code>
     * @param defaults     signals that will be used to notify the user
     */
    @SuppressWarnings("SameParameterValue")
    protected Notification createNotification(final int messageResId, final int defaults) {
        final Intent parentIntent = new Intent(this, FeaturesActivity.class);
        parentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final Intent targetIntent = new Intent(this, UARTActivity.class);

        final Intent disconnect = new Intent(ACTION_DISCONNECT);
        disconnect.putExtra(EXTRA_SOURCE, SOURCE_NOTIFICATION);
        final PendingIntent disconnectAction = PendingIntent.getBroadcast(this, DISCONNECT_REQ, disconnect, PendingIntent.FLAG_UPDATE_CURRENT);

        // both activities above have launchMode="singleTask" in the AndroidManifest.xml file, so if the task is already running, it will be resumed
        final PendingIntent pendingIntent = PendingIntent.getActivities(this, OPEN_ACTIVITY_REQ, new Intent[]{parentIntent, targetIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ToolboxApplication.CONNECTED_DEVICE_CHANNEL);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(getString(R.string.app_name)).setContentText(getString(messageResId, getDeviceName()));
        builder.setSmallIcon(R.drawable.ic_stat_notify_uart);
        builder.setShowWhen(defaults != 0).setDefaults(defaults).setAutoCancel(true).setOngoing(true);
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_action_bluetooth, getString(R.string.uart_notification_action_disconnect), disconnectAction));

        return builder.build();
    }

    /**
     * Cancels the existing notification. If there is no active notification this method does nothing
     */
    private void cancelNotification() {
        final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }

    /**
     * This broadcast receiver listens for {@link #ACTION_DISCONNECT} that may be fired by pressing Disconnect action button on the notification.
     */
    private final BroadcastReceiver disconnectActionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final int source = intent.getIntExtra(EXTRA_SOURCE, SOURCE_NOTIFICATION);
            switch (source) {
                case SOURCE_NOTIFICATION:
                    Logger.i(getLogSession(), "[Notification] Disconnect action pressed");
                    break;
                case SOURCE_WEARABLE:
                    Logger.i(getLogSession(), "[WEAR] '" + Constants.ACTION_DISCONNECT + "' message received");
                    break;
            }
            if (isConnected())
                getBinder().disconnect();
            else
                stopSelf();
        }
    };

    /**
     * Broadcast receiver that listens for {@link #ACTION_SEND} from other apps. Sends the String or int content of the {@link Intent#EXTRA_TEXT} extra to the remote device.
     * The integer content will be sent as String (65 -> "65", not 65 -> "A").
     */
    private BroadcastReceiver intentBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final boolean hasMessage = intent.hasExtra(Intent.EXTRA_TEXT);
            if (hasMessage) {
                String message = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (message == null) {
                    final int intValue = intent.getIntExtra(Intent.EXTRA_TEXT, Integer.MIN_VALUE); // how big is the chance of such data?
                    if (intValue != Integer.MIN_VALUE)
                        message = String.valueOf(intValue);
                }

                if (message != null) {
                    final int source = intent.getIntExtra(EXTRA_SOURCE, SOURCE_3RD_PARTY);
                    switch (source) {
                        case SOURCE_WEARABLE:
                            Logger.i(getLogSession(), "[WEAR] '" + Constants.UART.COMMAND + "' message received with data: \"" + message + "\"");
                            break;
                        case SOURCE_3RD_PARTY:
                        default:
                            Logger.i(getLogSession(), "[Broadcast] " + ACTION_SEND + " broadcast received with data: \"" + message + "\"");
                            break;
                    }
                    manager.send(message);
                    return;
                }
            }
            // No data od incompatible type of EXTRA_TEXT
            if (!hasMessage)
                Logger.i(getLogSession(), "[Broadcast] " + ACTION_SEND + " broadcast received no data.");
            else
                Logger.i(getLogSession(), "[Broadcast] " + ACTION_SEND + " broadcast received incompatible data type. Only String and int are supported.");
        }
    };
}
