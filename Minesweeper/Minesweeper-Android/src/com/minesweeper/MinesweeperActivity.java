package com.minesweeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.accgames.XML.KeyboardReader;
import com.accgames.XML.XMLKeyboard;
import com.accgames.others.AnalyticsManager;
import com.accgames.others.Log;
import com.accgames.others.RuntimeConfig;
import com.minesweeper.game.Input;
import com.minesweeper.game.MinesweeperAnalytics;
import com.minesweeper.game.Music;
import com.minesweeper.game.SubtitleInfo;
import com.minesweeper.game.TTS;

/**
 * Main activity - messages from the server and provides
 * a menu item to invoke the accounts activity.
 */
public class MinesweeperActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnLongClickListener {
	/**
	 * Tag for logging.
	 */
	private static final String TAG = "MainMenu";
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;

	public static final String KEY_TTS = "org.example.game.TTS";
	public static final String KEY_DIFFICULTY = "org.example.game.difficulty";
	private static final String FILE_NAME_ID = ".info";

	private int difficult;
	private TTS textToSpeech;
	private KeyboardReader reader;
	private XMLKeyboard keyboard;

	private Dialog gameDialog;
	private Dialog instructionsDialog;
	
	private View focusedView;
	
	private static float fontSize;
	private static float scale;
	private static Typeface font;
	
	// To know if the user has started a game or not
	private boolean gamed = false;

//	private AsyncTask<Void, Void, String> task;

	// User id
	private UUID id;

	/**
	 * The current context.
	 */
	private Context mContext = this;

	/**
	 * A {@link BroadcastReceiver} to receive the response from a register or
	 * unregister request, and to update the UI.
	 */
	private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String accountName = intent
					.getStringExtra(DeviceRegistrar.ACCOUNT_NAME_EXTRA);
			int status = intent.getIntExtra(DeviceRegistrar.STATUS_EXTRA,
					DeviceRegistrar.ERROR_STATUS);
			String message = null;
			String connectionStatus = Util.DISCONNECTED;
			if (status == DeviceRegistrar.REGISTERED_STATUS) {
				message = getResources().getString(
						R.string.registration_succeeded);
				connectionStatus = Util.CONNECTED;
			} else if (status == DeviceRegistrar.UNREGISTERED_STATUS) {
				message = getResources().getString(
						R.string.unregistration_succeeded);
			} else {
				message = getResources().getString(R.string.registration_error);
			}

			// Set connection status
			SharedPreferences prefs = Util.getSharedPreferences(mContext);
			prefs.edit().putString(Util.CONNECTION_STATUS, connectionStatus)
					.commit();

