package org.example.activities;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class InstructionsActivity extends Activity{

	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		Typeface font;
		TextView t;
		String speech;
		Intent i;
		int type;		
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instructions_general);
		
		i = getIntent();
		type = i.getIntExtra(MainActivity.KEY_TYPE_INSTRUCTIONS, 0);
		font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		
		if(type == 1){
			speech = getString(R.string.instructions_controls_label) + " " + i.getStringExtra(MainActivity.KEY_INSTRUCTIONS_CONTROLS);
			t = (TextView) findViewById(R.id.instructions_controls_content);
		}else{
			speech = getString(R.string.instructions_general_label) + " " + i.getStringExtra(MainActivity.KEY_INSTRUCTIONS_GENERAL);
			t = (TextView) findViewById(R.id.instructions_general_content);
		}
		
		t.setTypeface(font);
		t.setTextSize(RuntimeConfig.FONT_SIZE);
		
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
	
}
