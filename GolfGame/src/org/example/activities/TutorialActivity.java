package org.example.activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.example.R;
import org.example.golf.GolfGame;
import org.example.golf.XML.KeyboardReader;
import org.example.golf.XML.XMLKeyboard;
import org.example.tinyEngineClasses.DrawablePanel;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

public class TutorialActivity extends Activity {

	private static String TAG = "TutorialActivity";
	private GolfGame game;
	private boolean mIsScrolling = false;
	private static TTS textToSpeech;

	// Cargamos la conf desde un .xml
	private XMLKeyboard keyboard;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(
				MainActivity.KEY_TTS);
		textToSpeech.setContext(this);

		loadKeyConfiguration();

		DrawablePanel golfView = new GolfGamePanel(this);
		setContentView(golfView);


		game = new GolfGame(2, golfView, textToSpeech, this);
	}

	private void loadKeyConfiguration() {
		KeyboardReader reader = new KeyboardReader();
		FileInputStream fis = null;
		try {
			fis = openFileInput(getString(R.string.app_name) + ".xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		keyboard = reader.loadEditedKeyboard(fis);
	}

	protected void onDestroy() {
		super.onDestroy();
		Music.getInstanceMusic().stop(this, R.raw.storm);
		Music.getInstanceMusic().stop(this, R.raw.sound_shot);
		Music.getInstanceMusic()
				.stop(this, R.raw.previous_shoot_feedback_sound);
		Music.getInstanceMusic().stop(this, R.raw.water_bubbles);
		Music.getInstanceMusic().stop(this, R.raw.clue_feed_back_sound);
		Music.getInstanceMusic().stop(this, R.raw.win_sound);
	}

	/*---------------------------------------------------------------------
	 *  GESTURE DETECTOR
	 ----------------------------------------------------------------------*/
	class MyGestureDetector extends SimpleOnGestureListener {
		private Context c;
		
		public MyGestureDetector(Context context){
			c = context;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.d(TAG, "VX: " + velocityX + "\n VY: " + velocityY);
			/* We should register the event only if up mode is disable */
			if (!SettingsActivity.getOnUp(c)) {
				Input.getInput().addEvent("onFling", e1, e2, velocityX,
						velocityY);
				return true;
			}
			return false;
		}

		// @Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.d(TAG, "Scroll: " + e1.toString() + "\n" + e2.toString());
			mIsScrolling = true;
			Input.getInput().addEvent("onScroll", e1, e2, distanceX, distanceY);
			return true;
		}
	}

	/*---------------------------------------------------------------------
	 *  FIN GESTURE DETECTOR
	 ----------------------------------------------------------------------*/

	/*---------------------------------------------------------------------
	 *  DRAWABLE PANEL
	 ----------------------------------------------------------------------*/

	class GolfGamePanel extends DrawablePanel {
		private GestureDetector mGestureDetector;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (mGestureDetector.onTouchEvent(event)) {
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (mIsScrolling) {
					Log.d(TAG, "onUp: " + event.toString());
					mIsScrolling = false;
					Input.getInput().addEvent("onUp",
							MotionEvent.obtain(event), null, -1, -1);
					return true;
				}
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mIsScrolling = true;
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG, "onDown: " + event.toString());
				Input.getInput().addEvent("onDown", MotionEvent.obtain(event),
						null, -1, -1);
				return true;
			}
			return false;
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			boolean found = manageCustomConfigurationKeys(keyCode, event);
			if (!found) {
				manageDefaultConfigurationKeys(keyCode, event);
			}
			return true;
		}

		private void manageDefaultConfigurationKeys(int keyCode, KeyEvent event) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_SEARCH:
				Input.getInput().addEvent("onKeySearch", event, -1, -1);
				break;
			case KeyEvent.KEYCODE_BACK:
				finish();
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				Input.getInput().addEvent("onVolDown", event, -1, -1);
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				Input.getInput().addEvent("onVolUp", event, -1, -1);
				break;
			}
		}

		private boolean manageCustomConfigurationKeys(int keyCode, KeyEvent e) {
			int i = 0;
			boolean found = false;
			if (e.getAction() == KeyEvent.ACTION_DOWN) {
				while (!found && i < keyboard.getNum()) {
					found = keyboard.getAction(keyCode) != null;
					i++;
				}
				if (found) {
					if (keyboard.getAction(keyCode).equals(
							KeyConfActivity.ACTION_RECORD)) {
						Input.getInput().addEvent(
								KeyConfActivity.ACTION_RECORD, e, -1, -1);
					}
				}
			}
			return found;
		}

		public GolfGamePanel(Context context) {
			super(context);
			mGestureDetector = new GestureDetector(new MyGestureDetector(context));
		}

		@Override
		public void onInitalize() {
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			game.onDraw(canvas);
		}

		@Override
		public void onUpdate() {
			game.onUpdate();
		}

	}

}
