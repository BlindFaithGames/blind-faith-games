package org.example.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.TTS;
import org.example.zarodnik.XML.KeyboardWriter;
import org.example.zarodnik.XML.XMLKeyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez
 * This class implements the music manager of the game
 */

public class MainActivity extends Activity implements OnClickListener, OnFocusChangeListener  {
	
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;
	
	public static final String KEY_TTS = "org.example.tinyEngineClasses.TTS";
	public static final String KEY_INSTRUCTIONS_CONTROLS = "org.example.zarodnikGame.mainActivity.iControls";
	public static final String KEY_INSTRUCTIONS_GENERAL = "org.example.zarodnikGame.mainActivity.iGeneral";
	public static final String KEY_TYPE_INSTRUCTIONS = "org.example.zarodnikGame.mainActivity.iType";
	public static final String KEY_RESULTS = "org.example.mainActivity.record";
	
	public static String FILENAMEFREEMODE = "ZarodnikRecords1.data";
	public static String FILENAMESTAGEMODE = "ZarodnikRecords2.data";
	
	private TTS textToSpeech;
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	private Dialog instructionsDialog;
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Typeface font;
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		keyboard = Input.getKeyboard();
		this.fillXMLKeyboard();
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		Button newButton = (Button) findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		newButton.setOnFocusChangeListener(this);
		newButton.setTextSize(RuntimeConfig.FONT_SIZE);
		newButton.setTypeface(font);	
		Button aboutButton = (Button) findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		aboutButton.setOnFocusChangeListener(this);
		aboutButton.setTextSize(RuntimeConfig.FONT_SIZE);
		aboutButton.setTypeface(font);
		Button instructionsButton = (Button) findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);
		instructionsButton.setOnFocusChangeListener(this);
		instructionsButton.setTextSize(RuntimeConfig.FONT_SIZE);
		instructionsButton.setTypeface(font);
		Button exitButton = (Button) findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		exitButton.setOnFocusChangeListener(this);
		exitButton.setTextSize(RuntimeConfig.FONT_SIZE);
		exitButton.setTypeface(font);
		
		createInstructionsDialog();
		
		checkFolderApp(getString(R.string.app_name)+".xml");

		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.introMainMenu)
				+ newButton.getContentDescription() + " "
				+ instructionsButton.getContentDescription() + " "
				+ aboutButton.getContentDescription() + " "
				+ exitButton.getContentDescription(), TTS.QUEUE_FLUSH);
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		textToSpeech.setEnabled(SettingsActivity.getTTS(this));
	}	
	
	private void createInstructionsDialog() {
		Typeface font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		TextView text; Button b;
		
		instructionsDialog = new Dialog(this);
		instructionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		instructionsDialog.setContentView(R.layout.custom_dialog);
		instructionsDialog.setTitle("hola");
		
		text = (TextView) instructionsDialog.findViewById(R.id.TextView01);
		text.setTextSize(RuntimeConfig.FONT_SIZE);
		text.setTypeface(font);
		b = (Button) instructionsDialog.findViewById(R.id.Button01main);
		b.setTextSize(RuntimeConfig.FONT_SIZE);
		b.setTypeface(font);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b = (Button) instructionsDialog.findViewById(R.id.Button02main);
		b.setOnClickListener(this);
		b.setOnFocusChangeListener(this);
		b.setTextSize(RuntimeConfig.FONT_SIZE);
		b.setTypeface(font);
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, AboutActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			break;
		case R.id.instructions_button:
			openInstructionsDialog();
			break;
		case R.id.new_button:
			startGame();
			break;
		case R.id.Button01main: // controls
			startInstructions(0);
			break;
		case R.id.Button02main: // instructions
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
		}
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
	private void startGame() {
		Intent intent = new Intent(this, ZarodnikGameActivity.class);
		intent.putExtra(KEY_TTS, textToSpeech);
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
			nextIntent = new Intent(getApplicationContext(),ZarodnikGameActivity.class);
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
	 * It creates the options menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		textToSpeech.speak(this
				.getString(R.string.settings_menu_initial_TTStext));
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		textToSpeech.speak(this
				.getString(R.string.key_configuration_menu_initial_TTStext));
		textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
		return true;
	}

	/**
	 * Manages what to do depending on the selected item
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			Intent i = new Intent(this, SettingsActivity.class);
			i.putExtra(KEY_TTS, textToSpeech);
			startActivity(i);
			return true;
		case R.id.keyConf:
			Intent i1 = new Intent(this, KeyConfActivity.class);
			i1.putExtra(KEY_TTS, textToSpeech);
			startActivity(i1);
			return true;
		}
		return false;
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