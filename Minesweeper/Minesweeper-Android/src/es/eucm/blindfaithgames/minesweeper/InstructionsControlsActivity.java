package es.eucm.blindfaithgames.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.accgames.feedback.AnalyticsManager;
import com.accgames.feedback.Log;
import com.accgames.input.Input;
import com.accgames.sound.TTS;

import es.eucm.blindfaithgames.minesweeper.game.MinesweeperAnalytics;
import es.eucm.blindfaithgames.minesweeper.others.CustomView;

public class InstructionsControlsActivity extends Activity{
	
	private static String TAG = "InstructionsControls";
	
	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {

		if(!PrefsActivity.getBlindMode(this)){
			setTheme(android.R.style.Theme_Dialog);
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.instructions_controls);
		}else{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(new CustomView(this));
		}

		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MinesweeperActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.instructions_controls_title) + " " + getString(R.string.instructions_controls_text));
	
		Log.getLog().addEntry(InstructionsControlsActivity.TAG,PrefsActivity.configurationToString(this),
				Log.NONE,Thread.currentThread().getStackTrace()[2].getMethodName(),"");
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(MinesweeperAnalytics.INSTRUCTIONS_CONTROLS_ACTIVITY);
	}
	
	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    textToSpeech.stop();   
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