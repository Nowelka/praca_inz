package pl.monika.inzandroid;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsTrack implements LocationListener {
	private Context context;
	private Location location;
	private LocationManager locationManager;

	private static final float minDistance = 10; // meters
	private static final long minTime = 1000;// miliseconds
	private boolean gpsEnable = false;
	private boolean gpsLocated = false;
	private double latitude = 0.0;
	private double longitude = 0.0;

	public GpsTrack(Context context) {
		this.context = context;
		getLocation();
	}

	public Location getLocation() {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				minTime, minDistance, (android.location.LocationListener) this);

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

	// sprawdza czy wlaczony jest gps
	public boolean isGpsEnable() {
		gpsEnable = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return gpsEnable;

	}

	// sprawdza czy ustalona lokalizacja
	public boolean isGpsLocated() {
		gpsLocated = locationManager
				.isProviderEnabled(LocationManager.KEY_PROVIDER_ENABLED);
		return gpsLocated;
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

	public void setGpsStatus(boolean gpsStatus) {
		this.gpsEnable = gpsStatus;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
