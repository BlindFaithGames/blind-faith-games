package com.minesweeper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TableRow;

import com.accgames.feedback.AnalyticsManager;
import com.accgames.feedback.Log;
import com.accgames.input.Input;
import com.accgames.input.KeyboardWriter;
import com.accgames.input.XMLKeyboard;
import com.accgames.sound.TTS;
import com.minesweeper.game.MinesweeperAnalytics;


public class KeyConfActivity extends Activity implements OnFocusChangeListener, OnClickListener, OnLongClickListener {
	
	private static final String TAG = "keyConfiguration";
	
	public static final int KEY_PRESSED = 1;

	public static final String ACTION_REPEAT = "repeat";
	public static final String ACTION_ZOOM = "zoom";
	public static final String ACTION_CONTEXT = "context";
	public static final String ACTION_INSTRUCTIONS = "instructions";
	public static final String ACTION_EXPLORATION = "exploration";
	public static final String ACTION_COORDINATES = "coordinates";
	
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	private TTS textToSpeech;
	private String action;
	private int key;
	private Button buttonZoom, buttonInstructions, buttonExploration;
	private Button buttonCoordinates, buttonContext;
	private Button buttonRepeat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getKeyboard();
		
		TableRow tr;
		
		tr = (TableRow) findViewById(R.id.zoom_row);
		tr.setOnClickListener(this);

		tr = (TableRow) findViewById(R.id.exploration_row);
		tr.setOnClickListener(this);
		
		tr = (TableRow) findViewById(R.id.instructions_row);
		tr.setOnClickListener(this);
		
		tr = (TableRow) findViewById(R.id.coordinates_row);
		tr.setOnClickListener(this);
		
		tr = (TableRow) findViewById(R.id.context_row);
		tr.setOnClickListener(this);
		
		tr = (TableRow) findViewById(R.id.repeat_row);
		tr.setOnClickListener(this);
		
		buttonZoom = (Button) findViewById(R.id.buttonZoom);
		buttonZoom.setOnFocusChangeListener(this);
		buttonZoom.setOnClickListener(this);
		buttonZoom.setOnLongClickListener(this);
		
		buttonExploration = (Button) findViewById(R.id.buttonExploration);
		buttonExploration.setOnFocusChangeListener(this);
		buttonExploration.setOnClickListener(this);
		buttonExploration.setOnLongClickListener(this);

		buttonInstructions = (Button) findViewById(R.id.buttonInstructions);
		buttonInstructions.setOnFocusChangeListener(this);
		buttonInstructions.setOnClickListener(this);
		buttonInstructions.setOnLongClickListener(this);
		
		buttonCoordinates = (Button) findViewById(R.id.buttonCoordinates);
		buttonCoordinates.setOnFocusChangeListener(this);
		buttonCoordinates.setOnClickListener(this);
		buttonCoordinates.setOnLongClickListener(this);
		
		buttonContext = (Button) findViewById(R.id.buttonContext);
		buttonContext.setOnFocusChangeListener(this);
		buttonContext.setOnClickListener(this);
		buttonContext.setOnLongClickListener(this);
		
		buttonRepeat = (Button) findViewById(R.id.buttonRepeat);
		buttonRepeat.setOnFocusChangeListener(this);
		buttonRepeat.setOnClickListener(this);
		buttonRepeat.setOnLongClickListener(this);
		
