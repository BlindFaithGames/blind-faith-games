package org.example.activities;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class ControlsActivity extends Activity implements OnClickListener{
	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		float scale = this.getResources().getDisplayMetrics().density;
		float fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instructions_controls);
		
		Typeface font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		
		TextView t = (TextView) findViewById(R.id.instructions_controls_content);
		String speech = getString(R.string.instructions_controls_label) + " " + t.getContentDescription() + "Click to continue";
		
		t.setTextSize(fontSize);
		t.setTypeface(font);
		t.setOnClickListener(this);
		
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
	public void onClick(View v) {
		textToSpeech.stop();
		this.finish();
	}

}
