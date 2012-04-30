package com.golfgame.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.accgames.feedback.AnalyticsManager;
import com.accgames.input.Input;
import com.accgames.sound.TTS;
import com.golfgame.R;
import com.golfgame.game.GolfGameAnalytics;

public class AboutActivity extends Activity{

	private TTS textToSpeech;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		if(!SettingsActivity.getBlindMode(this)){
//			setTheme(android.R.style.Theme_Dialog);
			super.onCreate(savedInstanceState);
			setContentScreen();
		}else{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.empty);
		}
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.about_title) + " " + getString(R.string.about_text));
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.ABOUT_ACTIVITY);
	}
	
	private void setContentScreen() {
//		float fontSize;
//		float scale;
//		Typeface font;
//		TextView t;
//		View v;
		
		setContentView(R.layout.about);
		
		//scale = this.getResources().getDisplayMetrics().density;
		//fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		//font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		
		//t = (TextView) findViewById(R.id.about_content);
		//t.setTextSize(fontSize);
		//t.setTypeface(font);
		
//		v = findViewById(R.id.about_root);
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