		this.buttonsUpdate();

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
				+ buttonZoom.getContentDescription() + ", "
				+ buttonExploration.getContentDescription() + ", "
				+ buttonInstructions.getContentDescription() + ", "
				+ buttonCoordinates.getContentDescription() + ", "
				+ buttonContext.getContentDescription() + ", "
				+ buttonRepeat.getContentDescription());
		
		Log.getLog().addEntry(KeyConfActivity.TAG,PrefsActivity.configurationToString(this),
				Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),"Setting up keys");
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(MinesweeperAnalytics.KEY_CONF_ACTIVITY);
	}
	
	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		 super.onDestroy();
	     textToSpeech.stop();
	}
	
	private void buttonsUpdate(){
		buttonZoom.setText(keyboard.searchButtonByAction("zoom"));
		buttonExploration.setText(keyboard.searchButtonByAction("exploration"));
		buttonInstructions.setText(keyboard.searchButtonByAction("instructions"));
		buttonCoordinates.setText(keyboard.searchButtonByAction("coordinates"));
		buttonContext.setText(keyboard.searchButtonByAction("context"));
		buttonRepeat.setText(keyboard.searchButtonByAction("repeat"));
	}
	
	/**
	 * Guarda el teclado editado
	 * @throws ParserConfigurationException 
	 */
	public void saveEditedKeyboard(String file){
		// Si el writer no ha sido aún creado, lo creamos
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

	public void onClick(View v) {
		if(!PrefsActivity.getBlindInteraction(this)){
			menuAction(v);
		}else{
			if(v != null){
				if(v instanceof Button){
					String res = null;
					Button b = (Button) v;
					Integer s = keyboard.getKeyByButton(b.getText().toString());
					if(s != null)
						res = keyboard.toString(s);
					if(res != null)
						textToSpeech.speak(v.getContentDescription().toString() + " " + getString(R.string.infoKeyConf) +" " + res);
					else
						textToSpeech.speak(v.getContentDescription().toString() + " " + getString(R.string.infoKeyConffail));
					}
				else
					textToSpeech.speak(v);
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(PrefsActivity.getBlindInteraction(this)){
			menuAction(v);
			return true;
		}else
			return false;
	}
	
	private void menuAction(View v) {
		Intent intent = new Intent(this, CheckKeyActivity.class);
		intent.putExtra(MinesweeperActivity.KEY_TTS, textToSpeech);
		switch (v.getId()) {
		case R.id.buttonZoom:
			action = ACTION_ZOOM;
			break;
		case R.id.buttonExploration:
			action = ACTION_EXPLORATION;
			break;
		case R.id.buttonInstructions:
			action = ACTION_INSTRUCTIONS;
			break;
		case R.id.buttonCoordinates:
			action = ACTION_COORDINATES;
			break;
		case R.id.buttonContext:
			action = ACTION_CONTEXT;
			break;
		case R.id.buttonRepeat:
			action = ACTION_REPEAT;
			break;
		default:
			textToSpeech.speak(v);
			return;
		}
		AnalyticsManager.getAnalyticsManager().registerAction(MinesweeperAnalytics.CONFIGURATION_CHANGED,
				MinesweeperAnalytics.KEY_CONFIGURATION_CHANGED, "Action-Key Changed: " + action, 0);
		startActivityForResult(intent, KEY_PRESSED);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle extras = data.getExtras();
		key = extras.getInt(CheckKeyActivity.KEY_CODE);
		if (isValid(key)){
			switch (resultCode) {
			case (KEY_PRESSED):
				if (action.equals(ACTION_ZOOM)){;
					keyboard.addButtonAction(key, ACTION_ZOOM);
				}
				else if (action.equals(ACTION_EXPLORATION)){
					keyboard.addButtonAction(key, ACTION_EXPLORATION);
				}
				else if (action.equals(ACTION_INSTRUCTIONS)){
					keyboard.addButtonAction(key, ACTION_INSTRUCTIONS);
				}
				else if (action.equals(ACTION_COORDINATES)){
					keyboard.addButtonAction(key, ACTION_COORDINATES);
				}
				else if (action.equals(ACTION_CONTEXT)){
					keyboard.addButtonAction(key, ACTION_CONTEXT);
				}
				else if (action.equals(ACTION_REPEAT)){
					keyboard.addButtonAction(key, ACTION_REPEAT);
				}
				break;
			}
			buttonsUpdate();
			this.saveEditedKeyboard("minesweeper.xml");
			textToSpeech.speak(getString(R.string.key_conf_success) + " " + keyboard.searchButtonByAction(action));
			Log.getLog().addEntry(KeyConfActivity.TAG,PrefsActivity.configurationToString(this),
					Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),
					"New configuration: " + keyConfigurationtoString());
			
			AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.CONFIGURATION_CHANGED,
						MinesweeperAnalytics.KEY_CONFIGURATION_CHANGED,  MinesweeperAnalytics.KEY_CONFIGURATION_SUCCESS + keyConfigurationtoString(), 0);
		}
		else{
			textToSpeech.speak(getString(R.string.key_conf_fail));
			
			Log.getLog().addEntry(KeyConfActivity.TAG,PrefsActivity.configurationToString(this),
					Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),
					"invalid new configuration");
			
			AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.CONFIGURATION_CHANGED, 
					MinesweeperAnalytics.KEY_CONFIGURATION_CHANGED, MinesweeperAnalytics.KEY_CONFIGURATION_FAILS, 0);
		}
		

	}

	private String keyConfigurationtoString() {
		String aux;
		aux = "Exploration: " + keyboard.getKeyByAction("exploration");
		aux = "Zoom: " + aux.concat(keyboard.getKeyByAction("zoom") + keyboard.searchButtonByAction("zoom"));
		aux = "Coordinates: " + aux.concat(keyboard.getKeyByAction("coordinates") + keyboard.searchButtonByAction("coordinates"));
		aux = "Context: " + aux.concat(keyboard.getKeyByAction("context") + keyboard.searchButtonByAction("context"));
		aux = "R: " + aux.concat(keyboard.getKeyByAction("repeat") + keyboard.searchButtonByAction("repeat"));
		return aux;
	}

	/**
	 * DPAD keys always have the same action
	 * @param key
	 * @return
	 */
	private boolean isValid(int key) {	
		return key != keyboard.getKeyByButton("DPAD Center") &&
		key != keyboard.getKeyByButton("DPAD Down") &&
		key != keyboard.getKeyByButton("DPAD Left") &&
		key != keyboard.getKeyByButton("DPAD Right") &&
		key != keyboard.getKeyByButton("DPAD Up") &&
		key != keyboard.getKeyByButton("BACK");
	}

	/**
	 * OnFocusChangeListener Interface
	 * */
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			textToSpeech.speak(v);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Integer key = Input.getKeyboard().getKeyByAction(KeyConfActivity.ACTION_REPEAT);
		if(key != null){
			if (keyCode == key) {
				textToSpeech.repeatSpeak();
				return true;
			} 
		}
		return super.onKeyDown(keyCode, event);
	}
}
