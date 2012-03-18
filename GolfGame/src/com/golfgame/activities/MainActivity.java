package com.golfgame.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.accgames.input.Input;
import com.accgames.input.KeyboardReader;
import com.accgames.input.XMLKeyboard;
import com.accgames.others.AnalyticsManager;
import com.accgames.others.GolfMusicSources;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Music;
import com.accgames.sound.SubtitleInfo;
import com.accgames.sound.TTS;
import com.golfgame.R;
import com.golfgame.game.GolfGameAnalytics;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the music manager of the game
 */

public class MainActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnLongClickListener{
	
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;

	public static final String KEY_TTS = "org.example.tinyEngineClasses.TTS";
	public static final String KEY_TYPE_INSTRUCTIONS = "org.example.golfGame.mainActivity.iType";
	public static final String KEY_MODE = "org.example.mainActivity.Mode";
	public static final String KEY_RESULTS = "org.example.mainActivity.record";
	
	public static String FILENAMEFREEMODE = "GolfRecords1.data";
	public static String FILENAMESTAGEMODE = "GolfRecords2.data";
	
	private TTS textToSpeech;
	private KeyboardReader reader;
	private XMLKeyboard keyboard;
	
	private Dialog gameDialog;
	private Dialog instructionsDialog;
	
	private View focusedView;
	private boolean gamed;
	
	
	private static float fontSize;
	private static float scale;
	private static Typeface font;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		keyboard = Input.getKeyboard();
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		
		setContentScreen();
		
		checkFolderApp(getString(R.string.app_name)+".xml");
		
