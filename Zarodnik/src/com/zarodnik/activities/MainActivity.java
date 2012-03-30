package com.zarodnik.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.accgames.input.Input;
import com.accgames.input.KeyboardReader;
import com.accgames.input.XMLKeyboard;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Music;
import com.accgames.sound.SubtitleInfo;
import com.accgames.sound.TTS;
import com.zarodnik.R;
import com.zarodnik.game.ZarodnikMusicSources;

/**
 * @author Gloria Pozuelo and Javier Álvarez
 * This class implements the music manager of the game
 */

public class MainActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnLongClickListener {
	
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;
	
	public static final String KEY_TTS = "org.example.tinyEngineClasses.TTS";
	public static final String KEY_INSTRUCTIONS_CONTROLS = "org.example.zarodnikGame.mainActivity.iControls";
	public static final String KEY_INSTRUCTIONS_GENERAL = "org.example.zarodnikGame.mainActivity.iGeneral";
	public static final String KEY_TYPE_INSTRUCTIONS = "org.example.zarodnikGame.mainActivity.iType";
	public static final String KEY_RESULTS = "org.example.mainActivity.record";
	
	public static String FILENAMEFREEMODE = "ZarodnikRecords1.data";
	public static String FILENAMESTAGEMODE = "ZarodnikRecords2.data";
	
	private static float fontSize;
	private static float scale;
	
	private TTS textToSpeech;
	private KeyboardReader reader;
	private XMLKeyboard keyboard;
	private Dialog instructionsDialog, interactionModeDialog;
	private View focusedView;
	private Typeface font;
	
	private SharedPreferences wmbPreference;
	private SharedPreferences.Editor editor;
	private boolean blindInteraction;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setScreenContent(R.layout.main);

		keyboard = Input.getKeyboard();
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		checkFirstExecution();
		
