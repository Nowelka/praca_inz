//color #190544
package pl.monika.inzandroid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
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

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private EditText editText;
	private TextView textView;
	private Button button;

	private Handler handler;
	private Context context;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder builder;
	private Intent intent;
	private PendingIntent pIntent;
	private Notification notif;

	GpsTrack gps;
	public String savedId = "";
	public int tmp;

	public String getSavedId() {
		return savedId;
	}

	public void setSavedId(String savedId) {
		this.savedId = savedId;
	}

	private String localhost = "192.168.1.2:8080";
	private HttpClient httpClient = new DefaultHttpClient();
	private HttpPost httpPost = new HttpPost("http://" + localhost
			+ "/inzServlet/insert");// //Tu nazwe
	private HttpPost httpPostAuthentication = new HttpPost("http://"
			+ localhost + "/inzServlet/authentication");
	private HttpGet httpGetAuthentication = new HttpGet("http://" + localhost
			+ "/inzServlet/authentication");// //Tu nazwe

	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = android.os.Process.myPid();

		setContentView(R.layout.activity_main);
		gps = new GpsTrack(this);
		tmp = 0;
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onDestroy();
			}
		});
		editText = (EditText) findViewById(R.id.editText1);
		textView = (TextView) findViewById(R.id.textView1);
		handler = new Handler();
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s == null || s.length() < 4) {
					editText.setEnabled(true);
				} else {
					editText.setEnabled(false);
					savedId = editText.getText().toString();

					// ///////////////////////////////

					ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("ID", savedId));
					try {
						httpPostAuthentication
								.setEntity(new UrlEncodedFormEntity(pairs));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					// new Thread(new Runnable() {

					// @Override
					// public void run() {
					try {
						final HttpClient httpClientA = new DefaultHttpClient();
						final HttpResponse httpResponseA = httpClientA
								.execute(httpGetAuthentication);
						final HttpEntity entityA = httpResponseA.getEntity();
						textView.setText(EntityUtils.toString(entityA)
								+ "hhhhhhhhhhh");
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						System.out.println(e);
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println(e);
					}
					// }
					// }).start();
					// /////////////////////////////////////////////////////
					Toast.makeText(getBaseContext(), "wprowadzono ID",
							Toast.LENGTH_SHORT).show();
					// handler.postDelayed(statusChecker, 5000);

					notif = builder.setContentText(
							"zalogowany jako: " + savedId).build();
					mNotificationManager.notify(id, notif);
				}

			}
		});

		context = getApplicationContext();
		builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Aplikacja kurierska")
				.setContentText("nie zalogowano sie do systemu");

		intent = new Intent(context, Main.class);
		pIntent = PendingIntent.getActivity(context, id, intent,
				Notification.FLAG_AUTO_CANCEL);
		builder.setContentIntent(pIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notif = builder.build();
		mNotificationManager.notify(id, notif);

	}

	Runnable statusChecker = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			startSendGpsDate();
			handler.postDelayed(statusChecker, 5000);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void startSendGpsDate() {
		// TODO Auto-generated method stub
		if (gps.isGpsEnable()) {
			gps.getLocation();
			double lat = gps.getLatitude();
			double lon = gps.getLongitude();
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			textView.setText(lon + "\n" + lat + "\n" + date + "\n" + tmp++);
			ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("ID", savedId));
			pairs.add(new BasicNameValuePair("longitude", lon + ""));
			pairs.add(new BasicNameValuePair("latitude", lat + ""));
			pairs.add(new BasicNameValuePair("timestamp", df.format(date)));
			pairs.add(new BasicNameValuePair("activ", 1 + ""));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						HttpResponse httpResponse = httpClient
								.execute(httpPost);
						System.out.println(httpResponse.getParams().toString());
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			textView.setText("Wlacz gps");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mNotificationManager.cancelAll();
		android.os.Process.killProcess(id);
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
