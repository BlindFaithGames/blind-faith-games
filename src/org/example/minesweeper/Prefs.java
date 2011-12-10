package org.example.minesweeper;

import org.example.others.Log;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the preferences activity, where you can choose whether disable or enable sounds
 */

public class Prefs extends PreferenceActivity {
	private static String TAG = "SettingsMenu";
	
	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = false;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	private static final String OPT_COORDINATES = "context";
	private static final boolean OPT_COORDINATES_DEF = true;

	
	private TTS textToSpeech;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(Game.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(findPreference(OPT_MUSIC).toString() + findPreference(OPT_TTS).toString());
	
		Log.getLog().addEntry(Prefs.TAG,
				configurationToString(this),
				Log.NONE,
				Thread.currentThread().getStackTrace()[2].getMethodName(),
				"Changing actual configuration");
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
	
	/** Get the current value of the tts option */
	public static boolean getCoordinates(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_COORDINATES, OPT_COORDINATES_DEF);
	}
	
	public static String configurationToString(Context context){
		return	"Actual Configuration Music: " + Prefs.getMusic(context) +
				" TTS: " + Prefs.getTTS(context)+
				" Context Coordinates: "+ Prefs.getCoordinates(context);
	}
}