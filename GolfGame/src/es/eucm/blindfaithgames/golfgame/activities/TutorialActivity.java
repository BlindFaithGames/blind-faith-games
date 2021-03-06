package es.eucm.blindfaithgames.golfgame.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;


import es.eucm.blindfaithgames.engine.feedback.AnalyticsManager;
import es.eucm.blindfaithgames.engine.general.DrawablePanel;
import es.eucm.blindfaithgames.engine.general.Game;
import es.eucm.blindfaithgames.engine.general.GameState;
import es.eucm.blindfaithgames.engine.input.Input;
import es.eucm.blindfaithgames.engine.input.XMLKeyboard;
import es.eucm.blindfaithgames.engine.sound.Music;
import es.eucm.blindfaithgames.engine.sound.TTS;
import es.eucm.blindfaithgames.golfgame.R;
import es.eucm.blindfaithgames.golfgame.game.GolfGameAnalytics;
import es.eucm.blindfaithgames.golfgame.game.GolfGameplay;

public class TutorialActivity extends Activity {

	private static String TAG = "TutorialActivity";

	private static final Integer GAMEPLAY_ID = 0;

	private Game game;
	private boolean mIsScrolling = false;
	private static TTS textToSpeech;

	// Cargamos la conf desde un .xml
	private XMLKeyboard keyboard;
	private SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(
				MainActivity.KEY_TTS);
		textToSpeech.setContext(this);

		keyboard = Input.getKeyboard();

		DrawablePanel golfView = new GolfGamePanel(this);
		setContentView(golfView);

		createGame(2, golfView);
		
		if (SettingsActivity.getBlindMode(this))
			game.setDisabled(true);

		// We set the configuration options at its default values
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		SettingsActivity.setEditor(settings.edit());

		AnalyticsManager.getAnalyticsManager(this).registerPage(
				GolfGameAnalytics.TUTORIAL_ACTIVITY);
	}

	private void createGame(int mode, DrawablePanel golfView) {
		ArrayList<Integer> order = new ArrayList<Integer>();
		order.add(GAMEPLAY_ID);

		game = new Game();

		ArrayList<GameState> gameStates = new ArrayList<GameState>();
		gameStates.add(GAMEPLAY_ID, new GolfGameplay(mode, golfView,
				textToSpeech, this, game));

		game.initialize(gameStates, order);
	}

	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.stop();
		Music.getInstanceMusic().stop(R.raw.sound_shot);
		Music.getInstanceMusic().stop(R.raw.previous_shoot_feedback_sound);
		Music.getInstanceMusic().stop(R.raw.clue_feed_back_sound);
		Music.getInstanceMusic().stop(R.raw.win_sound);
	}

	/*---------------------------------------------------------------------
	 *  GESTURE DETECTOR
	 ----------------------------------------------------------------------*/
	class MyGestureDetector extends SimpleOnGestureListener {
		private Context c;

		public MyGestureDetector(Context context) {
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

				AnalyticsManager.getAnalyticsManager().registerAction(
						GolfGameAnalytics.GAME_EVENTS,
						GolfGameAnalytics.FLING,
						"e1: (" + e1.getX() + ", " + e1.getY() + ") e2: ("
								+ e2.getX() + ", " + e2.getY() + ")", 0);
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

			AnalyticsManager.getAnalyticsManager().registerAction(
					GolfGameAnalytics.GAME_EVENTS,
					GolfGameAnalytics.SCROLL,
					"e1: (" + e1.getX() + ", " + e1.getY() + ") e2: ("
							+ e2.getX() + ", " + e2.getY() + ")", 0);
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
		private boolean dragging;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			onDrag(event);
			if (mGestureDetector.onTouchEvent(event)) {
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (mIsScrolling) {
					Log.d(TAG, "onUp: " + event.toString());
					mIsScrolling = false;
					Input.getInput().addEvent("onUp",
							MotionEvent.obtain(event), null, -1, -1);
					AnalyticsManager.getAnalyticsManager().registerAction(
							GolfGameAnalytics.GAME_EVENTS,
							GolfGameAnalytics.ON_UP,
							event.getX() + " " + event.getY(), 0);
					return true;
				}
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mIsScrolling = true;
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG, "onDown: " + event.toString());
				Input.getInput().addEvent("onDown", MotionEvent.obtain(event),
						null, -1, -1);
				AnalyticsManager.getAnalyticsManager().registerAction(
						GolfGameAnalytics.GAME_EVENTS,
						GolfGameAnalytics.SIMPLE_TAP,
						event.getX() + " " + event.getY(), 0);
				return true;
			}
			return false;
		}

		private void onDrag(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				dragging = true;
				Input.getInput().addEvent("onDrag", MotionEvent.obtain(event),
						null, -1, -1);
				AnalyticsManager.getAnalyticsManager().registerAction(
						GolfGameAnalytics.GAME_EVENTS, GolfGameAnalytics.DRAG,
						event.getX() + " " + event.getY(), 0);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				dragging = false;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (dragging) {
					Input.getInput().addEvent("onDrag",
							MotionEvent.obtain(event), null, -1, -1);
					AnalyticsManager.getAnalyticsManager().registerAction(
							GolfGameAnalytics.GAME_EVENTS,
							GolfGameAnalytics.DRAG,
							event.getX() + " " + event.getY(), 0);
				}
			}
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
					if (keyboard.getAction(keyCode).equals(
							KeyConfActivity.ACTION_REPEAT)) {
						textToSpeech.repeatSpeak();
					}
				}
			}
			AnalyticsManager.getAnalyticsManager().registerAction(
					GolfGameAnalytics.GAME_EVENTS,
					GolfGameAnalytics.KEY_PUSHED,
					keyboard.toString(keyCode) + " "
							+ keyboard.getAction(keyCode), 0);
			return found;
		}

		public GolfGamePanel(Context context) {
			super(context);
			mGestureDetector = new GestureDetector(new MyGestureDetector(
					context));
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