package com.minesweeper;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.accgames.others.AnalyticsManager;
import com.accgames.others.Log;
import com.minesweeper.R;
import com.minesweeper.game.MinesweeperAnalytics;
import com.minesweeper.game.TTS;

public class InstructionsControlsActivity extends Activity{
	
	private static String TAG = "InstructionsControls";
	
	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.instructions_controls);

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
}