package org.example.Activities;

import org.example.R;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.os.Bundle;

public class InstructionsActivity extends Activity{

	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions_general);
		
		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(Game.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.instructions_general_title) + " " + getString(R.string.instructions_general_text));
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
