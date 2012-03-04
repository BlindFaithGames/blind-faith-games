package com.golfgame.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;

import com.accgames.tinyengine.TTS;
import com.golfgame.R;

public class CheckKeyActivity extends Activity implements OnKeyListener{
	public static final String KEY_NAME = "KeyName";
	public static final String KEY_CODE = "KeyCode";
	private TTS textToSpeech;
	private int key;
	private EditText editText;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.keydialog);	
		editText = (EditText) findViewById(R.id.entry);
		editText.setOnKeyListener(this);
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.keydialog) + " " + getString(R.string.keydialog_text));
	}
	
	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		 super.onDestroy();
	     textToSpeech.stop();
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN){
			Intent i = new Intent(this, KeyConfActivity.class);
			key = keyCode;
			i.putExtra(CheckKeyActivity.KEY_CODE, key);
			this.setResult(KeyConfActivity.KEY_PRESSED, i);
			this.finish();
		}
		return true;
	}
}