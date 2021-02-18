/*
 * Copyright (c) 2018, Nordic Semiconductor
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

package no.nordicsemi.android.blinky.profile.callback;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.traceappproject_daram.bluetoothme.Constants;
import com.example.traceappproject_daram.data.Cons;
import com.example.traceappproject_daram.data.Result;
import com.example.traceappproject_daram.reprot_page.heatmap.FootOneFrame;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class BlinkyByteDataCallback implements ProfileDataCallback, BlinkyByteCallback {
    /*
    private static final int STATE_RELEASED = 0x00;
    private static final int STATE_PRESSED = 0x01;
     */
    private String TAG = "BlinkyByteDataCallback";
    private Result result;
    private byte EMPTY_PASTMODE = 0x11;//-1
    private byte pastMode;
    private int idx=0;
    private byte[] bytes;

    public BlinkyByteDataCallback(Result emptyResult){
        result= emptyResult;
    }

    private static int IDX = 0;

    @Override
    public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
        if (data.size() != 1) { //1byte가 아니면 받지를 않는다.<-이거는 그냥 버튼관련 커맨드라 이렇게 한듯?
            onInvalidDataReceived(device, data);
            return;
        }
        //final byte recieved = data.getByte(0);//일단 1byte라 이렇게 채워넣음 byte 배열도 있긴 한데 안함
        /*
        //version2 m커맨드시 2개 받는다 치기
        final byte[] rcvarr = data.getValue();
        int mode = rcvarr[0];
        Log.i(TAG, "onDataRecived : "+data.getStringValue(0));
        if(mode == Constants.MODE_RUN){
            //do nothing
        }
        else if(mode == Constants.MODE_STOP){
            //do nothing
        }
        else if(mode==Constants.MODE_VERSION){
            byte version = rcvarr[1];
            //어떤 버전인지 result에 넣는 작업 필요함
        }
        else{//measure left or right
            //8byte의 한쪽 발 센서 값을 한번에 받음
            byte[] foot= new byte[Cons.SENSOR_NUM_FOOT];
            for(int i = 0;i<Cons.SENSOR_NUM_FOOT;i++){
                foot[i] = rcvarr[i+1];
            }

            if(mode == Constants.MODE_MEASURE_LEFT){
                result.appendOneFrame(foot);//어차피 mod8의 홀짝으로 인덱스 나뉨
                Log.i(TAG,"왼발이 들어왔습니다");
            }
            else if(mode == Constants.MODE_MEASURE_RIGHT){
                result.appendOneFrame(foot);
            }
        }
        */
        //version 3
        byte oneb = data.getByte(0);
        if(pastMode==EMPTY_PASTMODE){
            pastMode = oneb;
            if(pastMode == Constants.MODE_RUN){
                idx =0;
            }
            else if(pastMode == Constants.MODE_MEASURE_LEFT||pastMode == Constants.MODE_MEASURE_RIGHT){
                idx=0;
            }
        }
        else{ //EMPTY가 아님
            if(pastMode == Constants.MODE_RUN){
                idx = 0;
            }
            else if(pastMode == Constants.MODE_MEASURE_LEFT||pastMode == Constants.MODE_MEASURE_RIGHT){


                idx++;

            }
        }


        /*
        if (state == STATE_PRESSED) {
            onByteStateChanged(device, true);
        } else if (state == STATE_RELEASED) {
            onByteStateChanged(device, false);
        } else {
            onInvalidDataReceived(device, data);
        }
         */
    }
}