package com.zarodnik.activities;


import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.accgames.sound.Music;
import com.accgames.sound.SubtitleInfo;
import com.accgames.sound.TTS;
import com.zarodnik.R;
import com.zarodnik.game.ZarodnikMusicSources;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	private CheckBoxPreference music, tts, transcription;

	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = false;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	private static final String OPT_TRANSCRIPTION = "transcription";
	private static final boolean OPT_TRANSCRIPTION_DEF = true;
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

		transcription = (CheckBoxPreference) findPreference(OPT_TRANSCRIPTION);
		transcription.setOnPreferenceClickListener(this);
		
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

	/** Get the current value of the tts option */
	public static boolean getTranscription(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_TRANSCRIPTION, OPT_TRANSCRIPTION_DEF);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (OPT_MUSIC.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_MUSIC).toString()+ " "
					+ music.isChecked());
		} else if (OPT_TTS.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_TTS).toString() + " "
					+ tts.isChecked());
		} else if (OPT_TRANSCRIPTION.equals(preference.getKey())) {
			if(transcription.isChecked()){
				Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);
				
				SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
						R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
				
				Music.getInstanceMusic().enableTranscription(this, s);
				textToSpeech.enableTranscription(s);
			}
			textToSpeech.speak(findPreference(OPT_TRANSCRIPTION).toString()+ " "
					+ transcription.isChecked());
		}
		return true;
	}
	
}