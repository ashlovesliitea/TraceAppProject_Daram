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

package no.nordicsemi.android.blinky;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.traceappproject_daram.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.nordicsemi.android.ble.livedata.state.ConnectionState;
import no.nordicsemi.android.blinky.adapter.DiscoveredBluetoothDevice;
import no.nordicsemi.android.blinky.profile.BlinkyManager;
import no.nordicsemi.android.blinky.viewmodels.BlinkyViewModel;

@SuppressWarnings("ConstantConditions")
public class BlinkyActivityNormal {
	public static final String EXTRA_DEVICE = "no.nordicsemi.android.blinky.EXTRA_DEVICE";

	private BlinkyViewModel viewModel;
	private final String TAG ="MJBlinkyActivityNormal";
	@BindView(R.id.led_switch) SwitchMaterial led;
	@BindView(R.id.button_state) TextView buttonState;
	private Application parent;
	DiscoveredBluetoothDevice d1,d2;
	public BlinkyActivityNormal(Application application, DiscoveredBluetoothDevice a, DiscoveredBluetoothDevice b){
		d1 =a;
		d2 =b;
		// Configure the view model.
		parent = application;
		BlinkyViewModel viewModel1 = new BlinkyViewModel(application);
		viewModel1.connect(d1);

		viewModel1.getConnectionState().observe((LifecycleOwner) application, state -> {
			Log.i(TAG,"current state : "+state);
			switch (state.getState()) {
				case CONNECTING:
					Log.i(TAG,"현재상태 : "+R.string.state_connecting+ " , 기기 :"+d1.getAddress());
					break;
				case INITIALIZING:
					Log.i(TAG,"현재상태 : "+R.string.state_initializing+ " , 기기 :"+d1.getAddress());
					break;
				case READY:
					/*
					progressContainer.setVisibility(View.GONE);
					content.setVisibility(View.VISIBLE);
					onConnectionStateChanged(true);
					final Intent controlBlinkIntent = new Intent(this, ScannerActivity.class);
					//controlBlinkIntent.putExtra(BlinkyActivity.EXTRA_DEVICE, device);
					startActivity(controlBlinkIntent);

					 */
					break;
				case DISCONNECTED:
					/*
					if (state instanceof ConnectionState.Disconnected) {
						final ConnectionState.Disconnected stateWithReason = (ConnectionState.Disconnected) state;
						if (stateWithReason.isNotSupported()) {
							progressContainer.setVisibility(View.GONE);
							notSupported.setVisibility(View.VISIBLE);
						}
					}

					 */
					// fallthrough
				case DISCONNECTING:
					//onConnectionStateChanged(false);
					break;
			}

		});
		/*
		viewModel.getLedState().observe(this, isOn -> {
			ledState.setText(isOn ? R.string.turn_on : R.string.turn_off);
			led.setChecked(isOn);
		});
		viewModel.getButtonState().observe(this,
				pressed -> buttonState.setText(pressed ?
						R.string.button_pressed : R.string.button_released));

		 */
	}

	/*

	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blinky);
		ButterKnife.bind(this);

		final Intent intent = getIntent();
		final DiscoveredBluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);
		final String deviceName = device.getName();
		final String deviceAddress = device.getAddress();

		final MaterialToolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(deviceName != null ? deviceName : getString(R.string.unknown_device));
		toolbar.setSubtitle(deviceAddress);
		//setSupportActionBar(toolbar);//errorneous 그래서 뺌
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Configure the view model.
		viewModel = new ViewModelProvider(this).get(BlinkyViewModel.class);
		viewModel.connect(device);

		// Set up views.
		final TextView ledState = findViewById(R.id.led_state);
		final LinearLayout progressContainer = findViewById(R.id.progress_container);
		final TextView connectionState = findViewById(R.id.connection_state);
		final View content = findViewById(R.id.device_container);
		final View notSupported = findViewById(R.id.not_supported);

		led.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setLedState(isChecked));
		viewModel.getConnectionState().observe(this, state -> {
			Log.i(TAG,"current state : "+state);

			switch (state.getState()) {
				case CONNECTING:
					progressContainer.setVisibility(View.VISIBLE);
					notSupported.setVisibility(View.GONE);
					connectionState.setText(R.string.state_connecting);
					break;
				case INITIALIZING:
					connectionState.setText(R.string.state_initializing);
					break;
				case READY:

					progressContainer.setVisibility(View.GONE);
					content.setVisibility(View.VISIBLE);
					onConnectionStateChanged(true);


					final Intent controlBlinkIntent = new Intent(this, ScannerActivity.class);
					//controlBlinkIntent.putExtra(BlinkyActivity.EXTRA_DEVICE, device);
					startActivity(controlBlinkIntent);
					break;
				case DISCONNECTED:
					if (state instanceof ConnectionState.Disconnected) {
						final ConnectionState.Disconnected stateWithReason = (ConnectionState.Disconnected) state;
						if (stateWithReason.isNotSupported()) {
							progressContainer.setVisibility(View.GONE);
							notSupported.setVisibility(View.VISIBLE);
						}
					}
					// fallthrough
				case DISCONNECTING:
					onConnectionStateChanged(false);
					break;
			}

		});
		viewModel.getLedState().observe(this, isOn -> {
			ledState.setText(isOn ? R.string.turn_on : R.string.turn_off);
			led.setChecked(isOn);
		});
		viewModel.getButtonState().observe(this,
				pressed -> buttonState.setText(pressed ?
						R.string.button_pressed : R.string.button_released));
	}

	@OnClick(R.id.action_clear_cache)
	public void onTryAgainClicked() {
		viewModel.reconnect();
	}

	private void onConnectionStateChanged(final boolean connected) {
		led.setEnabled(connected);
		if (!connected) {
			led.setChecked(false);
			buttonState.setText(R.string.button_unknown);
		}
	}

	 */
}
