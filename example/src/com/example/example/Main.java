package com.example.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private Button myBtn;
	private EditText editText;
	private TextView txtView;

	// private GpsTrack myGPS;
	// private LocationListener myLocation;
	// private LocationManager myLocationMngr;

	// private Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myBtn = (Button) findViewById(R.id.btn1);
		myBtn.setOnClickListener(new BtnClicker());
		editText = (EditText) findViewById(R.id.editText1);
		txtView = (TextView) findViewById(R.id.textView1);

		// myLocation = new GpsTrack();
		// myLocationMngr = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// myLocationMngr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 35000, 10, (LocationListener) this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	class BtnClicker implements Button.OnClickListener {

		@Override
		public void onClick(View v) {

			if (v == myBtn) {
				txtView.setText(editText.getText().toString());

				Toast.makeText(v.getContext(), editText.getText().toString(),
						Toast.LENGTH_SHORT).show();

				// location = myLocationMngr
				// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
				// Toast.makeText(v.getContext(), (int) location.getLatitude(),
				// Toast.LENGTH_LONG).show();
				// txtView.setText("A " + location.getLatitude());

			}

		}

	}
}
