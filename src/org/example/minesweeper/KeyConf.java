package org.example.minesweeper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.example.minesweeper.XML.KeyboardWriter;
import org.example.minesweeper.XML.XMLKeyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;


public class KeyConf extends Activity implements OnFocusChangeListener, OnClickListener {
	public static final int KEY_PRESSED = 1;
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	private TTS textToSpeech;
	private String buttonName, action;
	private int key;
	private Button buttonZoom, buttonInstructions, buttonExploration, buttonCoordinates;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getInstance();
		
		buttonZoom = (Button) findViewById(R.id.buttonZoom);
		buttonZoom.setText(keyboard.searchButtonByAction("zoom"));
		buttonZoom.setOnFocusChangeListener(this);
		buttonZoom.setOnClickListener(this);
		
		
		buttonExploration = (Button) findViewById(R.id.buttonExploration);
		buttonExploration.setText(keyboard.searchButtonByAction("exploration"));
		buttonExploration.setOnFocusChangeListener(this);
		buttonExploration.setOnClickListener(this);

		
		buttonInstructions = (Button) findViewById(R.id.buttonInstructions);
		buttonInstructions.setText(keyboard.searchButtonByAction("instructions"));
		buttonInstructions.setOnFocusChangeListener(this);
		buttonInstructions.setOnClickListener(this);

		
		buttonCoordinates = (Button) findViewById(R.id.buttonCoordinates);
		buttonCoordinates.setText(keyboard.searchButtonByAction("coordinates"));
		buttonCoordinates.setOnFocusChangeListener(this);
		buttonCoordinates.setOnClickListener(this);

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(Game.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
				+ buttonZoom.getContentDescription() + " "
				+ buttonExploration.getContentDescription() + " "
				+ buttonInstructions.getContentDescription() + " "
				+ buttonCoordinates.getContentDescription());
	}
	
	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		 super.onDestroy();
	     textToSpeech.stop();
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
		Intent intent = new Intent(this, CheckKey.class);
		intent.putExtra(Game.KEY_TTS, textToSpeech);
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
		}
		startActivityForResult(intent, KEY_PRESSED);

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle extras = data.getExtras();
		key = extras.getInt(CheckKey.KEY_CODE);
		buttonName = extras.getString(CheckKey.KEY_NAME);
		
		switch (resultCode) {
		case (KEY_PRESSED):
			if (action.equals("zoom")){
				buttonZoom.setText(buttonName);
				keyboard.addButtonAction(key, "zoom", buttonName);
			}
			else if (action.equals("exploration")){
				buttonExploration.setText(buttonName);
				keyboard.addButtonAction(key, "exploration", buttonName);
			}
			else if (action.equals("instructions")){
				buttonInstructions.setText(buttonName);
				keyboard.addButtonAction(key, "instructions", buttonName);
			}
			else if (action.equals("coordinates")){
				buttonCoordinates.setText(buttonName);
				keyboard.addButtonAction(key, "coordinates", buttonName);

			}
			break;
		}
		this.saveEditedKeyboard("minesweeper.xml");
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
