package org.example.activities;

import org.example.R;

import org.example.tinyEngineClasses.TTS;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	private CheckBoxPreference music, tts, profileA, profileB;

	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = false;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	private static final String OPT_PROFILEA = "profile A";
	private static final String OPT_PROFILEB = "profile B";
	private TTS textToSpeech;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		// Get the checkbox preference
		music = (CheckBoxPreference) findPreference(OPT_MUSIC);
		music.setOnPreferenceClickListener(this);

		tts = (CheckBoxPreference) findPreference(OPT_TTS);
		tts.setOnPreferenceClickListener(this);

		profileA = (CheckBoxPreference) findPreference(OPT_PROFILEA);
		profileA.setOnPreferenceClickListener(this);
		profileB = (CheckBoxPreference) findPreference(OPT_PROFILEB);
		profileB.setOnPreferenceClickListener(this);
		

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech("Click any option");
	}

	/**
	 * Turns off TTS engine
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


	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (OPT_MUSIC.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_MUSIC).toString()
					+ music.isChecked());
		} else if (OPT_TTS.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_TTS).toString()
					+ tts.isChecked());
		} else if (OPT_PROFILEA.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_PROFILEA).toString()
					+ profileA.isChecked());
			manageProfileA();
		} else if (OPT_PROFILEB.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_PROFILEB).toString()
					+ profileB.isChecked());
			manageProfileB();
		}
		return true;
	}

	private void manageProfileA() {
		profileB.setChecked(false);;

	}
	
	private void manageProfileB() {

		 profileA.setChecked(false);

	}
	
}