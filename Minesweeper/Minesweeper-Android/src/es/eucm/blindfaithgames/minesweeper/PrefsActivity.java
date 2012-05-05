package es.eucm.blindfaithgames.minesweeper;



import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;
import es.eucm.blindfaithgames.engine.feedback.AnalyticsManager;
import es.eucm.blindfaithgames.engine.feedback.Log;
import es.eucm.blindfaithgames.engine.input.Input;
import es.eucm.blindfaithgames.engine.sound.SubtitleInfo;
import es.eucm.blindfaithgames.engine.sound.TTS;
import es.eucm.blindfaithgames.minesweeper.game.MinesweeperAnalytics;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier �?lvarez
 * This class implements the preferences activity, where you can choose whether disable or enable sounds
 */

public class PrefsActivity extends PreferenceActivity implements OnPreferenceClickListener {
	private static String TAG = "SettingsMenu";
	
	private CheckBoxPreference music, tts, transcription, blindInteraction, blindMode;
	
	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = true;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	private static final String OPT_TRANSCRIPTION = "transcription";
	private static final boolean OPT_TRANSCRIPTION_DEF = true;
	public static final String FIRSTRUN = "first";
	public static final boolean FIRSTRUN_DEF = true;
	public static final String OPT_BLIND_INTERACTION = "interaction";
	private static final boolean OPT_BLIND_INTERACTION_DEF = true;
	public static final String OPT_BLIND_MODE = "blind_mode";
	private static final boolean OPT_BLIND_MODE_DEF = false;

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
		
		blindInteraction = (CheckBoxPreference) findPreference(OPT_BLIND_INTERACTION);
		blindInteraction.setOnPreferenceClickListener(this);
		
		blindMode = (CheckBoxPreference) findPreference(OPT_BLIND_MODE);
		blindMode.setOnPreferenceClickListener(this);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.settings_menu_initial_TTStext) + ", "
				+ findPreference(OPT_MUSIC).toString() + ", "
				+ findPreference(OPT_TTS).toString() + ", "
				+ findPreference(OPT_TRANSCRIPTION).toString() + ", "
				+ findPreference(OPT_BLIND_INTERACTION).toString() + ", "
				+ findPreference(OPT_BLIND_MODE).toString());
	
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
	
	/** Get the current value of the blind interaction option */
	public static boolean getBlindMode(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_BLIND_MODE, OPT_BLIND_MODE_DEF);
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
				" Transcription " + PrefsActivity.getTranscription(context);
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
		} else if (OPT_BLIND_INTERACTION.equals(preference.getKey())){
			if(blindInteraction.isChecked())
				textToSpeech.speak(findPreference(OPT_BLIND_INTERACTION).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_BLIND_INTERACTION).toString() + " " + getString(R.string.disabled));
			
		} else if (OPT_BLIND_MODE.equals(preference.getKey())){
			if(blindInteraction.isChecked())
				textToSpeech.speak(findPreference(OPT_BLIND_MODE).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_BLIND_MODE).toString() + " " + getString(R.string.disabled));
			
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