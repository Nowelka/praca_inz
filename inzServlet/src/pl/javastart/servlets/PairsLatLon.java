package pl.javastart.servlets;

public class PairsLatLon {

	private final double lat;
	private final double lon;

	public PairsLatLon(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	public String toString() {
		return (lat + " " + lon);
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
}
