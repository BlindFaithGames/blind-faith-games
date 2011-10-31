package org.example.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

public class Instructions extends Activity{
	
	private TextToSpeech mTts;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions);
		// This initialize TTS engine
		// Checking if TTS is installed on device
		OnInitTTS initialize = new OnInitTTS(mTts,getString(R.string.instructions_title) + " " + getString(R.string.instructions_text));
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