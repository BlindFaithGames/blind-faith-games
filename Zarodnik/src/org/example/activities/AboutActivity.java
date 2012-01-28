package org.example.activities;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class AboutActivity extends Activity{

	private TTS textToSpeech;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		TextView t; 
		Typeface font;
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);	
		
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		t = (TextView) findViewById(R.id.about_content);
		t.setTypeface(font);
		t.setTextSize(RuntimeConfig.FONT_SIZE);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.about_title) + " " + getString(R.string.about_text));
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
