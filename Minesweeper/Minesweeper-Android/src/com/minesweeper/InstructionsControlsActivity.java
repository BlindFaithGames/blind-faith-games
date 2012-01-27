package com.minesweeper;

import org.example.minesweeper.TTS;
import org.example.others.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class InstructionsControlsActivity extends Activity{
	
	private static String TAG = "InstructionsControls";
	
	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.instructions_controls);

		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(GameActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.instructions_controls_title) + " " + getString(R.string.instructions_controls_text));
	
		Log.getLog().addEntry(InstructionsControlsActivity.TAG,PrefsActivity.configurationToString(this),
				Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),"");
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