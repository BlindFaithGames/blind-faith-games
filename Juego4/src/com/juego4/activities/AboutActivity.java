package com.juego4.activities;



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
import com.juego4.R;

/**
 * @author Gloria Pozuelo, Gonzalo Benito and Javier Álvarez
 * This class implements the about activity, where is shown a description of 
 */

public class AboutActivity extends Activity implements OnTouchListener{	
	private TTS textToSpeech;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);

		float fontSize;
		float scale;
		Typeface font;
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);  
		
		scale = this.getResources().getDisplayMetrics().density;
		fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		TextView t = (TextView) findViewById(R.id.about_content);
		t.setTextSize(fontSize);
		t.setTypeface(font);
		
		View v = findViewById(R.id.about_root);
		v.setOnTouchListener(this);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.about_title) + " " + getString(R.string.about_text) + " " + getString(R.string.instruction_speech));
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