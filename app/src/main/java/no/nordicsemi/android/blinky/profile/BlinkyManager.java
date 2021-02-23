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

package no.nordicsemi.android.blinky.profile;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.traceappproject_daram.data.Cons;
import com.example.traceappproject_daram.data.LoginInfo;
import com.example.traceappproject_daram.data.Result;

import java.util.UUID;

import no.nordicsemi.android.ble.ConnectionPriorityRequest;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.livedata.ObservableBleManager;
import no.nordicsemi.android.blinky.profile.callback.BlinkyButtonDataCallback;
import no.nordicsemi.android.blinky.profile.callback.BlinkyLedDataCallback;
import no.nordicsemi.android.blinky.profile.data.Constants;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

public class BlinkyManager extends ObservableBleManager {
	/** Nordic Blinky Service UUID. */
	public final static UUID LBS_UUID_SERVICE = UUID.fromString("00001523-1212-efde-1523-785feabcd123");
	/** BUTTON characteristic UUID. */
	private final static UUID LBS_UUID_BUTTON_CHAR = UUID.fromString("00001524-1212-efde-1523-785feabcd123");
	/** LED characteristic UUID. */
	private final static UUID LBS_UUID_LED_CHAR = UUID.fromString("00001525-1212-efde-1523-785feabcd123");
	private final static String TAG = "MJBlinkyManager";
	private final MutableLiveData<Boolean> ledState = new MutableLiveData<>();
	private final MutableLiveData<Boolean> buttonState = new MutableLiveData<>();
	//mj 0217 update
	private final MutableLiveData<Boolean> curVal = new MutableLiveData<>();

	private BluetoothGattCharacteristic buttonCharacteristic, ledCharacteristic;
	private LogSession logSession;
	private boolean supported;
	private boolean on;
	private Result result;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public BlinkyManager(@NonNull final Context context) {
		super(context);
		result = new Result(new LoginInfo("dummy","data"));
		this.requestConnectionPriority(ConnectionPriorityRequest.CONNECTION_PRIORITY_HIGH);

	}
	//여기서 led state 받는 거 말고 걍 데이터 받는 거 작성해야할덧

	public final LiveData<Boolean> getLedState() {
		return ledState;
	}

	public final LiveData<Boolean> getButtonState() {
		return buttonState;
	}

	@NonNull
	@Override
	protected BleManagerGattCallback getGattCallback() {
		return new BlinkyBleManagerGattCallback();
	}

	/**
	 * Sets the log session to be used for low level logging.
	 * @param session the session, or null, if nRF Logger is not installed.
	 */
	public void setLogger(@Nullable final LogSession session) {
		logSession = session;
	}

	@Override
	public void log(final int priority, @NonNull final String message) {
		// The priority is a Log.X constant, while the Logger accepts it's log levels.
		Logger.log(logSession, LogContract.Log.Level.fromPriority(priority), message);
	}

	@Override
	protected boolean shouldClearCacheWhenDisconnected() {
		return !supported;
	}

	/**
	 * The Button callback will be notified when a notification from Button characteristic
	 * has been received, or its data was read.
	 * <p>
	 * If the data received are valid (single byte equal to 0x00 or 0x01), the
	 * {@link BlinkyButtonDataCallback#onMeasureEnd(BluetoothDevice, Data)} will be called.
	 * Otherwise, the {@link BlinkyButtonDataCallback#onInvalidDataReceived(BluetoothDevice, Data)}
	 * will be called with the data received.
	 */
	private	final BlinkyButtonDataCallback buttonCallback = new BlinkyButtonDataCallback(result) {
		@Override
		public void onMeasureEnd(@NonNull BluetoothDevice device, Data data) {
			//이 blinky manager가 할 일이 다 끝남
		}
		/*
		@Override
		public void onButtonStateChanged(@NonNull final BluetoothDevice device,
										 final boolean pressed) {
			log(LogContract.Log.Level.APPLICATION, "Button " + (pressed ? "pressed" : "released"));
			buttonState.setValue(pressed);
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
										  @NonNull final Data data) {
			log(Log.WARN, "Invalid data received: " + data);
		}
		
		 */
	};

