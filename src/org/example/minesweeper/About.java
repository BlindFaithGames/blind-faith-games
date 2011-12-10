package org.example.minesweeper;

import org.example.others.Log;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the about activity, where is shown a description of minesweeper
 */

public class About extends Activity{

	private static String TAG = "About";
	
	private TTS textToSpeech;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);	
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(Game.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.about_title) + " " + getString(R.string.about_text));
	
		Log.getLog().addEntry(About.TAG,
				Prefs.configurationToString(this),
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