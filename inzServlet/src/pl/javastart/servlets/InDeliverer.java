package pl.javastart.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class InDeliverer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PairsLatLon> l;

	public InDeliverer() {
		// wroclaw buforowa 104 -> olesnica lwowska 6
		l = new ArrayList<PairsLatLon>();
		l.add(new PairsLatLon(51.054774, 17.057963));
		l.add(new PairsLatLon(51.095516, 17.042405));
		l.add(new PairsLatLon(51.087070, 17.027222));
		l.add(new PairsLatLon(51.063558, 17.001473));
		l.add(new PairsLatLon(51.056976, 16.983792));
		l.add(new PairsLatLon(51.049962, 16.969029));
		l.add(new PairsLatLon(51.048991, 16.951520));
		l.add(new PairsLatLon(51.051689, 16.933152));
		l.add(new PairsLatLon(51.085668, 16.929890));
		l.add(new PairsLatLon(51.124424, 16.932147));
		l.add(new PairsLatLon(51.150274, 16.949313));
		l.add(new PairsLatLon(51.166639, 16.982616));
		l.add(new PairsLatLon(51.168146, 17.000812));
		l.add(new PairsLatLon(51.167607, 17.006820));
		l.add(new PairsLatLon(51.167500, 17.034114));
		l.add(new PairsLatLon(51.172128, 17.079776));
		l.add(new PairsLatLon(51.177617, 17.123378));
		l.add(new PairsLatLon(51.176003, 17.131618));
		l.add(new PairsLatLon(51.172451, 17.167495));
		l.add(new PairsLatLon(51.159318, 17.147926));
		l.add(new PairsLatLon(51.156412, 17.148097));
		l.add(new PairsLatLon(51.156412, 17.148097));
		l.add(new PairsLatLon(51.156412, 17.148097));
		l.add(new PairsLatLon(51.170083, 17.168010));
		l.add(new PairsLatLon(51.181706, 17.204402));
		l.add(new PairsLatLon(51.192465, 17.249206));
		l.add(new PairsLatLon(51.193756, 17.277015));
		l.add(new PairsLatLon(51.198167, 17.302593));
		l.add(new PairsLatLon(51.207417, 17.338470));
		l.add(new PairsLatLon(51.216127, 17.364219));
		l.add(new PairsLatLon(51.214622, 17.376064));
		l.add(new PairsLatLon(51.212882, 17.384238));
		l.add(new PairsLatLon(51.212505, 17.390675));
		l.add(new PairsLatLon(51.210892, 17.393593));
		l.add(new PairsLatLon(51.210193, 17.393851));
		l.add(new PairsLatLon(51.210023, 17.391636));
		l.add(new PairsLatLon(51.209660, 17.390392));
		l.add(new PairsLatLon(51.209243, 17.389211));
		l.add(new PairsLatLon(51.208954, 17.385885));
		l.add(new PairsLatLon(51.209124, 17.384437));
		l.add(new PairsLatLon(51.209124, 17.384437));

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		for (PairsLatLon pll : l) {
			out.write(pll.toString() + "\n");
		}
		double lat = 15.15, lon = 51.51;
		for (int i = 0; i < 10; i++) {
			System.out.println(i + "\n\n");
			Random rand = new Random();
			connect(130, lat, lon);
			lat += rand.nextDouble();
			lon += rand.nextDouble();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	HttpPost httpPost;
	HttpClient httpClient;

	public void connect(int id, double lat, double lon) {
		String localhost = "192.168.1.2:8080";
		String url = "http://" + localhost + "/inzServlet/insert";
		try {
			httpPost = new HttpPost(url);
			httpClient = HttpClientBuilder.create().build();
			ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("ID", id + ""));
			pairs.add(new BasicNameValuePair("longitude", lon + ""));
			pairs.add(new BasicNameValuePair("latitude", lat + ""));
			pairs.add(new BasicNameValuePair("timestamp", "2014-11-25 15:12:32"));
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					httpClient.execute(httpPost);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
