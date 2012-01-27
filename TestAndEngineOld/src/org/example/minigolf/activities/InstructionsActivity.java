package org.example.minigolf.activities;



import org.example.minigolf.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InstructionsActivity extends Activity{

	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions_general);
		
		
		Intent i = getIntent();
		
		int type = i.getIntExtra(MainActivity.KEY_TYPE_INSTRUCTIONS, 0);
		
		String speech;
		
		if(type == 0)
			speech = getString(R.string.instructions_controls_label) + " " + i.getStringExtra(MainActivity.KEY_INSTRUCTIONS_CONTROLS);
		else
			speech = getString(R.string.instructions_general_label) + " " + i.getStringExtra(MainActivity.KEY_INSTRUCTIONS_GENERAL);
		
		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(speech);
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
