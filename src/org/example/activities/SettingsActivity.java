package org.example.activities;

import org.example.R;
import org.example.tinyEngineClasses.TTS;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity{

	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = false;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	private static final String OPT_INFO_TARGET = "infoTarget";
	private static final boolean OPT_INFO_TARGET_DEF = false;

	private TTS textToSpeech;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(findPreference(OPT_MUSIC).toString() + 
									  findPreference(OPT_TTS).toString() + 
									  findPreference(OPT_INFO_TARGET).toString());
	}

	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    textToSpeech.stop();
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

	public static boolean getNotifyTarget(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_INFO_TARGET, OPT_INFO_TARGET_DEF);
	}
}
