package org.example.minesweeper;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the preferences activity, where you can choose whether disable or enable sounds
 */

public class Prefs extends PreferenceActivity {
	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = false;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	
	private TextToSpeech mTts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		// This initialize TTS engine
		// Checking if TTS is installed on device
		OnInitTTS initialize = new OnInitTTS(mTts,findPreference(OPT_MUSIC).toString() + findPreference(OPT_TTS).toString());
		if(OnInitTTS.isInstalled(this) && getTTS(this)){
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
	
	/** Get the current value of the music option */
	public static boolean getMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
	}
	
	/** Get the current value of the tts option */
	public static boolean getTTS(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_TTS, OPT_TTS_DEF);
	}
}