		Display display = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.MAIN_ACTIVITY);
		AnalyticsManager.getAnalyticsManager(this).registerAction(GolfGameAnalytics.MISCELLANEOUS, GolfGameAnalytics.DEVICE_DATA, 
				Build.DEVICE + " " + Build.MODEL + " " + Build.MANUFACTURER
				+ " " + Build.BRAND + " " + Build.HARDWARE + " " + width + " " + height, 3);
	}	
	
	private void setContentScreen() {
		setContentView(R.layout.main);
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
		
		Button exitButton = (Button) findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		exitButton.setOnFocusChangeListener(this);
		exitButton.setOnLongClickListener(this);
		exitButton.setTextSize(fontSize);
		exitButton.setTypeface(font);
		
		createGameDialog();
		
		createInstructionsDialog();
		
		Map<Integer, String> onomatopeias = GolfMusicSources.getMap(this);
		
		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
		
		Music.getInstanceMusic().enableTranscription(this, s);
		
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.introMainMenu)
				+ newButton.getContentDescription() + ","
				+ tutorialButton.getContentDescription() + ","
				+ settingsButton.getContentDescription() + ","
				+ keyConfButton.getContentDescription() + ","
				+ instructionsButton.getContentDescription() + ","
				+ aboutButton.getContentDescription() + ","
				+ exitButton.getContentDescription(), TTS.QUEUE_FLUSH, s);
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
	}

	/**
	 * Default keyboard config
	 */
	private void fillXMLKeyboard(){
		keyboard.addObject(22, KeyConfActivity.ACTION_RECORD);
		keyboard.addObject(24, KeyConfActivity.ACTION_BLIND_MODE);
		keyboard.setNum(2);
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
				e.printStackTrace();
				keyboard = Input.getKeyboard();
				this.fillXMLKeyboard();
			}
		}
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
			AnalyticsManager.getAnalyticsManager().registerAction(GolfGameAnalytics.MAIN_MENU_EVENTS, GolfGameAnalytics.CLICK, 
				"Button Reading", 0);
		else
			AnalyticsManager.getAnalyticsManager().registerAction(GolfGameAnalytics.MAIN_MENU_EVENTS, GolfGameAnalytics.CLICK, 
					"Button Reading fail", 0);
	}
	
	
	/**
	 * Shows selection mode dialog
	 * 
	 * */
	private void openNewGameDialog() {
		gameDialog.show();
	
		Music.getInstanceMusic().stop(R.raw.main);
		
		textToSpeech.speak(this
				.getString(R.string.alert_dialog_modes_TTStext)
				+ " "
				+ this.getString(R.string.StageMode)
				+ " "
				+ this.getString(R.string.FreeMode));
	}

	@Override
	public boolean onLongClick(View v) {
		menuAction(v);
		if(v != null)
			AnalyticsManager.getAnalyticsManager().registerAction(GolfGameAnalytics.MAIN_MENU_EVENTS,
					GolfGameAnalytics.LONG_CLICK, "Success", 0);
		else
			AnalyticsManager.getAnalyticsManager().registerAction(GolfGameAnalytics.MAIN_MENU_EVENTS, GolfGameAnalytics.LONG_CLICK, 
					"Fail", 0);
		return true;
	}

	private void menuAction(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.about_button:
			i = new Intent(this, AboutActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.instructions_button:
			AnalyticsManager.getAnalyticsManager(this).registerAction(GolfGameAnalytics.MISCELLANEOUS, 
					GolfGameAnalytics.OPEN_INSTRUCTIONS, "Yes", 0);
			openInstructionsDialog();
			break;
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.tutorial_button:
			i = new Intent(this, TutorialActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.keyConf_button:
			i = new Intent(this, KeyConfActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.settings_button:
			i = new Intent(this, SettingsActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.stage_mode_button:
			startGame(0);
			gameDialog.dismiss();
			break;
		case R.id.free_mode_button:
			startGame(1);
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
				AnalyticsManager.getAnalyticsManager(this).registerAction(GolfGameAnalytics.MISCELLANEOUS, 
						GolfGameAnalytics.LEAVES_GAME, "Yes", 3);
			}
			finish();
			break;
		}
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
	private void createGameDialog() {
		Button b; TextView t;
		
		gameDialog = new Dialog(this);
		gameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		gameDialog.setContentView(R.layout.game_dialog);
		
		t = (TextView) gameDialog.findViewById(R.id.game_textView);
		t.setTextSize(fontSize);
		t.setTypeface(font);	
		b = (Button) gameDialog.findViewById(R.id.stage_mode_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);	
		b = (Button) gameDialog.findViewById(R.id.free_mode_button);
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
	
	
	/** Ask the user what type of instructions */
	private void openInstructionsDialog() {
		textToSpeech.speak(this
				.getString(R.string.alert_dialog_instructions_TTStext)
				+ this.getString(R.string.instructions_general_label)
				+ " "
				+ this.getString(R.string.instructions_controls_label));
		
		instructionsDialog.show();
	}


	/** Start a new game with the given difficulty level 
	 * @param mode */
	private void startGame(int mode) {
		gamed = true;
		Intent intent = new Intent(this, GolfGameActivity.class);
		intent.putExtra(KEY_TTS, textToSpeech);
		intent.putExtra(KEY_MODE, mode);
		startActivityForResult(intent, RESET_CODE);
	}


	/** Start an Instructions screen, with the option given: controls or general **/
	private void startInstructions(int i) {
		Intent intent;
		
		intent = new Intent(this, InstructionsActivity.class);
		intent.putExtra(KEY_TYPE_INSTRUCTIONS, i);
		
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
			nextIntent = new Intent(getApplicationContext(),GolfGameActivity.class);
			nextIntent.putExtra(KEY_TTS, textToSpeech);
			startActivityForResult(nextIntent, RESET_CODE);
			break;
		case (EXIT_GAME_CODE):
			nextIntent = new Intent(getApplicationContext(),RankingActivity.class);
			nextIntent.putExtra(KEY_TTS, textToSpeech);
			nextIntent.putExtra(KEY_RESULTS, data.getStringExtra(KEY_RESULTS));
			startActivity(nextIntent);
			break;
		case (RESULT_CANCELED):
			break;
		default:
			finish();
			break;
		}
	}
	
	/**
	 * ------------------------------------------------------------ Music
	 * ---------------------------------------------------------------
	 */
	@Override
	protected void onResume() {
		super.onResume();

		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
		
		if(SettingsActivity.getTranscription(this)){
			
			Map<Integer, String> onomatopeias = GolfMusicSources.getMap(this);
			
			SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
					R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
			
			textToSpeech.enableTranscription(s);
			Music.getInstanceMusic().enableTranscription(this, s);
		}else{
			textToSpeech.disableTranscription();
			Music.getInstanceMusic().disableTranscription();
		}
		
		if(SettingsActivity.getMusic(this))
			Music.getInstanceMusic().play(this, R.raw.main, true);
		
		textToSpeech.speak(this.getString(R.string.main_menu_initial_TTStext));
		
 		if (!TTS.isBestTTSInstalled(this)){
			Toast toast = Toast.makeText(this, getString(R.string.synthesizer_suggestion), Toast.LENGTH_LONG);
			toast.show();
			textToSpeech.speak(getString(R.string.synthesizer_suggestion));
		}
		
		Music.getInstanceMusic().stop(R.raw.storm);
		
		// Removes all events
		Input.getInput().clean();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.getInstanceMusic().stop(R.raw.main);
	}

	/**
	 * Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.stop();
    	AnalyticsManager.dispatch();
		AnalyticsManager.stopTracker();
	}

}