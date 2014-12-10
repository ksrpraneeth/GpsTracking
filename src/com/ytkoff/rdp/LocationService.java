package com.ytkoff.rdp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {
	LocationManager locationManager;
	LocationListener locationListener;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO do something useful

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		System.out.println("Service started");
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				System.out.println(location.getLatitude() + ","
						+ location.getLongitude());

				try {
					writeTofile(location.getLatitude() + ","
							+ location.getLongitude() + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}
		};

		// Register the listener with the Location Manager to receive location
		// updates

		// All your normal criteria setup
		Criteria criteria = new Criteria();
		// Use FINE or COARSE (or NO_REQUIREMENT) here
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(true);
		criteria.setSpeedRequired(true);
		criteria.setCostAllowed(true);
		criteria.setBearingRequired(true);

		// API level 9 and up
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
		criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);

		// Provide your criteria and flag enabledOnly that tells
		// LocationManager only to return active providers.
		locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO for communication return IBinder implementation
		return null;
	}

	public void writeTofile(String data) throws IOException {

		File myFile = new File("/sdcard/latlng23.txt");
		if (myFile.exists()) {
			try {
				FileOutputStream fOut = new FileOutputStream(myFile, true);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.append(data + "\n");
				myOutWriter.close();
				fOut.close();

			} catch (Exception e) {

			}
		} else {
			try {
				myFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
		System.out.println("Service Destroyed");
	}
}