			// Display a notification
			Util.generateNotification(mContext,
					String.format(message, accountName));
		}
	};
	/**
	 * Begins the activity.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		checkId();
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		// Register a receiver to provide register/unregister notifications
		registerReceiver(mUpdateUIReceiver, new IntentFilter(
				Util.UPDATE_UI_INTENT));

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setScreenContent(R.layout.main);
		
		checkFolderApp("minesweeper.xml");
		
		setTTS();

		Display display = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		// Screen size
		int width;
		int height;
		width = display.getWidth();
		height = display.getHeight();

		Log.getLog().setTag("Minesweeper");
		// Device information
		Log.getLog().addEntry(
				MinesweeperActivity.TAG,
				PrefsActivity.configurationToString(this),
				Log.DEVICE,
				Thread.currentThread().getStackTrace()[2].getMethodName(),
				Build.DEVICE + " " + Build.MODEL + " " + Build.MANUFACTURER
						+ " " + Build.BRAND + " " + Build.HARDWARE + " "
						+ width + " " + height + " " + id);
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(MinesweeperAnalytics.MAIN_ACTIVITY);
		AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.MISCELLANEOUS, MinesweeperAnalytics.DEVICE_DATA, 
				Build.DEVICE + " " + Build.MODEL + " " + Build.MANUFACTURER
				+ " " + Build.BRAND + " " + Build.HARDWARE + " "
				+ width + " " + height, 3);
;
	}
	
	private void checkId() {
		id = null;
		FileInputStream fis;
		try { 
			fis = this.openFileInput(MinesweeperActivity.FILE_NAME_ID);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object f = ois.readObject();
			id = (UUID) f;
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(id == null){
			id = UUID.randomUUID();
			save(id);
		}
	}
	
	private void save(UUID id) {
		FileOutputStream fos;
		try { 
			fos = this.openFileOutput(MinesweeperActivity.FILE_NAME_ID, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(id); 
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		Music.play(this, R.raw.main);

		textToSpeech.setEnabled(PrefsActivity.getTTS(this));
		
		if(PrefsActivity.getTranscription(this)){
			SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
					R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, null);
			textToSpeech.enableTranscription(s);
		}else
			textToSpeech.disableTranscription();

		textToSpeech.speak(this.getString(R.string.main_menu_initial_TTStext));
		
 		if (!TTS.isBestTTSInstalled(this)){
			Toast toast = Toast.makeText(this, getString(R.string.synthesizer_suggestion), Toast.LENGTH_LONG);
			toast.show();
			textToSpeech.speak(getString(R.string.synthesizer_suggestion));
		}

		Log.getLog().addEntry(MinesweeperActivity.TAG,
				PrefsActivity.configurationToString(this), Log.NONE,
				Thread.currentThread().getStackTrace()[2].getMethodName(), "");
	}

	/**
	 * Shuts down the activity and turns off TTS engine
	 */
	@Override
	public void onDestroy() {
		//unregisterReceiver(mUpdateUIReceiver);
		textToSpeech.stop();
		instructionsDialog.dismiss();
		gameDialog.dismiss();
    	AnalyticsManager.dispatch();
		super.onDestroy();
		AnalyticsManager.stopTracker();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}
	// Manage UI Screens

	/**
	 * Sets the screen content based on the screen id.
	 */
	private void setScreenContent(int screenId) {
		setContentView(screenId);
		switch (screenId) {
		case R.layout.main:
			setMainScreenContent(R.layout.main);
			break;
		case R.layout.blind_main:
			setMainScreenContent(R.layout.blind_main);
			break;
		}
	}

	private void setMainScreenContent(int id) {
		Button newButton = (Button) findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		newButton.setOnFocusChangeListener(this);
		newButton.setOnLongClickListener(this);
		newButton.setTextSize(fontSize);
		newButton.setTypeface(font);	
        Button tutorialButton = (Button) findViewById(R.id.tutorial_button);
        tutorialButton.setOnClickListener(this);
        tutorialButton.setOnFocusChangeListener(this);
        tutorialButton.setOnLongClickListener(this);
        tutorialButton.setTextSize(fontSize);
        tutorialButton.setTypeface(font);        
		Button settingsButton = (Button) findViewById(R.id.settings_button);
		settingsButton.setOnClickListener(this);
		settingsButton.setOnFocusChangeListener(this);
		settingsButton.setOnLongClickListener(this);
		settingsButton.setTextSize(fontSize);
		settingsButton.setTypeface(font);	
		Button keyConfButton = (Button) findViewById(R.id.keyConf_button);
		keyConfButton.setOnClickListener(this);
		keyConfButton.setOnFocusChangeListener(this);
		keyConfButton.setOnLongClickListener(this);
		keyConfButton.setTextSize(fontSize);
		keyConfButton.setTypeface(font);	
		Button aboutButton = (Button) findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		aboutButton.setOnFocusChangeListener(this);
		aboutButton.setOnLongClickListener(this);
		aboutButton.setTextSize(fontSize);
		aboutButton.setTypeface(font);	
		Button instructionsButton = (Button) findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);
		instructionsButton.setOnFocusChangeListener(this);
		instructionsButton.setOnLongClickListener(this);
		instructionsButton.setTextSize(fontSize);
		instructionsButton.setTypeface(font);	
		Button formButton = (Button) findViewById(R.id.form_button);
		formButton.setOnClickListener(this);
		formButton.setOnFocusChangeListener(this);
		formButton.setOnLongClickListener(this);
		formButton.setTextSize(fontSize);
		formButton.setTypeface(font);	
		Button exitButton = (Button) findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		exitButton.setOnFocusChangeListener(this);
		exitButton.setOnLongClickListener(this);
		exitButton.setTextSize(fontSize);
		exitButton.setTypeface(font);
		
		createGameDialog();
		
		createInstructionsDialog();
	}

	private void setTTS() {
		Button newButton = (Button) findViewById(R.id.new_button);
        Button tutorialButton = (Button) findViewById(R.id.tutorial_button);
		Button settingsButton = (Button) findViewById(R.id.settings_button);
		Button keyConfButton = (Button) findViewById(R.id.keyConf_button);
		Button aboutButton = (Button) findViewById(R.id.about_button);
		Button instructionsButton = (Button) findViewById(R.id.instructions_button);
		Button formButton = (Button) findViewById(R.id.form_button);
		Button exitButton = (Button) findViewById(R.id.exit_button);

		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, null);
	
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.intro_main_menu)
				+ newButton.getContentDescription() + ","
				+ tutorialButton.getContentDescription() + ","
				+ settingsButton.getContentDescription() + ","
				+ keyConfButton.getContentDescription() +  ","
				+ instructionsButton.getContentDescription() + ","
				+ aboutButton.getContentDescription() + ","
				+ formButton.getContentDescription() + ","
				+ exitButton.getContentDescription(), TTS.QUEUE_FLUSH, s);

		textToSpeech.setEnabled(PrefsActivity.getTTS(this));
	}

	/**
	 * Default keyboard config
	 */
	private void fillXMLKeyboard() {
		keyboard.addObject(24, "zoom");
		keyboard.addObject(25, "exploration");
		keyboard.addObject(82, "instructions");
		keyboard.addObject(84, "coordinates");
		keyboard.addObject(5, "context");
		keyboard.addObject(10, "blind_mode");
		keyboard.setNum(6);
	}

	private void checkFolderApp(String file) {
		File f = new File(file);
		if (f == null || (!f.exists() && !f.mkdir())) {
			if (reader == null)
				reader = new KeyboardReader();
			try {
				FileInputStream fis = openFileInput(file);
				keyboard = reader.loadEditedKeyboard(fis);
			} catch (FileNotFoundException e) {
				keyboard = Input.getInstance();
				this.fillXMLKeyboard();
			}
		}
	}
	
	@Override
	public boolean onLongClick(View v) {
		menuAction(v);
		if(v != null)
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.MAIN_MENU_EVENTS,
					MinesweeperAnalytics.LONG_CLICK, "Success", 0);
		else
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.MAIN_MENU_EVENTS, MinesweeperAnalytics.LONG_CLICK, 
					"Fail", 0);
		return true;
	}

	private void menuAction(View v) {
		switch (v.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, AboutActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.instructions_button:
			// The user checks the instructions.
			Log.getLog().addEntry(MinesweeperActivity.TAG,
					PrefsActivity.configurationToString(this),
					Log.INSTRUCTIONS,
					Thread.currentThread().getStackTrace()[2].getMethodName(),
					"Instructions");
			AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.MISCELLANEOUS, 
					MinesweeperAnalytics.OPEN_INSTRUCTIONS, "Yes", 0);
			openInstructionsDialog();
			break;
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.tutorial_button:
			i = new Intent(this, MinesweeperTutorialActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.form_button:
			i = new Intent(this, FormActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.settings_button:
			i = new Intent(this, PrefsActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.keyConf_button:
			i = new Intent(this, KeyConfActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.easy_button:
			startGame(0);
			gameDialog.dismiss();
			break;
		case R.id.medium_button:
			startGame(1);
			gameDialog.dismiss();
			break;
		case R.id.hard_button:
			startGame(2);
			gameDialog.dismiss();
			break;
		case R.id.controls_button: // controls
			startInstructions(0);
			instructionsDialog.dismiss();
			break;
		case R.id.instructions_general_button: // instructions
			startInstructions(1);
			instructionsDialog.dismiss();
			break;
		case R.id.exit_button:
			if (!gamed) {
				Log.getLog().addEntry(
						MinesweeperActivity.TAG,
						PrefsActivity.configurationToString(this),
						Log.EXIT,
						Thread.currentThread().getStackTrace()[2]
								.getMethodName(), "Exit");
				AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.MISCELLANEOUS, 
						MinesweeperAnalytics.LEAVES_GAME, "Yes", 3);
//				sendLog();
			}
			finish();
			break;
		}
	}

	private void createGameDialog() {
		Button b; TextView t;
		
		gameDialog = new Dialog(this);
		gameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (RuntimeConfig.blindMode)
			gameDialog.setContentView(R.layout.blind_game_dialog);
		else 
			gameDialog.setContentView(R.layout.game_dialog);
		
		t = (TextView) gameDialog.findViewById(R.id.game_textView);
		t.setTextSize(fontSize);
		t.setTypeface(font);	
		b = (Button) gameDialog.findViewById(R.id.easy_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);	
		b = (Button) gameDialog.findViewById(R.id.medium_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);	
		b = (Button) gameDialog.findViewById(R.id.hard_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);	
	}
	
	private void createInstructionsDialog() {
		Button b; TextView t;
		
		instructionsDialog = new Dialog(this);
		instructionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (RuntimeConfig.blindMode)
			instructionsDialog.setContentView(R.layout.blind_instructions_dialog);
		else
			instructionsDialog.setContentView(R.layout.instructions_dialog);
		
		t = (TextView) instructionsDialog.findViewById(R.id.instructions_textView);
		t.setTextSize(fontSize);
		t.setTypeface(font);
		b = (Button) instructionsDialog.findViewById(R.id.controls_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);	
		b = (Button) instructionsDialog.findViewById(R.id.instructions_general_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);	
	}

	/**
	 * onClick manager
	 */
	public void onClick(View v) {
		if(focusedView != null){
			if(focusedView.getId() == v.getId())
				menuAction(v);
			else
				textToSpeech.speak(v);
		}
		else
			textToSpeech.speak(v);
		if(v != null)
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.MAIN_MENU_EVENTS, MinesweeperAnalytics.CLICK, 
				"Button Reading", 0);
		else
			AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.MAIN_MENU_EVENTS, MinesweeperAnalytics.CLICK, 
					"Button Reading fail", 0);
	}

	/**
	 * OnFocusChangeListener Interface
	 * */
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			textToSpeech.speak(v);
			focusedView = v;
		}
	}

	/** Ask the user what difficulty level want */
	private void openNewGameDialog() {
		gameDialog.show();
		textToSpeech.speak(this
				.getString(R.string.alert_dialog_difficulty_TTStext)
				+ " "
				+ this.getString(R.string.easy_label)
				+ " "
				+ this.getString(R.string.medium_label)
				+ " "
				+ this.getString(R.string.hard_label));
	}

	/** Ask the user what type of instructions */
	private void openInstructionsDialog() {
		textToSpeech.speak(this
				.getString(R.string.alert_dialog_instructions_TTStext)
				+ " "
				+ this.getString(R.string.instructions_general_label)
				+ " "
				+ this.getString(R.string.instructions_controls_label));
		
		instructionsDialog.show();
	}

	/** Start a new game with the given difficulty level */
	private void startGame(int i) {
		gamed = true;
		Intent intent = new Intent(mContext, Minesweeper.class);
		intent.putExtra(KEY_TTS, textToSpeech);
		intent.putExtra(KEY_DIFFICULTY, i);
		difficult = i;
		startActivityForResult(intent, RESET_CODE);
	}

	/** Start an Instructions screen, with the option given: controls or general **/
	private void startInstructions(int i) {
		Intent intent;
		if (i == 0)
			intent = new Intent(mContext, InstructionsGeneralActivity.class);
		else
			intent = new Intent(mContext, InstructionsControlsActivity.class);

		intent.putExtra(KEY_TTS, textToSpeech);

		startActivity(intent);
	}

	/**
	 * Operates on the outcome of the game
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Intent nextIntent;
		switch (resultCode) {
		case (RESET_CODE):
//			sendLog();
			nextIntent = new Intent(getApplicationContext(), Minesweeper.class);
			nextIntent.putExtra(KEY_TTS, textToSpeech);
			nextIntent.putExtra(KEY_DIFFICULTY, difficult);
			startActivityForResult(nextIntent, RESET_CODE);
			break;
		case (EXIT_GAME_CODE):
//			sendLog();
			break;
		case (RESULT_CANCELED):
			break;
		default:
			finish();
			break;
		}
	}
	
//	private synchronized void sendLog() {
//    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//    	NetworkInfo nInfo = cm.getActiveNetworkInfo();
//    	if(nInfo != null){
//    		if(nInfo.isConnected()){
//				AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
//					private String message;
//	
//					@Override
//					protected String doInBackground(Void... arg0) {
//						MyRequestFactory factory = (MyRequestFactory) Util
//								.getRequestFactory(mContext, MyRequestFactory.class);
//						LogRequest request = factory.logRequest();
//	
//						LogProxy log = request.create(LogProxy.class);
//	
//						log.setTag(Log.getLog().getTag());
//						log.setLogEntries(sendEntries());
//						log.setFormAnswers(Log.getLog().getFormAnswers());
//						log.setComment(Log.getLog().getComment());
//	
//						request.createLog(log).fire(new Receiver<LogProxy>() {
//		                    @Override
//		                    public void onFailure(ServerFailure error) {
//		                        message = "Failure: " + error.getMessage();
//		                        synchronized (Log.getLog()){
//		                        	Log.getLog().notify();
//		                        }
//		                    }
//		                    @Override
//		                    public void onSuccess(LogProxy l) {
//		                        message = "We have received your log succesfully";
//		                        synchronized (Log.getLog()){
//		                        	Log.getLog().notify();
//		                        }
//		                    }
//		                });
//						return message;
//					}
//	
//					@Override
//		            protected void onPostExecute(String result) {
//						 Log.getLog().getLogEntries().clear();
//		            }
//		        }.execute();
//		        synchronized (Log.getLog()){
//					try {
//						textToSpeech.speak(this.getString(R.string.load_text));
//						Log.getLog().wait();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//		        }
//    		}
//    	}	
//	}
//
//	private List<Long> sendEntries() {
//		MyRequestFactory factory = (MyRequestFactory) Util.getRequestFactory(mContext, MyRequestFactory.class);
//		EntryRequest request;
//
//		Set<Integer> entries = Log.getLog().getEntryKeys();
//		Iterator<Integer> it = entries.iterator();
//		Integer entryN;
//		Entry e;
//		EntryProxy entry;
//		List<Long> result = new ArrayList<Long>();
//		while (it.hasNext()) {
//			entryN = it.next();
//			e = Log.getLog().getEntry(entryN);
//
//			request = factory.entryRequest();
//			entry = request.create(EntryProxy.class);
//
//			entry.setComment(e.getComment());
//			entry.setPath(e.getPath());
//			entry.setTag(e.getTag());
//			entry.setType(e.getType());
//			entry.setConfigurationSettings(e.getConfigurationSettings());
//			entry.setTimestamp(e.getTimestamp());
//
//			EntryReceiver r = new EntryReceiver();
//			request.createEntry(entry).fire(r);
//			Long n = new Long(r.getResult());
//			result.add(n);
//		}
//		return result;
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_SEARCH)
			RuntimeConfig.blindMode = !RuntimeConfig.blindMode; 
		
		if(RuntimeConfig.blindMode)	
			setScreenContent(R.layout.blind_main);
		else
			setScreenContent(R.layout.main);
		return super.onKeyDown(keyCode, event);
	}
}