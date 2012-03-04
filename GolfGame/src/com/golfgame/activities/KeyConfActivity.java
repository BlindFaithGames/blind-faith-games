package com.golfgame.activities;

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

import com.accgames.others.AnalyticsManager;
import com.accgames.tinyengine.Input;
import com.accgames.tinyengine.TTS;
import com.accgames.xml.KeyboardWriter;
import com.accgames.xml.XMLKeyboard;
import com.golfgame.R;
import com.golfgame.game.GolfGameAnalytics;


public class KeyConfActivity extends Activity implements OnFocusChangeListener, OnClickListener {
	
	public static final int KEY_PRESSED = 1;

	public static final String ACTION_RECORD = "speakRecord";
	
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	
	private TTS textToSpeech;
	
	private String action;
	private int key;
	
	private Button buttonRecord;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getKeyboard();
		
		buttonRecord = (Button) findViewById(R.id.buttonRecord);
		buttonRecord.setOnFocusChangeListener(this);
		buttonRecord.setOnClickListener(this);
		
		this.buttonsUpdate();

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
										+ buttonRecord.getContentDescription() + " ");
		AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.KEY_CONF_ACTIVITY);
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
		buttonRecord.setText(keyboard.searchButtonByAction(ACTION_RECORD));
	}
	
	/**
	 * Save edited keyboard configuration
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
		intent.putExtra(MainActivity.KEY_TTS, textToSpeech);
		switch (view.getId()) {
		case R.id.buttonRecord:
			action = ACTION_RECORD;
			break;
		}
		AnalyticsManager.getAnalyticsManager().registerAction(GolfGameAnalytics.CONFIGURATION_CHANGED,
				GolfGameAnalytics.KEY_CONFIGURATION_CHANGED, "Action-Key Changed: " + action, 0);
		startActivityForResult(intent, KEY_PRESSED);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle extras = data.getExtras();
		key = extras.getInt(CheckKeyActivity.KEY_CODE);
		if (isValid(key)){
			switch (resultCode) {
			case (KEY_PRESSED):
				if (action.equals(ACTION_RECORD)){;
					keyboard.addButtonAction(key, ACTION_RECORD);
				}
				break;
			}
			buttonsUpdate();
			this.saveEditedKeyboard(getString(R.string.app_name)+".xml");
			AnalyticsManager.getAnalyticsManager(this).registerAction(GolfGameAnalytics.CONFIGURATION_CHANGED,
					GolfGameAnalytics.KEY_CONFIGURATION_CHANGED,  GolfGameAnalytics.KEY_CONFIGURATION_SUCCESS + keyConfigurationtoString(), 0);
		}
		else{
			Toast toast = Toast.makeText(this, getString(R.string.key_conf_fail), Toast.LENGTH_SHORT);
			toast.show();
			AnalyticsManager.getAnalyticsManager(this).registerAction(GolfGameAnalytics.CONFIGURATION_CHANGED, 
					GolfGameAnalytics.KEY_CONFIGURATION_CHANGED, GolfGameAnalytics.KEY_CONFIGURATION_FAILS, 0);
		}
	}

	private String keyConfigurationtoString() {
		String aux;
		aux = ACTION_RECORD + ": " + keyboard.getKeyByAction(ACTION_RECORD);
		return aux;
	}

	
	/**
	 * DPAD keys always have the same action
	 * @param key
	 * @return
	 */
	private boolean isValid(int key) {	
		return key != keyboard.getKeyByButton("Search") &&
			   key != keyboard.getKeyByButton("Volume Up") &&
			   key != keyboard.getKeyByButton("Volume Down") &&
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
