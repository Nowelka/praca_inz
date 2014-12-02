package pl.monika.inzandroid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	private final int refreshTimeStamp = 1000;
	private EditText editText;
	private TextView textView;
	private Button button;
	private Handler handler;
	private Handler handlerWifi;
	private Context context;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationCompatBuilder;
	private Intent intent;
	private PendingIntent pendingIntent;
	private Notification notification;
	private GpsTrack gpsTrack;
	private String savedId = "";
	private String localhost = "192.168.1.2:8080";
	private HttpClient httpClient = new DefaultHttpClient();
	private HttpPost httpPost = new HttpPost("http://" + localhost
			+ "/inzServlet/insert");
	private CheckConnection checkConnection;
	private int pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pid = android.os.Process.myPid();

		setContentView(R.layout.activity_main);
		gpsTrack = new GpsTrack(this);

		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (savedId != "")
					try {
						new AuthenticationDeliverer()
								.execute(savedId, "0", "0").get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				savedId = "";
				onDestroy();
			}
		});

		editText = (EditText) findViewById(R.id.editText1);
		textView = (TextView) findViewById(R.id.textView1);
		handler = new Handler();
		context = getApplicationContext();

		checkConnection = new CheckConnection(context);
		handlerWifi = new Handler();
		handlerWifi.postDelayed(connectionChecker, 100);

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable enteredText) {
				if (enteredText == null || enteredText.length() < 4) {
					editText.setEnabled(true);
				} else {
					editText.setEnabled(false);
					handlerWifi.removeCallbacks(connectionChecker);
					savedId = editText.getText().toString();

					String response = "";
					try {
						response = new AuthenticationDeliverer().execute(
								savedId, "1", "1").get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					if (response == "logIn") {
						Toast.makeText(getBaseContext(),
								"wprowadzono prawidlowe ID " + savedId,
								Toast.LENGTH_SHORT).show();
						handler.postDelayed(statusChecker, refreshTimeStamp);

						notification = notificationCompatBuilder
								.setContentText("zalogowany jako: " + savedId)
								.build();
						notificationManager.notify(pid, notification);
					} else if (response == "busy") {
						editText.setEnabled(true);
						editText.setText("");
						enteredText = null;
						Toast.makeText(
								getBaseContext(),
								"Kurier o ID " + savedId
										+ " jest już zalogowany",
								Toast.LENGTH_SHORT).show();
						savedId = "";
					} else if (response == "false") {
						editText.setEnabled(true);
						editText.setText("");
						enteredText = null;
						Toast.makeText(getBaseContext(),
								"Nie ma kuriera o ID " + savedId,
								Toast.LENGTH_SHORT).show();
						savedId = "";
					}

				}

			}
		});

		// ikona i zadanie w pasku TaskBar
		notificationCompatBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Aplikacja kurierska")
				.setContentText("nie zalogowano sie do systemu");

		intent = new Intent(context, Main.class);
		pendingIntent = PendingIntent.getActivity(context, pid, intent,
				Notification.FLAG_AUTO_CANCEL);
		notificationCompatBuilder.setContentIntent(pendingIntent);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notification = notificationCompatBuilder.build();
		notificationManager.notify(pid, notification);

	}

	Runnable statusChecker = new Runnable() {

		@Override
		public void run() {
			startSendGpsDate();
			handler.postDelayed(statusChecker, refreshTimeStamp);
		}
	};

	Runnable connectionChecker = new Runnable() {

		@SuppressLint("ShowToast")
		@Override
		public void run() {
			if (checkConnection.isConnection() == true) {
				editText.setEnabled(true);
				Toast.makeText(context, "Włączono dostęp do sieci",
						Toast.LENGTH_SHORT).cancel();
				onStop();
			} else {
				Toast.makeText(getBaseContext(), "Włącz dostęp do sieci",
						Toast.LENGTH_SHORT).show();
				editText.setEnabled(false);
			}
			handlerWifi.postDelayed(connectionChecker, refreshTimeStamp);
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	private void startSendGpsDate() {
		if (gpsTrack.isGpsEnable()) {
			gpsTrack.getLocation();
			double lat = gpsTrack.getLatitude();
			double lon = gpsTrack.getLongitude();
			if (lat != 0.0 && lon != 0.0) {
				Date date = new Date(System.currentTimeMillis());
				textView.setText(lon + "\n" + lat + "\n" + date);
				ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("ID", savedId));
				pairs.add(new BasicNameValuePair("longitude", lon + ""));
				pairs.add(new BasicNameValuePair("latitude", lat + ""));
				pairs.add(new BasicNameValuePair("timestamp",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(date)));
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
							httpClient.execute(httpPost);
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} else
				textView.setText("Czekam na zlokalizowanie");
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
		notificationManager.cancelAll();
		android.os.Process.killProcess(pid);
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

	public class CheckConnection {
		private Context context;

		public CheckConnection(Context context) {
			this.context = context;
		}

		public boolean isConnection() {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] network = connectivity.getAllNetworkInfo();
				for (int i = 0; i < network.length; i++) {
					if (network[i].getState() == NetworkInfo.State.CONNECTED)
						return true;
				}
			}
			return false;
		}
	}

	public String getSavedId() {
		return savedId;
	}

	public void setSavedId(String savedId) {
		this.savedId = savedId;
	}
}
