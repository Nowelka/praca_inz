package pl.monika.inzandroid;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

//import com.google.android.gms.location.LocationListener;

public class GpsTrack implements LocationListener {
	private Context context;
	private Location location;
	private LocationManager locationManager;

	private static final float minDistance = 10; // meters
	private static final long minTime = 1000;// miliseconds
	private boolean gpsStatus = false;
	private boolean gpsy;

	private double latitude;
	private double longitude;

	public GpsTrack(Context context) {
		this.context = context;
		getLocation();
	}

	public Location getLocation() {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// sprawdza czy jest wlaczony
		isGpsEnable();
		gpsy = locationManager
				.isProviderEnabled(LocationManager.KEY_PROVIDER_ENABLED);
		System.out.println(gpsStatus + " <- stat; gps ->" + gpsy);
		// smieszne oszustwo musi byc string string i zeby zrobic string to " "
		// + ...

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				minTime, minDistance, (android.location.LocationListener) this);
		// ^tu sie cos przed this pojawilo
		if (locationManager != null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				longitude = location.getLongitude();
				latitude = location.getLatitude();
			}
		}
		return location;

	}

	public boolean isGpsEnable() {
		gpsStatus = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return gpsStatus;

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(boolean gpsStatus) {
		this.gpsStatus = gpsStatus;
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
