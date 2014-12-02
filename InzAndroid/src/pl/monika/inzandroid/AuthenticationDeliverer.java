package pl.monika.inzandroid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class AuthenticationDeliverer extends AsyncTask<String, Void, String> {

	private HttpClient httpClient = new DefaultHttpClient();
	private String localhost = "192.168.1.2:8080";
	private HttpPost httpPostAuthentication = new HttpPost("http://"
			+ localhost + "/inzServlet/authentication");
	private HttpGet httpGetAuthentication = new HttpGet("http://" + localhost
			+ "/inzServlet/authentication");
	private HttpResponse httpResponse;

	@Override
	protected String doInBackground(String... params) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("ID", params[0]));
		pairs.add(new BasicNameValuePair("activ", params[1]));
		pairs.add(new BasicNameValuePair("logout", params[2]));
		try {
			httpPostAuthentication.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					httpClient.execute(httpPostAuthentication);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		try {
			httpResponse = (new DefaultHttpClient())
					.execute(httpGetAuthentication);
			String entityStr = EntityUtils.toString(httpResponse.getEntity());
			if (entityStr.contains("logIn"))
				return "logIn";
			else if (entityStr.contains("busy"))
				return "busy";
			else if (entityStr.contains("false"))
				return "false";
			else if (entityStr.contains("logOut"))
				return "logOut";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
