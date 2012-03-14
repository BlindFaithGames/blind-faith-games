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
import com.accgames.sound.TTS;
import com.zarodnik.R;

public class InstructionsActivity extends Activity implements OnTouchListener{
	private TTS textToSpeech;
	

	protected void onCreate(Bundle savedInstanceState) {
		float fontSize;
		float scale;
		Typeface font;
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instructions_general);
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		TextView t = (TextView) findViewById(R.id.instructions_general_content);
		t.setTextSize(fontSize);
		t.setTypeface(font);
		
		View v = findViewById(R.id.general_root);
		v.setOnTouchListener(this);
		
		String speech = getString(R.string.instructions_general_label) + " " + t.getContentDescription()+ " "+ getString(R.string.instruction_speech);
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
