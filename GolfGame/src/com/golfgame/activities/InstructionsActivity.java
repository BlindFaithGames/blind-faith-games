package com.golfgame.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.TextView;

import com.accgames.others.AnalyticsManager;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.TTS;
import com.golfgame.R;
import com.golfgame.game.GolfGameAnalytics;

public class InstructionsActivity extends Activity implements OnTouchListener{

	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instructions_general);
		float fontSize;
		float scale;
		Typeface font;
		TextView t;
		View v;
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		
		Intent i = getIntent();
		
		int type = i.getIntExtra(MainActivity.KEY_TYPE_INSTRUCTIONS, 0);
		
		String speech;
		
		if(type == 0){
			setContentView(R.layout.instructions_controls);
			speech = getString(R.string.instructions_controls_label) + " " + getString(R.id.instructions_controls_content);
			AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.INSTRUCTIONS_CONTROLS_ACTIVITY);
			t = (TextView) findViewById(R.id.instructions_controls_content);
			v = findViewById(R.id.control_root);
		}else{
			setContentView(R.layout.instructions_general);
			speech = getString(R.string.instructions_general_label) + " " + getString(R.id.instructions_general_content);
			AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.INSTRUCTIONS_GENERAL_ACTIVITY);
			t = (TextView) findViewById(R.id.instructions_general_content);
			v = findViewById(R.id.general_root);
		}

		v.setOnTouchListener(this);
		
		t.setTextSize(fontSize);
		t.setTypeface(font);
		
		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(speech);
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
	public boolean onTouch(View v, MotionEvent event) {
		this.finish();
		return false;
	}
}
