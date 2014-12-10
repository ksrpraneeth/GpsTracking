package com.ytkoff.rdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ytkoff.CriticalPoints.CriticalPointsHandler;

public class MainActivity extends FragmentActivity {
	private GoogleMap googleMap;
	ArrayList<LatLng> nagoltodilsukhnagar = new ArrayList<LatLng>();
	ArrayList<LatLng> nagoltodilsuCriticalPoints = new ArrayList<LatLng>();
	CriticalPointsHandler handler;
	int criticalPointsCount = 0;
	PolylineOptions polylineOptions;
	TextView totalnoofmarker;
	double totaldistance = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new CriticalPointsHandler();
		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		totalnoofmarker = (TextView) findViewById(R.id.totalnoofmarkers);

		File sdcard = Environment.getExternalStorageDirectory();
		try {
			// Get the text file
			/*
			 * InputStream is = this.getResources().openRawResource(
			 * R.drawable.latlng); BufferedReader in = new BufferedReader(new
			 * InputStreamReader(is));
			 */

			File file = new File(sdcard, "latlng23.txt");
			BufferedReader in = new BufferedReader(new FileReader(file));

			String str;
			str = in.readLine();

			while ((str = in.readLine()) != null) {
				if (!str.isEmpty() && !str.matches("[^A-Za-z0-9]")) {
					String[] ar = str.split(",");

					double latitudes = Double.parseDouble(ar[0]);
					double longitudes = Double.parseDouble(ar[1]);
					nagoltodilsukhnagar.add(new LatLng(latitudes, longitudes));

				}

			}

			in.close();
		} catch (IOException e) {
			System.out.println("File Read Error");
		}

		System.out.println("Points to be printed : "
				+ nagoltodilsukhnagar.size());

	}

	public void PrintAll(View v) {
		totaldistance = 0;
		googleMap.clear();
		for (int i = 0; i < nagoltodilsukhnagar.size() - 1; i++) {
			googleMap.addMarker(new MarkerOptions().position(
					nagoltodilsukhnagar.get(i)).title(
					nagoltodilsukhnagar.get(i).toString()));
			totaldistance += distFrom(nagoltodilsukhnagar.get(i).latitude,
					nagoltodilsukhnagar.get(i).longitude,
					nagoltodilsukhnagar.get(i + 1).latitude,
					nagoltodilsukhnagar.get(i + 1).longitude);
		}
		polylineOptions = new PolylineOptions();
		polylineOptions.color(Color.RED);
		polylineOptions.width(5);

		polylineOptions.addAll(nagoltodilsukhnagar);
		googleMap.addPolyline(polylineOptions);
		CameraUpdate center = CameraUpdateFactory.newLatLng(nagoltodilsukhnagar
				.get(0));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
		googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);
		totalnoofmarker.setText("No of Markers :" + nagoltodilsukhnagar.size()
				+ "\n total Distance :" + totaldistance);

	}

	public void PrintCritical(View v) {
		totaldistance = 0;
		googleMap.clear();
		criticalPointsCount = 0;
		for (int i = 0; i < nagoltodilsukhnagar.size() - 2; i++) {

			if (i == 0) {
				googleMap.addMarker(new MarkerOptions().position(
						nagoltodilsukhnagar.get(i)).title(
						nagoltodilsukhnagar.get(i).toString()));

				nagoltodilsuCriticalPoints.add(nagoltodilsukhnagar.get(i));
				criticalPointsCount++;
			} else if (i == nagoltodilsukhnagar.size()) {
				googleMap.addMarker(new MarkerOptions().position(
						nagoltodilsukhnagar.get(i + 2)).title(
						nagoltodilsukhnagar.get(i + 2).toString()));

				nagoltodilsuCriticalPoints.add(nagoltodilsukhnagar.get(i + 2));
				criticalPointsCount++;
			} else {
				Double bearing1 = CriticalPointsHandler.bearing(
						nagoltodilsukhnagar.get(i).latitude,
						nagoltodilsukhnagar.get(i).longitude,
						nagoltodilsukhnagar.get(i + 1).latitude,
						nagoltodilsukhnagar.get(i + 1).longitude);
				Double bearing2 = CriticalPointsHandler.bearing(
						nagoltodilsukhnagar.get(i + 1).latitude,
						nagoltodilsukhnagar.get(i + 1).longitude,
						nagoltodilsukhnagar.get(i + 2).latitude,
						nagoltodilsukhnagar.get(i + 2).longitude);

				if (bearing1 > bearing2 ? (bearing1 - bearing2 > CriticalPointsHandler.BEARING_THRESHOLD_DEGREE)
						: (bearing2 - bearing1 > CriticalPointsHandler.BEARING_THRESHOLD_DEGREE)) {

					googleMap.addMarker(new MarkerOptions().position(
							nagoltodilsukhnagar.get(i + 2)).title(
							nagoltodilsukhnagar.get(i + 2).toString()));

					nagoltodilsuCriticalPoints.add(nagoltodilsukhnagar
							.get(i + 2));
					criticalPointsCount++;
				}
			}

		}

		polylineOptions = new PolylineOptions();
		polylineOptions.color(Color.RED);
		polylineOptions.width(5);

		polylineOptions.addAll(nagoltodilsuCriticalPoints);
		googleMap.addPolyline(polylineOptions);
		for (int k = 0; k < nagoltodilsuCriticalPoints.size() - 1; k++) {

			totaldistance += distFrom(
					nagoltodilsuCriticalPoints.get(k).latitude,
					nagoltodilsuCriticalPoints.get(k).longitude,
					nagoltodilsuCriticalPoints.get(k + 1).latitude,
					nagoltodilsuCriticalPoints.get(k + 1).longitude);
		}
		totalnoofmarker.setText("No of Markers :" + criticalPointsCount
				+ "\n total Distance :" + totaldistance);
		System.out.println("Critical Points:" + criticalPointsCount);
		CameraUpdate center = CameraUpdateFactory.newLatLng(nagoltodilsukhnagar
				.get(0));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
		googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);
	}

	public static float distFrom(double latitude, double longitude,
			double latitude2, double longitude2) {
		double earthRadius = 6371; // kilometers
		double dLat = Math.toRadians(latitude2 - latitude);
		double dLng = Math.toRadians(longitude2 - longitude);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(latitude))
				* Math.cos(Math.toRadians(latitude2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}
}
