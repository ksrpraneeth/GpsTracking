package com.ytkoff.rdp;

import java.util.ArrayList;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

public class StartScreen extends Activity {
	boolean isServiceStarted = false;
	Button StartService;
	Button plotonmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ytkoff.rdp.R.layout.activity_start_screen);
		StartService = (Button) findViewById(com.ytkoff.rdp.R.id.btnStartRequest);
		plotonmap = (Button) findViewById(com.ytkoff.rdp.R.id.plotonmap);
		plotonmap.setVisibility(View.GONE);
	}

	public void buttonClicked(View v) {

		if (v.getId() == com.ytkoff.rdp.R.id.btnStartRequest) {
			// use this to start and trigger a service
			if (!isServiceStarted) {
				Intent i = new Intent(this, LocationService.class);
				StartService.setText("Stop");
				isServiceStarted = true;
				plotonmap.setVisibility(View.GONE);
				this.startService(i);

			} else {
				isServiceStarted = false;
				Intent i = new Intent(this, LocationService.class);
				StartService.setText("Start");
				plotonmap.setVisibility(View.VISIBLE);
				this.stopService(i);
			}

		} else if (v.getId() == com.ytkoff.rdp.R.id.plotonmap) {
			Intent plot = new Intent(this, MainActivity.class);
			startActivity(plot);
		}

	}
}
