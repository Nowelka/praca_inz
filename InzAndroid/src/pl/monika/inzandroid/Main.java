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
	private Context context;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder builder;
	private Intent intent;
	private PendingIntent pIntent;
	private Notification notif;

	GpsTrack gps;
	private String savedId = "";

	public String getSavedId() {
		return savedId;
	}

	public void setSavedId(String savedId) {
		this.savedId = savedId;
	}

	private String localhost = "192.168.1.2:8080";
	private HttpClient httpClient = new DefaultHttpClient();
	private HttpPost httpPost = new HttpPost("http://" + localhost
			+ "/inzServlet/insert");

	private int pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pid = android.os.Process.myPid();

		setContentView(R.layout.activity_main);
		gps = new GpsTrack(this);

		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (savedId != "")
					try {
						String tmp = new AuthenticationDeliverer().execute(
								savedId, "0", "1").get();
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
			public void afterTextChanged(Editable s) {
				if (s == null || s.length() < 4) {
					editText.setEnabled(true);
				} else {
					editText.setEnabled(false);
					savedId = editText.getText().toString();

					String b = "";
					try {
						b = new AuthenticationDeliverer().execute(savedId, "1",
								"0").get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					if (b == "logIn") {
						Toast.makeText(getBaseContext(),
								"wprowadzono prawidlowe ID " + savedId,
								Toast.LENGTH_SHORT).show();
						handler.postDelayed(statusChecker, refreshTimeStamp);

						notif = builder.setContentText(
								"zalogowany jako: " + savedId).build();
						mNotificationManager.notify(pid, notif);
					} else if (b == "busy") {
						editText.setEnabled(true);
						editText.setText("");
						s = null;
						Toast.makeText(
								getBaseContext(),
								"Kurier o ID " + savedId
										+ " jest już zalogowany",
								Toast.LENGTH_SHORT).show();
						savedId = "";
					} else if (b == "false") {
						editText.setEnabled(true);
						editText.setText("");
						s = null;
						Toast.makeText(getBaseContext(),
								"Nie ma kuriera o ID " + savedId,
								Toast.LENGTH_SHORT).show();
						savedId = "";
					}

				}

			}
		});

		context = getApplicationContext();
		builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Aplikacja kurierska")
				.setContentText("nie zalogowano sie do systemu");

		intent = new Intent(context, Main.class);
		pIntent = PendingIntent.getActivity(context, pid, intent,
				Notification.FLAG_AUTO_CANCEL);
		builder.setContentIntent(pIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notif = builder.build();
		mNotificationManager.notify(pid, notif);

	}

	Runnable statusChecker = new Runnable() {

		@Override
		public void run() {
			startSendGpsDate();
			handler.postDelayed(statusChecker, refreshTimeStamp);
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
		if (gps.isGpsEnable()) {
			gps.getLocation();
			double lat = gps.getLatitude();
			double lon = gps.getLongitude();
			if (lat != 0.0 && lon != 0.0) {
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				textView.setText(lon + "\n" + lat + "\n" + date);
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
							/* HttpResponse httpResponse = */httpClient
									.execute(httpPost);
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
		mNotificationManager.cancelAll();
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
}
