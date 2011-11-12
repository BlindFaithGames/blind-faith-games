package org.example.minesweeper;

import android.app.Activity;
import android.os.Bundle;

public class InstructionsControls extends Activity{
	
	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions_controls);

		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(Game.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.instructions_controls_title) + " " + getString(R.string.instructions_controls_text));
	}
	
	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    textToSpeech.stop();   
	}
}