package org.example.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.example.R;
import org.example.golf.XML.KeyboardWriter;
import org.example.golf.XML.XMLKeyboard;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the music manager of the game
 */

public class MainActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnLongClickListener{
	
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;

	public static final String KEY_TTS = "org.example.tinyEngineClasses.TTS";
	public static final String KEY_INSTRUCTIONS_CONTROLS = "org.example.golfGame.mainActivity.iControls";
	public static final String KEY_INSTRUCTIONS_GENERAL = "org.example.golfGame.mainActivity.iGeneral";
	public static final String KEY_TYPE_INSTRUCTIONS = "org.example.golfGame.mainActivity.iType";
	public static final String KEY_MODE = "org.example.mainActivity.Mode";
	public static final String KEY_RESULTS = "org.example.mainActivity.record";
	
	public static String FILENAMEFREEMODE = "GolfRecords1.data";
	public static String FILENAMESTAGEMODE = "GolfRecords2.data";
	
	private TTS textToSpeech;
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	
	private Dialog gameDialog;
	private Dialog instructionsDialog;
	
	private View focusedView;
	
	private static float fontSize;
	private static float scale;
	private static Typeface font;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		

		keyboard = Input.getKeyboard();
		this.fillXMLKeyboard();
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		
		setContentScreen();
		
		checkFolderApp(getString(R.string.app_name)+".xml");
	}	
	
	private void setContentScreen() {
		setContentView(R.layout.main);
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
		
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.introMainMenu)
				+ newButton.getContentDescription() + " "
				+ settingsButton.getContentDescription() + " "
				+ keyConfButton.getContentDescription() + " "
				+ instructionsButton.getContentDescription() + " "
				+ aboutButton.getContentDescription() + " "
				+ exitButton.getContentDescription(), TTS.QUEUE_FLUSH);
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
		
	}

	/**
	 * Default keyboard config
	 */
	private void fillXMLKeyboard(){
		keyboard.addObject(22, KeyConfActivity.ACTION_RECORD);
		keyboard.setNum(1);
	}

	private void checkFolderApp(String file) {
		File f = new File(file);
		if (f == null || (!f.exists() && !f.mkdir())) {
			if (writer == null) writer = new KeyboardWriter();
			try {
				FileOutputStream fos = openFileOutput(file, 3);
				writer.saveEditedKeyboard(keyboard.getNum(), keyboard.getKeyList(), fos);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
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
	}
	
	
	/**
	 * Shows selection mode dialog
	 * 
	 * */
	private void openNewGameDialog() {
		gameDialog.show();
	
		Music.getInstanceMusic().stop(this,R.raw.main);
		
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
			openInstructionsDialog();
			break;
		case R.id.new_button:
			openNewGameDialog();
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
			break;
		case R.id.instructions_general_button: // instructions
			startInstructions(1);
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
		
		if (i == 0)
			intent.putExtra(KEY_INSTRUCTIONS_CONTROLS, getString(R.id.instructions_controls_content));
		else
			intent.putExtra(KEY_INSTRUCTIONS_GENERAL, getString(R.id.instructions_general_content));
		
		
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
	 * ------------------------------------------------------------ Musica
	 * ---------------------------------------------------------------
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if(SettingsActivity.getMusic(this))
			Music.getInstanceMusic().play(this, R.raw.main, true);

		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
		
		textToSpeech.speak(this.getString(R.string.main_menu_initial_TTStext));
		
		Music.getInstanceMusic().stop(this,R.raw.storm);
		
		// Removes all events
		Input.getInput().clean();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.getInstanceMusic().stop(this,R.raw.main);
	}

	/**
	 * Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.stop();
	}

}