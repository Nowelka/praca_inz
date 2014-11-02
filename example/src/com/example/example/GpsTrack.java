package com.example.example;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GpsTrack implements LocationListener {
	private Context context;

	public Location Location;
	private LocationListener myLocation;
	private LocationManager myLocationMngr;

	private static final float minDist = 30; // 300 m
	private static final long minTime = 3; // 3min
	boolean GpsStatus = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Location getLocation() {

		myLocationMngr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// sprawdza czy jest wlaczony
		GpsStatus = myLocationMngr
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		Log.v("status gps", " " + GpsStatus);

		myLocationMngr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				minTime, minDist, this);
		return Location;

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
