package com.juego4.activities;

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

import com.accgames.input.Input;
import com.accgames.input.KeyboardWriter;
import com.accgames.input.XMLKeyboard;
import com.accgames.sound.TTS;
import com.juego4.R;


public class KeyConfActivity extends Activity implements OnFocusChangeListener, OnClickListener, OnLongClickListener {
	
	public static final int KEY_PRESSED = 1;

	public static final String ACTION_BLIND_MODE = "blindMode";
	public static final String ACTION_REPEAT = "repeat";
	
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	
	private TTS textToSpeech;
	
	private String action;
	private int key;
	
	private Button buttonBlindMode, buttonRepeat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getKeyboard();
		
		TableRow tr;
		
		tr = (TableRow) findViewById(R.id.blind_mode_row);
		tr.setOnClickListener(this);
		
		tr = (TableRow) findViewById(R.id.repeat_row);
		tr.setOnClickListener(this);
		
		buttonBlindMode = (Button) findViewById(R.id.buttonBlindMode);
		buttonBlindMode.setOnFocusChangeListener(this);
		buttonBlindMode.setOnClickListener(this);
		buttonRepeat.setOnLongClickListener(this);
		
		buttonRepeat = (Button) findViewById(R.id.buttonRepeat);
		buttonRepeat.setOnFocusChangeListener(this);
		buttonRepeat.setOnClickListener(this);
		buttonRepeat.setOnLongClickListener(this);
		
		this.buttonsUpdate();

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
										+ buttonBlindMode.getContentDescription() + " "
										+ buttonRepeat.getContentDescription() + " ");
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
		buttonBlindMode.setText(keyboard.searchButtonByAction(ACTION_BLIND_MODE));
		buttonRepeat.setText(keyboard.searchButtonByAction(ACTION_REPEAT));
	}
	
	/**
	 * Save edited keyboard configuration
	 * @throws ParserConfigurationException 
	 */
	public void saveEditedKeyboard(String file){
		// Si el writer no ha sido a�n creado, lo creamos
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
		if(!SettingsActivity.getBlindInteraction(this)){
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
						textToSpeech.speak(v.getContentDescription().toString() + getString(R.string.infoKeyConf) +" " + res);
					else
						textToSpeech.speak(v.getContentDescription().toString() + getString(R.string.infoKeyConffail));
					}
				else
					textToSpeech.speak(v);
			}
		}
	}

	private void menuAction(View v) {
		Intent intent = new Intent(this, CheckKeyActivity.class);
		intent.putExtra(MainActivity.KEY_TTS, textToSpeech);
		switch (v.getId()) {
		case R.id.buttonBlindMode:
			action = ACTION_BLIND_MODE;
			break;
		case R.id.buttonRepeat:
			action = ACTION_REPEAT;
			break;
		default: 
			textToSpeech.speak(v);
			return;
		}
		startActivityForResult(intent, KEY_PRESSED);
	}

	@Override
	public boolean onLongClick(View v) {
		if(SettingsActivity.getBlindInteraction(this)){
			menuAction(v);
			return true;
		}else
			return false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle extras = data.getExtras();
		key = extras.getInt(CheckKeyActivity.KEY_CODE);
		if (isValid(key)){
			switch (resultCode) {
			case (KEY_PRESSED):
				if (action.equals(ACTION_BLIND_MODE)){;
					keyboard.addButtonAction(key, ACTION_BLIND_MODE);
				}
				if (action.equals(ACTION_REPEAT)){;
					keyboard.addButtonAction(key, ACTION_REPEAT);
			}
				break;
			}
			buttonsUpdate();
			textToSpeech.speak(getString(R.string.key_conf_success) + " " + keyboard.searchButtonByAction(action));
			this.saveEditedKeyboard(getString(R.string.app_name)+".xml");
		}
		else{
			
			textToSpeech.speak(getString(R.string.key_conf_fail));
		}
	}

	/**
	 * DPAD keys always have the same action
	 * @param key
	 * @return
	 */
	private boolean isValid(int key) {	
		return key != keyboard.getKeyByButton("Volume Up") &&
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
