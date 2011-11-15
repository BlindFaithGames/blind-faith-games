package org.example.minesweeper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.example.minesweeper.XML.KeyboardWriter;
import org.example.minesweeper.XML.XMLKeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;


public class KeyConf extends Activity implements OnFocusChangeListener, OnClickListener {
	private KeyboardWriter writer;
	private XMLKeyboard keyboard;
	private TTS textToSpeech;
	private AlertDialog dialog;
	private String buttonName;
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
//		textToSpeech = (TTS) getIntent().getParcelableExtra(Game.KEY_TTS);
//		textToSpeech.setContext(this);
//		textToSpeech.setInitialSpeech(getString(R.string.key_configuration_menu_initial_TTStext)
//				+ buttonZoom.getContentDescription() + " "
//				+ buttonExploration.getContentDescription() + " "
//				+ buttonInstructions.getContentDescription() + " "
//				+ buttonCoordinates.getContentDescription());
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
	public void saveEditedKeyboard(String fichero){
		// Si el writer no ha sido aún creado, lo creamos
		if (writer == null) writer = new KeyboardWriter();
		try {
			FileOutputStream fos = openFileOutput("minesweeper.xml", 3);
			writer.saveEditedKeyboard(keyboard.getNum(), keyboard.getKeyList(), fos);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		buildDialog();
//		buttonName = KeyEvent.keyCodeToString(key);
		switch (view.getId()) {
		case R.id.buttonZoom:
			buttonZoom.setText(buttonName);
			keyboard.addButtonAction(key, "zoom", buttonName);
			break;
		case R.id.buttonExploration:
			buttonExploration.setText(buttonName);
			keyboard.addButtonAction(key, "exploration", buttonName);
			break;
		case R.id.buttonInstructions:
			buttonInstructions.setText(buttonName);
			keyboard.addButtonAction(key, "instructions", buttonName);
			break;
		case R.id.buttonCoordinates:
			buttonCoordinates.setText(buttonName);
			keyboard.addButtonAction(key, "coordinates", buttonName);
			break;
		}
	}
	
	/**
	 * Builds the dialog shown at the end of the game, when the result is positive
	 */
	private void buildDialog() {
		dialog = new AlertDialog.Builder(this)
				.setTitle("Action button")
				.setMessage("Select any button.")
				.setOnKeyListener(
						new DialogInterface.OnKeyListener() {
							@Override
							public boolean onKey(DialogInterface dialog,int keyCode, KeyEvent event) {
								if (event.getAction() == KeyEvent.ACTION_DOWN){
									key = keyCode;
								}
								return true;
							}
					
						}
				).create();
						
	}

	/**
	 * OnFocusChangeListener Interface
	 * */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
//		if (hasFocus) {
//			textToSpeech.speak(v);
//		}
	}
	
}
