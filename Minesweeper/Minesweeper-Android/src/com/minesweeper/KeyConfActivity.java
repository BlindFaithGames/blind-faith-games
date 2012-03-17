package com.minesweeper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.accgames.XML.KeyboardWriter;
import com.accgames.XML.XMLKeyboard;
import com.accgames.others.AnalyticsManager;
import com.accgames.others.Log;
import com.minesweeper.R;
import com.minesweeper.game.Input;
import com.minesweeper.game.MinesweeperAnalytics;
import com.minesweeper.game.TTS;


public class KeyConfActivity extends Activity implements OnFocusChangeListener, OnClickListener {
	
	private static final String TAG = "keyConfiguration";
	
	public static final int KEY_PRESSED = 1;
	
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	private TTS textToSpeech;
	private String action;
	private int key;
	private Button buttonZoom, buttonInstructions, buttonExploration;
	private Button buttonCoordinates, buttonContext, buttonBlindMode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getInstance();
		
		buttonZoom = (Button) findViewById(R.id.buttonZoom);
		buttonZoom.setOnFocusChangeListener(this);
		buttonZoom.setOnClickListener(this);
		
		
		buttonExploration = (Button) findViewById(R.id.buttonExploration);
		buttonExploration.setOnFocusChangeListener(this);
		buttonExploration.setOnClickListener(this);

		
		buttonInstructions = (Button) findViewById(R.id.buttonInstructions);
		buttonInstructions.setOnFocusChangeListener(this);
		buttonInstructions.setOnClickListener(this);

		
		buttonCoordinates = (Button) findViewById(R.id.buttonCoordinates);
		buttonCoordinates.setOnFocusChangeListener(this);
		buttonCoordinates.setOnClickListener(this);
		
		buttonContext = (Button) findViewById(R.id.buttonContext);
		buttonContext.setOnFocusChangeListener(this);
		buttonContext.setOnClickListener(this);
		
		buttonBlindMode = (Button) findViewById(R.id.buttonBlindMode);
		buttonBlindMode.setOnFocusChangeListener(this);
		buttonBlindMode.setOnClickListener(this);
		
		this.buttonsUpdate();

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
				+ buttonZoom.getContentDescription() + " "
				+ buttonExploration.getContentDescription() + " "
				+ buttonInstructions.getContentDescription() + " "
				+ buttonCoordinates.getContentDescription() + " "
				+ buttonContext.getContentDescription()
				+ buttonBlindMode.getContentDescription());
		
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
		buttonBlindMode.setText(keyboard.searchButtonByAction("blind_mode"));
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

	public void onClick(View view) {
		Intent intent = new Intent(this, CheckKeyActivity.class);
		intent.putExtra(MinesweeperActivity.KEY_TTS, textToSpeech);
		switch (view.getId()) {
		case R.id.buttonZoom:
			action = "zoom";
			break;
		case R.id.buttonExploration:
			action = "exploration";
			break;
		case R.id.buttonInstructions:
			action = "instructions";
			break;
		case R.id.buttonCoordinates:
			action = "coordinates";
			break;
		case R.id.buttonContext:
			action = "context";
			break;
		case R.id.buttonBlindMode:
			action = "blind_mode";
			break;
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
				if (action.equals("zoom")){;
					keyboard.addButtonAction(key, "zoom");
				}
				else if (action.equals("exploration")){
					keyboard.addButtonAction(key, "exploration");
				}
				else if (action.equals("instructions")){
					keyboard.addButtonAction(key, "instructions");
				}
				else if (action.equals("coordinates")){
					keyboard.addButtonAction(key, "coordinates");
				}
				else if (action.equals("context")){
					keyboard.addButtonAction(key, "context");
				}
				else if (action.equals("blind_mode")){
					keyboard.addButtonAction(key, "blind_mode");
				}
				break;
			}
			buttonsUpdate();
			this.saveEditedKeyboard("minesweeper.xml");
			Log.getLog().addEntry(KeyConfActivity.TAG,PrefsActivity.configurationToString(this),
					Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),
					"New configuration: " + keyConfigurationtoString());
			
			AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.CONFIGURATION_CHANGED,
						MinesweeperAnalytics.KEY_CONFIGURATION_CHANGED,  MinesweeperAnalytics.KEY_CONFIGURATION_SUCCESS + keyConfigurationtoString(), 0);
		}
		else{
			Toast toast = Toast.makeText(this, getString(R.string.key_conf_fail), Toast.LENGTH_SHORT);
			toast.show();
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
		aux = "BlindMode: " + aux.concat(keyboard.getKeyByAction("blind_mode") + keyboard.searchButtonByAction("blind_mode"));
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
	
}
