package es.eucm.blindfaithgames.tsm.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import es.eucm.blindfaithgames.engine.input.Input;
import es.eucm.blindfaithgames.engine.sound.TTS;
import es.eucm.blindfaithgames.tsm.R;

public class AboutActivity extends Activity{

	private TTS textToSpeech;
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		if(!SettingsActivity.getBlindMode(this)){
			setTheme(android.R.style.Theme_Dialog);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.about);
		}else{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.empty);
		}
		
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
