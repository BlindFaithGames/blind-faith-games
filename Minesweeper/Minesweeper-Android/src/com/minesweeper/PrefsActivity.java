package com.minesweeper;



import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.accgames.others.AnalyticsManager;
import com.accgames.others.Log;
import com.minesweeper.game.MinesweeperAnalytics;
import com.minesweeper.game.SubtitleInfo;
import com.minesweeper.game.TTS;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the preferences activity, where you can choose whether disable or enable sounds
 */

public class PrefsActivity extends PreferenceActivity implements OnPreferenceClickListener {
	private static String TAG = "SettingsMenu";
	
	private CheckBoxPreference music, tts, contextCell, transcription, blindInteraction;
	
	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = false;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	private static final String OPT_COORDINATES = "context";
	private static final boolean OPT_COORDINATES_DEF = true;
	private static final String OPT_TRANSCRIPTION = "transcription";
	private static final boolean OPT_TRANSCRIPTION_DEF = false;
	public static final String FIRSTRUN = "first";
	public static final boolean FIRSTRUN_DEF = true;
	public static final String OPT_BLIND_INTERACTION = "interaction";
	private static final boolean OPT_BLIND_INTERACTION_DEF = true;

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

		contextCell = (CheckBoxPreference) findPreference(OPT_COORDINATES);
		contextCell.setOnPreferenceClickListener(this);
		
		transcription = (CheckBoxPreference) findPreference(OPT_TRANSCRIPTION);
		transcription.setOnPreferenceClickListener(this);
		
		blindInteraction = (CheckBoxPreference) findPreference(OPT_BLIND_INTERACTION);
		blindInteraction.setOnPreferenceClickListener(this);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.settings_menu_initial_TTStext));
	
		Log.getLog().addEntry(PrefsActivity.TAG,
				configurationToString(this),
				Log.NONE,
				Thread.currentThread().getStackTrace()[2].getMethodName(),
				"Changing actual configuration");
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(MinesweeperAnalytics.PREFS_ACTIVITY);
	}
	
	protected void onPause() {
		super.onPause();
		AnalyticsManager.getAnalyticsManager(this).registerAction(MinesweeperAnalytics.CONFIGURATION_CHANGED,
				MinesweeperAnalytics.GENERAL_CONFIGURATION_CHANGED, configurationToString(this), 12);
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
	
	/** Get the current value of the blind interaction option */
	public static boolean getBlindInteraction(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_BLIND_INTERACTION, OPT_BLIND_INTERACTION_DEF);
	}
	
	/** Get the current value of the context option */
	public static boolean getCoordinates(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_COORDINATES, OPT_COORDINATES_DEF);
	}
	
	/** Get the current value of the transcription option */
	public static boolean getTranscription(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_TRANSCRIPTION, OPT_TRANSCRIPTION_DEF);
	}
	
	/** Get the current value of the first run option */
	public static boolean getFirstRun(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(FIRSTRUN, FIRSTRUN_DEF);
	}
	
	public static String configurationToString(Context context){
		return	"Music: " + PrefsActivity.getMusic(context) + "/" +
				" TTS: " + PrefsActivity.getTTS(context) + "/" +
				" Context Coordinates: "+ PrefsActivity.getCoordinates(context) + "/" +
				" Transcription " + PrefsActivity.getTranscription(context);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (OPT_MUSIC.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_MUSIC).toString() + " "
					+ music.isChecked());
		} else if (OPT_TTS.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_TTS).toString() + " "
					+ tts.isChecked());
		} else if (OPT_COORDINATES.equals(preference.getKey())) {
			textToSpeech.speak(findPreference(OPT_COORDINATES).toString() + " "
					+ contextCell.isChecked());
		}else if (OPT_BLIND_INTERACTION.equals(preference.getKey())){
			textToSpeech.speak(findPreference(OPT_BLIND_INTERACTION).toString() + " "
					+ blindInteraction.isChecked());
		} else if (OPT_TRANSCRIPTION.equals(preference.getKey())){
			if(transcription.isChecked()){
				SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
						R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, null);
				textToSpeech.enableTranscription(s);
			}else{
				textToSpeech.disableTranscription();
			}
			textToSpeech.speak(findPreference(OPT_TRANSCRIPTION).toString() + " "
					+ transcription.isChecked());
		}
		return true;
	}
}