package org.example.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the about activity, where is shown a description of minesweeper
 */

public class About extends Activity{

	private TextToSpeech mTts;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);	
		
		// This initialize TTS engine
		// Checking if TTS is installed on device
		OnInitTTS initialize = new OnInitTTS(mTts,getString(R.string.about_title) + " " + getString(R.string.about_text));
		if(OnInitTTS.isInstalled(this)){
			mTts = new TextToSpeech(this,initialize);
			initialize.setmTts(mTts);
		}
	}
	
	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		 super.onDestroy();
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
	}
}