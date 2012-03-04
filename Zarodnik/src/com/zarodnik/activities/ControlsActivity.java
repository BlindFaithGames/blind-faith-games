package com.zarodnik.activities;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.TextView;

import com.accgames.others.RuntimeConfig;
import com.accgames.tinyEngineClasses.TTS;
import com.zarodnik.R;

public class ControlsActivity extends Activity implements OnTouchListener{
	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		float scale = this.getResources().getDisplayMetrics().density;
		float fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instructions_controls);
		
		Typeface font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		
		TextView t = (TextView) findViewById(R.id.instructions_controls_content);
		t.setTextSize(fontSize);
		t.setTypeface(font);
	
		View v = findViewById(R.id.control_root);
		v.setOnTouchListener(this);
		
		String speech = getString(R.string.instructions_controls_label) + " " + t.getContentDescription() + " " + getString(R.string.instruction_speech);
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