		checkFolderApp(getString(R.string.app_name)+".xml");
	}	
	
	private void checkFirstExecution() {
		wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirstRun = wmbPreference.getBoolean(SettingsActivity.FIRSTRUN, SettingsActivity.FIRSTRUN_DEF);
		if (isFirstRun)	{
		    // Code to run once
			this.createInteractionModeDialog();
			this.openInteractionModeDialog();
			
		    editor = wmbPreference.edit();
		    editor.putBoolean(SettingsActivity.FIRSTRUN, false);
		    editor.commit();
		}
		else{
			setTTS();
		}
	}
	
			
	// Manage UI Screens

	/**
	 * Sets the screen content based on the screen id.
	 */
	private void setScreenContent(int screenId) {
		setContentView(screenId);
		setMainScreenContent();
	}

	private void setMainScreenContent(){
		Button newButton = (Button) findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		newButton.setOnFocusChangeListener(this);
		newButton.setOnLongClickListener(this);
		newButton.setTextSize(fontSize);
		newButton.setTypeface(font);	
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
		Button instructionsButton = (Button) findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);
		instructionsButton.setOnFocusChangeListener(this);
		instructionsButton.setOnLongClickListener(this);
		instructionsButton.setTextSize(fontSize);
		instructionsButton.setTypeface(font);
		Button aboutButton = (Button) findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		aboutButton.setOnLongClickListener(this);
		aboutButton.setOnFocusChangeListener(this);
		aboutButton.setTextSize(fontSize);
		aboutButton.setTypeface(font);
		Button exitButton = (Button) findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		exitButton.setOnLongClickListener(this);
		exitButton.setOnFocusChangeListener(this);
		exitButton.setTextSize(fontSize);
		exitButton.setTypeface(font);
		
		createInstructionsDialog();

		checkFolderApp(getString(R.string.app_name)+".xml");
	}

	
	private void createInstructionsDialog() {
		Typeface font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		TextView text; Button b;
		
		instructionsDialog = new Dialog(this);
		instructionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		if (RuntimeConfig.blindMode)
			instructionsDialog.setContentView(R.layout.blind_instructions_dialog);
		else
			instructionsDialog.setContentView(R.layout.instructions_dialog);
		
		text = (TextView) instructionsDialog.findViewById(R.id.instructions_textView);
		text.setTextSize(fontSize);
		text.setTypeface(font);
		b = (Button) instructionsDialog.findViewById(R.id.Button01main);
		b.setTextSize(fontSize);
		b.setTypeface(font);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b = (Button) instructionsDialog.findViewById(R.id.Button02main);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);
	}
	
	private void createInteractionModeDialog() {
		Button b; TextView t;
		
		interactionModeDialog = new Dialog(this);
		interactionModeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		interactionModeDialog.setContentView(R.layout.interaction_mode_dialog);
		
		t = (TextView) interactionModeDialog.findViewById(R.id.interactionMode_textView);
		t.setTextSize(fontSize);
		t.setTypeface(font);	
		b = (Button) interactionModeDialog.findViewById(R.id.blindMode_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);	
		b = (Button) interactionModeDialog.findViewById(R.id.noBlindMode_button);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setOnLongClickListener(this);
		b.setTextSize(fontSize);
		b.setTypeface(font);		
	}
	
	private void setTTS(){
		Button newButton = (Button) findViewById(R.id.new_button);
		Button settingsButton = (Button) findViewById(R.id.settings_button);
		Button keyConfButton = (Button) findViewById(R.id.keyConf_button);
		Button instructionsButton = (Button) findViewById(R.id.instructions_button);
		Button aboutButton = (Button) findViewById(R.id.about_button);
		Button exitButton = (Button) findViewById(R.id.exit_button);
		
		Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);
		
		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
		
		Music.getInstanceMusic().enableTranscription(this, s);
		
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.introMainMenu)
				+ newButton.getContentDescription() + ","
				+ settingsButton.getContentDescription() + ","
				+ keyConfButton.getContentDescription() + ","
				+ instructionsButton.getContentDescription() + ","
				+ aboutButton.getContentDescription() + ","
				+ exitButton.getContentDescription(), TTS.QUEUE_FLUSH,s);
		
		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
	}

	/**
	 * Default keyboard config
	 */
	private void fillXMLKeyboard(){
		keyboard.addObject(22, KeyConfActivity.ACTION_RECORD);
		keyboard.addObject(84, KeyConfActivity.ACTION_BLIND_MODE);
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

	public void onClick(View v) {
		if (blindInteraction){
			if(focusedView != null){
				if(focusedView.getId() == v.getId())
					menuAction(v);
				else
					textToSpeech.speak(v);
			}
			else
				textToSpeech.speak(v);
		}
		else{
			menuAction(v);
		}
	}
	
	public boolean onLongClick(View v) {
		if (blindInteraction){
			menuAction(v);
			return true;	
		}
		return false;
	}
	
	private void menuAction(View v) {
		Intent i;
		switch (v.getId()) {
			case R.id.settings_button:
				i = new Intent(this, SettingsActivity.class);
				i.putExtra(KEY_TTS, textToSpeech);
				startActivity(i);
				break;
			case R.id.keyConf_button:
				i = new Intent(this, KeyConfActivity.class);
				i.putExtra(KEY_TTS, textToSpeech);
				startActivity(i);
				break;
			case R.id.instructions_button:
				openInstructionsDialog();
				break;
			case R.id.new_button:
				startGame();
				break;
			case R.id.about_button:
				i = new Intent(this, AboutActivity.class);
				i.putExtra(KEY_TTS, textToSpeech);
				startActivity(i);
				break;
			case R.id.Button01main: // controls
				startInstructions(0);
				instructionsDialog.dismiss();
				break;
			case R.id.Button02main: // instructions
				startInstructions(1);
				instructionsDialog.dismiss();
				break;
				// Interaction mode dialog
			case R.id.blindMode_button:
				// Change preferences
				editor.putBoolean(SettingsActivity.OPT_BLIND_INTERACTION, true);
				editor.commit();
				blindInteraction = true;
				interactionModeDialog.dismiss();
				setTTS();
				break;
			case R.id.noBlindMode_button:
				editor.putBoolean(SettingsActivity.OPT_BLIND_INTERACTION, false);
				editor.commit();
				blindInteraction = false;
				interactionModeDialog.dismiss();
				setTTS();
				break;
			case R.id.exit_button:
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
	
	/** Ask the user what interaction mode wants */
	private void openInteractionModeDialog() {
		interactionModeDialog.show();
		
		Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);
		
		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
		
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, this
				.getString(R.string.interactionMode_select_TTS)
				+ ","
				+ this.getString(R.string.blindMode_label)
				+ ","
				+ this.getString(R.string.noBlindMode_label), TTS.QUEUE_FLUSH, s);

		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
		
		if(SettingsActivity.getTranscription(this)){
			textToSpeech.enableTranscription(s);
			Music.getInstanceMusic().enableTranscription(this, s);
		}else{
			textToSpeech.disableTranscription();
			Music.getInstanceMusic().disableTranscription();
		}

		
	}

	/** Start a new game with the given difficulty level 
	 * @param mode */
	private void startGame() {
		Intent intent = new Intent(this, ZarodnikGameActivity.class);
		intent.putExtra(KEY_TTS, textToSpeech);
		startActivityForResult(intent, RESET_CODE);
	}

	/** Start an Instructions screen, with the option given: controls or general **/
	private void startInstructions(int i) {
		Intent intent;
		
		if (i == 0){
			intent = new Intent(this, ControlsActivity.class);
			intent.putExtra(KEY_INSTRUCTIONS_CONTROLS, getString(R.id.instructions_controls_content));
		}
		else{
			intent = new Intent(this, InstructionsActivity.class);
			intent.putExtra(KEY_INSTRUCTIONS_GENERAL, getString(R.id.instructions_general_content));
			
		}	
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
			nextIntent = new Intent(getApplicationContext(),ZarodnikGameActivity.class);
			nextIntent.putExtra(KEY_TTS, textToSpeech);
			startActivityForResult(nextIntent, RESET_CODE);
			break;
		case (RESULT_CANCELED):
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * ------------------------------------------------------------ Musica
	 * ---------------------------------------------------------------
	 */
	@Override
	protected void onResume() {
		super.onResume();

		blindInteraction = SettingsActivity.getBlindInteraction(this);
		
		if(SettingsActivity.getTranscription(this)){
			
			Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);
			
			SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
					R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
			
			
			textToSpeech.enableTranscription(s);
			Music.getInstanceMusic().enableTranscription(this, s);
		}else{
			textToSpeech.disableTranscription();
			Music.getInstanceMusic().disableTranscription();
		}
		
		if(SettingsActivity.getMusic(this))
			Music.getInstanceMusic().play(this, R.raw.the_path_of_the_goblin_king, true);
		
		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
		
		textToSpeech.speak(this.getString(R.string.main_menu_initial_TTStext));
		
		if (RuntimeConfig.blindMode)	
			setScreenContent(R.layout.blind_main);
		else
			setScreenContent(R.layout.main);
		
 		if (!TTS.isBestTTSInstalled(this)){
			Toast toast = Toast.makeText(this, getString(R.string.synthesizer_suggestion), Toast.LENGTH_LONG);
			toast.show();
			textToSpeech.speak(getString(R.string.synthesizer_suggestion));
		}
		
		// Removes all events
		Input.getInput().clean();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.getInstanceMusic().stop(R.raw.the_path_of_the_goblin_king);
	}

	/**
	 * Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.stop();
	}
	
		@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_SEARCH)
			RuntimeConfig.blindMode = !RuntimeConfig.blindMode; 
		
		if (RuntimeConfig.blindMode)	
			setScreenContent(R.layout.blind_main);
		else
			setScreenContent(R.layout.main);
		return super.onKeyDown(keyCode, event);
	}
}