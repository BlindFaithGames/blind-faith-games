package com.minesweeper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.example.minesweeper.Input;
import org.example.minesweeper.TTS;
import org.example.minesweeper.XML.KeyboardWriter;
import org.example.minesweeper.XML.XMLKeyboard;
import org.example.others.AnalyticsManager;
import org.example.others.Log;
import org.example.others.MinesweeperAnalytics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class KeyConfActivity extends Activity implements OnFocusChangeListener, OnClickListener {
	
	private static final String TAG = "keyConfiguration";
	
	public static final int KEY_PRESSED = 1;
	
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	private TTS textToSpeech;
	private String action;
	private int key;
	private Button buttonZoom, buttonInstructions, buttonExploration, buttonCoordinates, buttonContext;

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
		
		this.buttonsUpdate();

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
				+ buttonZoom.getContentDescription() + " "
				+ buttonExploration.getContentDescription() + " "
				+ buttonInstructions.getContentDescription() + " "
				+ buttonCoordinates.getContentDescription() + " "
				+ buttonContext.getContentDescription());
		
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
		}
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
				break;
			}
			buttonsUpdate();
			this.saveEditedKeyboard("minesweeper.xml");
			Log.getLog().addEntry(KeyConfActivity.TAG,PrefsActivity.configurationToString(this),
					Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),
					"New configuration: " + keyConfigurationtoString());
			
			AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.CONFIGURATION_CHANGED,
						MinesweeperAnalytics.KEY_CONFIGURATION_CHANGED,  MinesweeperAnalytics.keyConfigurationSuccess + keyConfigurationtoString(), 3);
		}
		else{
			Toast toast = Toast.makeText(this, "Not a valid key", Toast.LENGTH_SHORT);
			toast.show();
			Log.getLog().addEntry(KeyConfActivity.TAG,PrefsActivity.configurationToString(this),
					Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),
					"invalid new configuration");
			
			AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.CONFIGURATION_CHANGED, 
					MinesweeperAnalytics.KEY_CONFIGURATION_CHANGED, MinesweeperAnalytics.keyConfigurationFails, 3);
		}
		

	}

	private String keyConfigurationtoString() {
		String aux;
		aux = "Exploration: " + keyboard.getKeyByAction("exploration");
		aux = "Zoom: " + aux.concat(keyboard.getKeyByAction("zoom") + keyboard.searchButtonByAction("zoom"));
		aux = "Coordinates: " + aux.concat(keyboard.getKeyByAction("coordinates") + keyboard.searchButtonByAction("coordinates"));
		aux = "Context: " + aux.concat(keyboard.getKeyByAction("context") + keyboard.searchButtonByAction("context"));
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