	/**
	 * The LED callback will be notified when the LED state was read or sent to the target device.
	 * <p>
	 * This callback implements both {@link no.nordicsemi.android.ble.callback.DataReceivedCallback}
	 * and {@link no.nordicsemi.android.ble.callback.DataSentCallback} and calls the same
	 * method on success.
	 * <p>
	 * If the data received were invalid, the
	 * {@link BlinkyLedDataCallback#onInvalidDataReceived(BluetoothDevice, Data)} will be
	 * called.
	 */
	private final BlinkyLedDataCallback ledCallback = new BlinkyLedDataCallback() {
		@Override
		public void onLedStateChanged(@NonNull final BluetoothDevice device,
									  final int mode) {
			//모드를 echoback하는 거
			/*
			on = mode;
			log(LogContract.Log.Level.APPLICATION, "LED " + (mode ? "ON" : "OFF"));
			ledState.setValue(mode);
			 */
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device,
										  @NonNull final Data data) {
			// Data can only invalid if we read them. We assume the app always sends correct data.
			log(Log.WARN, "Invalid data received: " + data);
		}
	};
	/*
	private final BlinkyByteDataCallbackOld byteCallback = new BlinkyByteDataCallbackOld(result) {
		@Override
		public void onByteRecieved(@NonNull BluetoothDevice device, Data data) {
			log(Log.DEBUG, "byte data recieved : "+data);
		}
	};
	*/
	/**
	 * BluetoothGatt callbacks object.
	 */
	private class BlinkyBleManagerGattCallback extends BleManagerGattCallback {
		@Override
		protected void initialize() {
			setNotificationCallback(buttonCharacteristic).with(buttonCallback);
			readCharacteristic(ledCharacteristic).with(ledCallback).enqueue();
			readCharacteristic(buttonCharacteristic).with(buttonCallback).enqueue();
			enableNotifications(buttonCharacteristic).enqueue();
		}

		@Override
		public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
			final BluetoothGattService service = gatt.getService(LBS_UUID_SERVICE);
			if (service != null) {
				buttonCharacteristic = service.getCharacteristic(LBS_UUID_BUTTON_CHAR);
				ledCharacteristic = service.getCharacteristic(LBS_UUID_LED_CHAR);
			}

			boolean writeRequest = false;
			if (ledCharacteristic != null) {
				final int rxProperties = ledCharacteristic.getProperties();
				writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
			}

			supported = buttonCharacteristic != null && ledCharacteristic != null && writeRequest;
			return supported;
		}

		@Override
		protected void onDeviceDisconnected() {
			buttonCharacteristic = null;
			ledCharacteristic = null;
		}
	}

	/**
	 * Sends a request to the device to turn the LED on or off.
	 *
	 * @param on to turn the LED on, false to turn it off.
	 */
	public void turnLed(final boolean on) {
		// Are we connected?
		if (ledCharacteristic == null)
			return;

		if (this.on == on){
			writeCharacteristic(ledCharacteristic,Data.opCode(Constants.MODE_STOP));
			Toast.makeText(getContext(),"이미 통신 중이어서 초기화합니다.",Toast.LENGTH_LONG);
			this.on = false;
			return;
		}
		/*
		writeCharacteristic(ledCharacteristic,Data.opCode(Constants.MODE_RUN)).with(ledCallback).enqueue();

		writeCharacteristic(ledCharacteristic,Data.opCode(Constants.MODE_VERSION)).with(ledCallback).enqueue();

		 */
		writeCharacteristic(ledCharacteristic,Data.opCode((byte)(0X33))).with(ledCallback).enqueue();
		//writeCharacteristic(ledCharacteristic,Data.opCode(Constants.MODE_VERSION)).with(ledCallback).enqueue();
		//반복적으로 10ms마다
		int ctr = 0;
		/*
		Log.i("BlinkyManager","measure횟수 : "+Cons.MAX_FRAMES_NUM);
		String log = "";
		long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
		Log.i(TAG,"시작 : "+beforeTime);
		while(ctr < Cons.MAX_FRAMES_NUM ctr<50){
			writeCharacteristic(ledCharacteristic,Data.opCode((byte)(0X33))).with(ledCallback).enqueue();

			//writeCharacteristic(ledCharacteristic,Data.opCode(Constants.MODE_MEASURE_RIGHT)).with(ledCallback).enqueue();

			try {
				Thread.sleep(Cons.MEASURE_INTERVAL_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			ctr++;
		}
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long msecDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
		System.out.println("시간차이(m) : "+msecDiffTime);
		writeCharacteristic(ledCharacteristic,Data.opCode(Constants.MODE_MEASURE_END)).with(ledCallback).enqueue();
		*/
		Log.i("BlinkyManager","measure 자동종료");
	}
}
