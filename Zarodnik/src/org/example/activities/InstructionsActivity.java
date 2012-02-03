package org.example.activities;

import org.example.R;
import org.example.tinyEngineClasses.TTS;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.TextView;

public class InstructionsActivity extends Activity implements OnLongClickListener{
	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.instructions_general);
		
//		ScrollView scrollView = (ScrollView) this.findViewById(R.id.instructions_scrollview);
		
//		scrollView.setOnLongClickListener(this);
		
		TextView t = (TextView) findViewById(R.id.instructions_general_content);
		String speech = getString(R.string.instructions_general_label) + " " + t.getContentDescription() + " Long touch to continue";
		
		t.setOnLongClickListener(this);
		
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
	public boolean onLongClick(View v) {
		textToSpeech.stop();
		this.finish();
		return true;
	}

	
}
