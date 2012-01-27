package org.example.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.example.R;
import org.example.golf.XML.KeyboardWriter;
import org.example.golf.XML.XMLKeyboard;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the music manager of the game
 */

public class MainActivity extends Activity implements OnClickListener, OnFocusChangeListener  {
	
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
	
	private AlertDialog loseDialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		keyboard = Input.getKeyboard();
		this.fillXMLKeyboard();
		
		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		newButton.setOnFocusChangeListener(this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		aboutButton.setOnFocusChangeListener(this);
		View instructionsButton = findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);
		instructionsButton.setOnFocusChangeListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
		exitButton.setOnFocusChangeListener(this);

		createEndingDialog();
		
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
			openNewGameDialog();
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}
	
	
	/**
	 * Shows selection mode dialog
	 * 
	 * */
	private void openNewGameDialog() {
		Builder newGameAlertDialogBuilder = new AlertDialog.Builder(this)
		.setTitle(R.string.new_game_dialog_title).setItems(R.array.modes,
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialoginterface, int i) {
						startGame(i);
					}
				});
		AlertDialog newGameAlertDialog = newGameAlertDialogBuilder.create();
		newGameAlertDialog.show();
		ListView l = newGameAlertDialog.getListView();
		l.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView option = (TextView) view;
				textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				textToSpeech.speak((String) option.getText());
				textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
			}
		
			public void onNothingSelected(AdapterView<?> arg0) {
		
			}
		});
	
		textToSpeech.speak(this
				.getString(R.string.alert_dialog_modes_TTStext)
				+ " "
				+ this.getString(R.string.StageMode)
				+ " "
				+ this.getString(R.string.FreeMode));
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
		Builder instructionsAlertDialogBuilder = new AlertDialog.Builder(this)
				.setTitle(R.string.instructions_title).setItems(
						R.array.instructions,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startInstructions(i);
							}
						});
		AlertDialog instructionsAlertDialog = instructionsAlertDialogBuilder
				.create();
		instructionsAlertDialog.show();
		ListView l = instructionsAlertDialog.getListView();

		textToSpeech.speak(this
				.getString(R.string.alert_dialog_instructions_TTStext)
				+ this.getString(R.string.instructions_general_label)
				+ " "
				+ this.getString(R.string.instructions_controls_label));

		l.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView option = (TextView) view;
				textToSpeech.setQueueMode(TTS.QUEUE_ADD);
				textToSpeech.speak((String) option.getText());
				textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
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
	
	
    private void createEndingDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.EndingDialogTitle)
				.setCancelable(false)
				.setPositiveButton(R.string.EndingPositiveButtonLabel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
							}
						});
		loseDialog = builder.create();
    }
    
    private void showEndingDialog(int actualRecord){
		// Show dialog
		loseDialog.show();
		Button buttonPositive = loseDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonPositive.setContentDescription(getString(R.string.EndingPositiveButtonLabel));
		textToSpeech.speak(getString(R.string.EndingDialogTitle) + " " + actualRecord + " " + 
						getString(R.string.EndingPositiveButtonLabel));
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