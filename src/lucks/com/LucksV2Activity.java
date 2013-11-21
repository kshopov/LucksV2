package lucks.com;

import lucks.com.data.DataConstants;
import lucks.com.model.Luck;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings({ "deprecation"})
public class LucksV2Activity extends Activity implements SensorListener, OnGestureListener, OnDoubleTapListener {

	private SensorManager sensorManager = null;
	private GestureDetector gestureDetector;

	private TextView luckTextView = null;
	
	private static final float threshold = 0.2f;
	private static final int interval = 1000;

	private long now = 0;	
	private long timeDiff = 0;
	private long lastUpdate = 0;
	private long lastShake = 0;

	private float x = 0;
	private float y = 0;
	private float z = 0;
	private float lastX = 0;
	private float lastY = 0;
	private float lastZ = 0;
	private float force = 0;

	private Luck luck;
	private LucksApp app;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		app = (LucksApp) getApplication();
		context = new ContextWrapper(getBaseContext());
		gestureDetector = new GestureDetector(this);
		
		luck = new Luck();

		if (sensorManager == null) {
			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		}
		
		final Button selectLanguageButton = (Button) findViewById(R.id.select_Lan_button);
		selectLanguageButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LucksV2Activity.this,
						SelectLanguageActivity.class);
				startActivity(intent);
			}
		});

		final Button addLuckButton = (Button) findViewById(R.id.add_btn);
		addLuckButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LucksV2Activity.this, AddLucksActivity.class);
				startActivity(intent);
			}
		});

		final Button editLuckButton = (Button) findViewById(R.id.edit_btn);
		editLuckButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(luck.getLuckText().equals("")){
					Toast.makeText(getBaseContext(), "There is not luck for editting", Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = new Intent(LucksV2Activity.this, EditLucksActivity.class);
				
				intent.putExtra("luck", luck.getLuckText());
				intent.putExtra("author", luck.getLuckAuthor());
				intent.putExtra("luckId", luck.getId());
				intent.putExtra("language", luck.getluckLanguageId());
				
				startActivity(intent);
			}
		});

		final Button updateLucksButton = (Button) findViewById(R.id.upd_btn);
		updateLucksButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				context.deleteDatabase(DataConstants.DATABASE_NAME);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1, Menu.NONE, "Information");
		menu.add(Menu.NONE, 2, Menu.NONE, "Contact");
		menu.add(Menu.NONE, 3, Menu.NONE, "Share");

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 3 : 
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Luck");
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, luck.getLuckText());
				startActivity(Intent.createChooser(shareIntent, "Share via"));
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void displayLuck(){
		if(app.getDataManager().getRandomLuck().getId() != 0){
			luck = app.getDataManager().getRandomLuck();
		}else{
			Toast.makeText(getBaseContext(),
					"You should select prefered language first!",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(LucksV2Activity.this,
					SelectLanguageActivity.class);
			startActivity(intent);
		}
		luckTextView = (TextView) findViewById(R.id.text_luck);
		luckTextView.setText(luck.getLuckText() + "\n\n\n" + luck.getLuckAuthor());
	}

	public void onSensorChanged(int sensor, float[] values) {
		switch (sensor) {
		case SensorManager.SENSOR_ACCELEROMETER:
			now = System.currentTimeMillis();

			x = values[0];
			y = values[1];
			z = values[2];

			// if not interesting in shake events
			// just remove the whole if then else bloc
			if (lastUpdate == 0) {
				lastUpdate = now;
				lastShake = now;
				lastX = x;
				lastY = y;
				lastZ = z;
			} else {
				timeDiff = now - lastUpdate;
				if (timeDiff > 0) {
					force = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDiff;
					if (force > threshold) {
						if (now - lastShake >= interval) {
							// trigger shake event
							displayLuck();
						}
						lastShake = now;
					}
					lastX = x;
					lastY = y;
					lastZ = z;
					lastUpdate = now;
				}
			}
			break;
		}
	}
	
	
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e) 
	{
		displayLuck();
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) 
	{
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return gestureDetector.onTouchEvent(event);//return the double tap events
	}
}