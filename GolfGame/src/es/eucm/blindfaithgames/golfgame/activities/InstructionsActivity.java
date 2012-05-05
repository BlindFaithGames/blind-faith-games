package es.eucm.blindfaithgames.golfgame.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.TextView;


import es.eucm.blindfaithgames.engine.feedback.AnalyticsManager;
import es.eucm.blindfaithgames.engine.input.Input;
import es.eucm.blindfaithgames.engine.sound.TTS;
import es.eucm.blindfaithgames.golfgame.R;
import es.eucm.blindfaithgames.golfgame.game.GolfGameAnalytics;

public class InstructionsActivity extends Activity implements OnTouchListener{

	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		if(!SettingsActivity.getBlindMode(this)){
			setTheme(android.R.style.Theme_Dialog);
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentScreen();
		}else{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.empty);
		}
		
		Intent i = getIntent();
		
		int type = i.getIntExtra(MainActivity.KEY_TYPE_INSTRUCTIONS, 0);
		
		String speech;
		
		if(type == 0)
			speech = getString(R.string.instructions_controls_label) + " " + getString(R.string.instructions_controls_text);
		else
			speech = getString(R.string.instructions_general_label) + " " + getString(R.string.instructions_general_text);

		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(speech);
	}
	
	private void setContentScreen() {
		float fontSize;
		float scale;
		Typeface font;
		TextView t;
		View v;
		
		Intent i = getIntent();
		
		int type = i.getIntExtra(MainActivity.KEY_TYPE_INSTRUCTIONS, 0);

		//scale = this.getResources().getDisplayMetrics().density;
		//fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		//font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		
		if(type == 0){
			setContentView(R.layout.instructions_controls);
			AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.INSTRUCTIONS_CONTROLS_ACTIVITY);
			//t = (TextView) findViewById(R.id.instructions_controls_content);
			v = findViewById(R.id.control_root);
		}else{
			setContentView(R.layout.instructions_general);
			AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.INSTRUCTIONS_GENERAL_ACTIVITY);
			//t = (TextView) findViewById(R.id.instructions_general_content);
			v = findViewById(R.id.general_root);
		}
		
		v.setOnTouchListener(this);
		
		//t.setTextSize(fontSize);
		//t.setTypeface(font);
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
