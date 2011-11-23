package org.example.activities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.example.R;
import org.example.golf.XML.KeyboardWriter;
import org.example.golf.XML.XMLKeyboard;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class KeyConfActivity extends Activity{
	
	public static final int KEY_PRESSED = 1;
	
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	
	private TTS textToSpeech;
	
	private String action;
	private int key;
	
	//private Button buttonZoom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getKeyboard();
		
		//buttonZoom = (Button) findViewById(R.id.buttonZoom);
		//buttonZoom.setOnFocusChangeListener(this);
		//buttonZoom.setOnClickListener(this);
		
		this.buttonsUpdate();

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext));
				
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
		//buttonZoom.setText(keyboard.searchButtonByAction("zoom"));
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
	//	case R.id.buttonZoom:
	//		action = "zoom";
	//		break;
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
		//		if (action.equals("zoom")){;
		//			keyboard.addButtonAction(key, "zoom");
		//		}
				break;
			}
			buttonsUpdate();
			this.saveEditedKeyboard("golfGame.xml");
		}
		else{
			Toast toast = Toast.makeText(this, "Not a valid key", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * DPAD keys always have the same action
	 * @param key
	 * @return
	 */
	private boolean isValid(int key) {	
		return false;
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
