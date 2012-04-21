package com.golfgame.activities;


import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.accgames.input.Input;
import com.accgames.others.AnalyticsManager;
import com.accgames.others.GolfMusicSources;
import com.accgames.sound.Music;
import com.accgames.sound.SubtitleInfo;
import com.accgames.sound.TTS;
import com.golfgame.R;
import com.golfgame.game.GolfGameAnalytics;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	private CheckBoxPreference music, tts, infoTarget, on_up_event,
								vibration_feedback, sound_feedback, 
								profileA, profileB, doppler_effect_feedback, 
								transcription, blindInteraction, blindMode;

	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = false;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	public static final String OPT_INFO_TARGET = "infoTarget";
	private static final boolean OPT_INFO_TARGET_DEF = false;
	public static final String OPT_UP = "On up event";
	private static final boolean OPT_UP_DEF = false;
	public static final String OPT_VIBRATION_FEEDBACK = "vibration feedback";
	private static final boolean OPT_VIBRATION_FEEDBACK_DEF = false;
	public static final String OPT_SOUND_FEEDBACK = "sound feedback";
	private static final boolean OPT_SOUND_FEEDBACK_DEF = false;
	public static final String OPT_SOUND_DOPPLER_EFFECT = "doppler effect";
	private static final boolean OPT_SOUND_DOPPLER_EFFECT_DEF = false;
	public static final String OPT_TRANSCRIPTION = "transcription";
	private static final boolean OPT_TRANSCRIPTION_DEF = true;
	public static final String FIRSTRUN = "first";
	public static final boolean FIRSTRUN_DEF = true;
	public static final String OPT_BLIND_INTERACTION = "interaction";
	private static final boolean OPT_BLIND_INTERACTION_DEF = true;
	public static final String OPT_BLIND_MODE = "blind_mode";
	private static final boolean OPT_BLIND_MODE_DEF = false;
	private static final String OPT_PROFILEA = "profile A";
	private static final String OPT_PROFILEB = "profile B";
	private TTS textToSpeech;

	private static Editor editor;

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
		
		infoTarget = (CheckBoxPreference) findPreference(OPT_INFO_TARGET);
		infoTarget.setOnPreferenceClickListener(this);

		on_up_event = (CheckBoxPreference) findPreference(OPT_UP);
		on_up_event.setOnPreferenceClickListener(this);

		vibration_feedback = (CheckBoxPreference) findPreference(OPT_VIBRATION_FEEDBACK);
		vibration_feedback.setOnPreferenceClickListener(this);

		sound_feedback = (CheckBoxPreference) findPreference(OPT_SOUND_FEEDBACK);
		sound_feedback.setOnPreferenceClickListener(this);
		
		doppler_effect_feedback = (CheckBoxPreference) findPreference(OPT_SOUND_DOPPLER_EFFECT);
		doppler_effect_feedback.setOnPreferenceClickListener(this);
		
		transcription = (CheckBoxPreference) findPreference(OPT_TRANSCRIPTION);
		transcription.setOnPreferenceClickListener(this);
		
		blindMode = (CheckBoxPreference) findPreference(OPT_BLIND_MODE);
		blindMode.setOnPreferenceClickListener(this);
		
		blindInteraction = (CheckBoxPreference) findPreference(OPT_BLIND_INTERACTION);
		blindInteraction.setOnPreferenceClickListener(this);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.settings_menu_initial_TTStext) + ", "
											+ findPreference(OPT_MUSIC).toString() + ", "
											+ findPreference(OPT_TTS).toString() + ", "
											+ findPreference(OPT_TRANSCRIPTION).toString() + ", "
											+ findPreference(OPT_BLIND_INTERACTION).toString() + ", "
											+ findPreference(OPT_BLIND_MODE).toString() + ", "
											+ findPreference(OPT_PROFILEA).toString() + ", "
											+ findPreference(OPT_PROFILEB).toString() + ", "
											+ findPreference(OPT_INFO_TARGET).toString() + ", "
											+ findPreference(OPT_UP).toString() + ", "
											+ findPreference(OPT_VIBRATION_FEEDBACK).toString() + ", "
											+ findPreference(OPT_SOUND_FEEDBACK).toString() + ", "
											+ findPreference(OPT_SOUND_DOPPLER_EFFECT).toString());
		AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.PREFS_ACTIVITY);
	}

	protected void onPause() {
		super.onPause();
		AnalyticsManager.getAnalyticsManager(this).registerAction(GolfGameAnalytics.CONFIGURATION_CHANGED,
				GolfGameAnalytics.GENERAL_CONFIGURATION_CHANGED, configurationToString(this), 12);
	}
	
	public static String configurationToString(Context context){
		return	"Music: " + SettingsActivity.getMusic(context) + "/" +
				" TTS: " + SettingsActivity.getTTS(context) + "/" +
				" Transcription "+ SettingsActivity.getTranscription(context) + "/" +
				" Context Coordinates: "+ SettingsActivity.getDopplerEffect(context) + "/" +
				" Notify target: "+ SettingsActivity.getNotifyTarget(context) + "/" +
				" on Up: "+ SettingsActivity.getOnUp(context) + "/" +
				" Sound Feedback: "+ SettingsActivity.getSoundFeedBack(context) + "/" +
				" Vibration Feedback: "+ SettingsActivity.getVibrationFeedback(context) + "/" +
				" ProfileA: "+ PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_PROFILEA, false) + "/" +
				" ProfileB: "+ PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_PROFILEA, true);
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
	
	/** Get the current value of the transcription option */
	public static boolean getTranscription(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_TRANSCRIPTION, OPT_TRANSCRIPTION_DEF);
	}
	
	/** Get the current value of the blind interaction option */
	public static boolean getBlindInteraction(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_BLIND_INTERACTION, OPT_BLIND_INTERACTION_DEF);
	}
	
	/** Get the current value of the blind interaction option */
	public static boolean getBlindMode(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_BLIND_MODE, OPT_BLIND_MODE_DEF);
	}

	/** Get the current value of the info target option */
	public static boolean getNotifyTarget(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_INFO_TARGET, OPT_INFO_TARGET_DEF);
	}

	/** Get the current value of the on up event option */
	public static boolean getOnUp(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_UP, OPT_UP_DEF);
	}

	/** Get the current value of the vibration feedback option */
	public static boolean getVibrationFeedback(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_VIBRATION_FEEDBACK, OPT_VIBRATION_FEEDBACK_DEF);
	}

	/** Get the current value of the sound feedback option */
	public static boolean getSoundFeedBack(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_SOUND_FEEDBACK, OPT_SOUND_FEEDBACK_DEF);
	}
	
	/** Get the current value of the doppler effect option */
	public static boolean getDopplerEffect(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_SOUND_DOPPLER_EFFECT, OPT_SOUND_DOPPLER_EFFECT_DEF);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (OPT_MUSIC.equals(preference.getKey())) {
			if(music.isChecked())
				textToSpeech.speak(findPreference(OPT_MUSIC).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_MUSIC).toString() + " " + getString(R.string.disabled));
		} else if (OPT_TTS.equals(preference.getKey())) {
			if(tts.isChecked())
				textToSpeech.speak(findPreference(OPT_TTS).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_TTS).toString() + " " + getString(R.string.disabled));
		} else if (OPT_INFO_TARGET.equals(preference.getKey())) {
			if(infoTarget.isChecked())
				textToSpeech.speak(findPreference(OPT_INFO_TARGET).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_INFO_TARGET).toString() + " " + getString(R.string.disabled));
			manageCustomProfile();
		} else if (OPT_UP.equals(preference.getKey())) {
			if(on_up_event.isChecked())
				textToSpeech.speak(findPreference(OPT_UP).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_UP).toString() + " " + getString(R.string.disabled));
			manageCustomProfile();
		} else if (OPT_VIBRATION_FEEDBACK.equals(preference.getKey())) {
			if(vibration_feedback.isChecked())
				textToSpeech.speak(findPreference(OPT_VIBRATION_FEEDBACK).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_VIBRATION_FEEDBACK).toString() + " " + getString(R.string.disabled));
			manageCustomProfile();
		} else if (OPT_SOUND_FEEDBACK.equals(preference.getKey())) {
			if(sound_feedback.isChecked())
				textToSpeech.speak(findPreference(OPT_SOUND_FEEDBACK).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_SOUND_FEEDBACK).toString() + " " + getString(R.string.disabled));
			manageCustomProfile();
		} else if (OPT_SOUND_DOPPLER_EFFECT.equals(preference.getKey())) {
			if(doppler_effect_feedback.isChecked())
				textToSpeech.speak(findPreference(OPT_SOUND_DOPPLER_EFFECT).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_SOUND_DOPPLER_EFFECT).toString() + " " + getString(R.string.disabled));
			manageCustomProfile();
		} else if (OPT_PROFILEA.equals(preference.getKey())) {
			if(profileA.isChecked())
				textToSpeech.speak(findPreference(OPT_PROFILEA).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_PROFILEA).toString() + " " + getString(R.string.disabled));
			manageProfileA();
		} else if (OPT_PROFILEB.equals(preference.getKey())) {
			if(profileB.isChecked())
				textToSpeech.speak(findPreference(OPT_PROFILEB).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_PROFILEB).toString() + " " + getString(R.string.disabled));
			manageProfileB();
		}else if (OPT_BLIND_INTERACTION.equals(preference.getKey())){
			if(blindInteraction.isChecked())
				textToSpeech.speak(findPreference(OPT_BLIND_INTERACTION).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_BLIND_INTERACTION).toString() + " " + getString(R.string.disabled));
		}else if (OPT_BLIND_MODE.equals(preference.getKey())){
			if(blindInteraction.isChecked())
				textToSpeech.speak(findPreference(OPT_BLIND_MODE).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_BLIND_MODE).toString() + " " + getString(R.string.disabled));
		}  else if (OPT_TRANSCRIPTION.equals(preference.getKey())) {
			if(transcription.isChecked()){
				Map<Integer, String> onomatopeias = GolfMusicSources.getMap(this);
				
				SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
						R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
				
				Music.getInstanceMusic().enableTranscription(this, s);
				textToSpeech.enableTranscription(s);
				
				textToSpeech.speak(findPreference(OPT_TRANSCRIPTION).toString()+ " " + getString(R.string.enabled));
			}
			else{
				Music.getInstanceMusic().disableTranscription();
				textToSpeech.disableTranscription();
				
				textToSpeech.speak(findPreference(OPT_TRANSCRIPTION).toString() + " " + getString(R.string.disabled));
			}
		}
		return true;
	}

	private void manageProfileA() {
		 infoTarget.setChecked(profileA.isChecked()); 
		 sound_feedback.setChecked(profileA.isChecked());
		 doppler_effect_feedback.setChecked(profileA.isChecked());
		 
	     profileB.setChecked(false);;
		 on_up_event.setChecked(profileB.isChecked());
		 vibration_feedback.setChecked(profileB.isChecked());
	}
	
	private void manageProfileB() {
		 infoTarget.setChecked(profileB.isChecked());
		 on_up_event.setChecked(profileB.isChecked());
		 vibration_feedback.setChecked(profileB.isChecked());
		 doppler_effect_feedback.setChecked(profileB.isChecked());

		 profileA.setChecked(false);
		 sound_feedback.setChecked(profileA.isChecked());
	}
	
	private void manageCustomProfile() {
		profileA.setChecked(false);
		profileB.setChecked(false);
	}

	public static void setOnUp(boolean b) {
		editor.putBoolean(SettingsActivity.OPT_UP, b);
		editor.commit();
	}
	
	public static void setVibration(boolean b) {
		editor.putBoolean(SettingsActivity.OPT_VIBRATION_FEEDBACK, b);
		editor.commit();
	}
	
	public static void setSoundFeedback(boolean b) {
		editor.putBoolean(SettingsActivity.OPT_SOUND_FEEDBACK, b);
		editor.commit();
	}
	
	public static void setInfoTarget(boolean b) {
		editor.putBoolean(SettingsActivity.OPT_INFO_TARGET, b);
		editor.commit();
	}
	
	public static void setDoppler(boolean b) {
		editor.putBoolean(SettingsActivity.OPT_SOUND_DOPPLER_EFFECT, b);
		editor.commit();
	}
	
	public static void setDefaultTutorialConfig(Editor ed){
		editor = ed;
		setDoppler(false);
		setInfoTarget(false);
		setOnUp(false);
		setSoundFeedback(false);
		setVibration(false);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Integer key = Input.getKeyboard().getKeyByAction(KeyConfActivity.ACTION_REPEAT);
		if(key != null){
			if (keyCode == key) {
				textToSpeech.repeatSpeak();
				return true;
			} 
		}
		return super.onKeyDown(keyCode, event);
	}
}