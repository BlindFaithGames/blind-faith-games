package org.example.minesweeper;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.example.minesweeper.XML.KeyboardWriter;
import org.example.minesweeper.XML.XMLKeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;


public class KeyConf extends Activity implements OnKeyListener, OnFocusChangeListener, OnClickListener {
	private KeyboardWriter writer;
	private HashMap<Integer, String> keyList;
	private XMLKeyboard keyboard;
	private TTS textToSpeech;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyconf);	
		
		keyboard = Input.getInstance();
		
		Button buttonZoom = (Button) findViewById(R.id.buttonZoom);
		buttonZoom.setText(keyboard.searchButtonByAction("zoom"));
		buttonZoom.setOnFocusChangeListener(this);
		buttonZoom.setOnClickListener(this);
		
		
		Button buttonExploration = (Button) findViewById(R.id.buttonExploration);
		System.out.println(keyboard.searchButtonByAction("exploration"));
		buttonExploration.setText(keyboard.searchButtonByAction("exploration"));
		buttonExploration.setOnFocusChangeListener(this);
		buttonExploration.setOnClickListener(this);

		
		Button buttonInstructions = (Button) findViewById(R.id.buttonInstructions);
		buttonInstructions.setText(keyboard.searchButtonByAction("instructions"));
		buttonInstructions.setOnFocusChangeListener(this);
		buttonInstructions.setOnClickListener(this);

		
		Button buttonCoordinates = (Button) findViewById(R.id.buttonCoordinates);
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
		if (writer == null)
			writer = new KeyboardWriter();
//		try{
//			writer.saveEditedKeyboard(4, keyList, "data/minesweeper.xml");
//		}
//		catch(ParserConfigurationException e){}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
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
