package com.zarodnik.activities;

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

import com.accgames.input.Input;
import com.accgames.input.KeyboardWriter;
import com.accgames.input.XMLKeyboard;
import com.accgames.sound.TTS;
import com.zarodnik.R;


public class KeyConfActivity extends Activity implements OnFocusChangeListener, OnClickListener {
	
	public static final int KEY_PRESSED = 1;

	public static final String ACTION_RECORD = "speakRecord";
	public static final String ACTION_BLIND_MODE = "blindMode";
	
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	
	private TTS textToSpeech;
	
	private String action;
	private int key;
	
	private Button buttonRecord, buttonBlindMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getKeyboard();
		
		buttonRecord = (Button) findViewById(R.id.buttonRecord);
		buttonRecord.setOnFocusChangeListener(this);
		buttonRecord.setOnClickListener(this);
		
		buttonBlindMode = (Button) findViewById(R.id.buttonBlindMode);
		buttonBlindMode.setOnFocusChangeListener(this);
		buttonBlindMode.setOnClickListener(this);
		
		this.buttonsUpdate();

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
										+ buttonRecord.getContentDescription() + " "
										+ buttonBlindMode.getContentDescription() + " ");
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
		buttonBlindMode.setText(keyboard.searchButtonByAction(ACTION_BLIND_MODE));
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
		case R.id.buttonBlindMode:
			action = ACTION_BLIND_MODE;
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
				if (action.equals(ACTION_RECORD)){;
					keyboard.addButtonAction(key, ACTION_RECORD);
				}
				if (action.equals(ACTION_BLIND_MODE)){;
					keyboard.addButtonAction(key, ACTION_BLIND_MODE);
				}
				break;
			}
			buttonsUpdate();
			this.saveEditedKeyboard(getString(R.string.app_name)+".xml");
		}
		else{
			Toast toast = Toast.makeText(this, R.string.key_conf_fail, Toast.LENGTH_SHORT);
			textToSpeech.speak(getString(R.string.key_conf_fail));
			toast.show();
		}
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
