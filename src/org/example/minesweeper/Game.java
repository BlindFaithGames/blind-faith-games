package org.example.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �lvarez This class
 *         implements the main activity
 */

public class Game extends Activity implements OnClickListener,
		OnFocusChangeListener {
	private static final String TAG = "Minesweeper";
	public static final int RESET_CODE = 1;
	public static final int EXIT_GAME_CODE = 2;

	private int difficult;
	private TextToSpeech mTts;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
	}

	public void init() {
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

		// Checking if TTS is installed on device
		OnInitTTS initialize = new OnInitTTS(mTts, "Main Menu minesweeper "
				+ newButton.getContentDescription() + " "
				+ instructionsButton.getContentDescription() + " "
				+ aboutButton.getContentDescription() + " "
				+ exitButton.getContentDescription());
		if (!Prefs.getTTS(this) && mTts != null)
			mTts = null;

		if (OnInitTTS.isInstalled(this) && Prefs.getTTS(this)) {
			mTts = new TextToSpeech(this, initialize);
			initialize.setmTts(mTts);
		}
	}

	/**
	 * onClick manager
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
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
	 * OnFocusChangeListener Interface
	 * */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus && mTts != null) {
			mTts.speak(v.getContentDescription().toString(),
					TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	/** Ask the user what difficulty level want */
	private void openNewGameDialog() {
		Builder newGameAlertDialogBuilder = new AlertDialog.Builder(this)
				.setTitle(R.string.new_game_title).setItems(R.array.difficulty,
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
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView option = (TextView) view;
				if(mTts != null)
						mTts.speak((String) option.getText(),
									TextToSpeech.QUEUE_FLUSH, null);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		if (mTts != null)
			mTts.speak(
					"Alert Dialog Difficulty " + this.getString(R.string.easy_label) + " "
							+ this.getString(R.string.medium_label) + " "
							+ this.getString(R.string.hard_label),
					TextToSpeech.QUEUE_FLUSH, null);
	}

	
	/** Ask the user what difficulty level want */
	private void openInstructionsDialog() {
		Builder instructionsAlertDialogBuilder = new AlertDialog.Builder(this)
				.setTitle(R.string.instructions_title).setItems(R.array.instructions,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startInstructions(i);
							}
						});
		AlertDialog instructionsAlertDialog = instructionsAlertDialogBuilder.create();
		instructionsAlertDialog.show(); 
		ListView l = instructionsAlertDialog.getListView();
		l.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				TextView option = (TextView) view;
				if(mTts != null)
						mTts.speak((String) option.getText(),
									TextToSpeech.QUEUE_FLUSH, null);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		if (mTts != null)
			mTts.speak(
					"Alert Dialog Instructions " + this.getString(R.string.instructions_general_label) + " "
							+ this.getString(R.string.instructions_controls_label),
					TextToSpeech.QUEUE_FLUSH, null);
	}
	
	/** Start a new game with the given difficulty level */
	private void startGame(int i) {
		Log.d(TAG, "clicked on " + i);
		Intent intent = new Intent(Game.this, Minesweeper.class);
		intent.putExtra(Minesweeper.KEY_DIFFICULTY, i);
		difficult = i;
		startActivityForResult(intent, RESET_CODE);
	}

	
	/** Start an Instructions screen, with the option given: controls or general **/
	private void startInstructions(int i) {
		Intent intent;
		if(i==0)
			intent = new Intent(Game.this, InstructionsGeneral.class);
		else 
			intent = new Intent(Game.this, InstructionsControls.class);
		startActivity(intent);
	}
	
	/**
	 * Operates on the outcome of the game
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case (RESET_CODE):
			Intent nextIntent = new Intent(getApplicationContext(),
					Minesweeper.class);
			nextIntent.putExtra(Minesweeper.KEY_DIFFICULTY, difficult);
			startActivityForResult(nextIntent, RESET_CODE);
			break;
		case (EXIT_GAME_CODE):
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
		if (mTts != null){
			mTts.speak("Menu Settings", TextToSpeech.QUEUE_FLUSH, null);
			mTts.speak("Key Configurations", TextToSpeech.QUEUE_FLUSH, null);
		}
		return true;
	}

	/**
	 * Manages what to do depending on the selected item
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true; 
		case R.id.keyConf:
			startActivity(new Intent(this, KeyConf.class));
			return true; 
		}

		return false;
	}

	/**
	 * ------------------------------------------------------------ M�sica
	 * ---------------------------------------------------------------
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.main);
		init();
		if (mTts != null)
			mTts.speak("Main Menu", TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}

	/**
	 * Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
	}
}