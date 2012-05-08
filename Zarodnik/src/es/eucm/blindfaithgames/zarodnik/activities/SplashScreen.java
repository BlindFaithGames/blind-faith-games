package es.eucm.blindfaithgames.zarodnik.activities;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;
import es.eucm.blindfaithgames.engine.sound.SubtitleInfo;
import es.eucm.blindfaithgames.engine.sound.TTS;
import es.eucm.blindfaithgames.zarodnik.R;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikMusicSources;

public class SplashScreen extends Activity {
    
	private static final String ADVICE = "es.eucm.blindfaithgames.zarodnik.activities.SlashAdvice";
	
	protected boolean _active = true;
    protected int _splashTime = 5000;
	private TTS textToSpeech;
	
	private Runnable lastRunnable;
	private Handler handler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent i = getIntent();
	
		int content = i.getIntExtra(ADVICE, 0);
		
		if(content == 0)
			setLogoContent();
		else
			setAdviceContent();
    }
    
    private void setAdviceContent() {
    	setContentView(R.layout.advice);
  
		Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);
		
		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
       
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.advice), TTS.QUEUE_FLUSH, s);
		
        handler = new Handler();
	    lastRunnable = new Runnable() {
            public void run() {
            	finish();
                /* start the activity */
                startActivity(new Intent("es.eucm.blindfaithgames.zarodnik.activities.MainActivity"));
            }
        };
        handler.postDelayed(lastRunnable, 5000);
	}

	private void setLogoContent() {
    	setContentView(R.layout.splash);
    	
		Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);
		
		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
       
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.mode) + "," + getString(R.string.group_name), TTS.QUEUE_FLUSH, s);
		
        handler = new Handler();

	    lastRunnable = new Runnable() {
	            public void run() {
	            	finish();
	                Intent i = new Intent("es.eucm.blindfaithgames.zarodnik.activities.SplashScreen");
	                i.putExtra(ADVICE, 1);
	                startActivity(i);
	            }
	    };
        handler.postDelayed(lastRunnable, 15000);
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            textToSpeech.stop();
            handler.removeCallbacks(lastRunnable);
            handler.postDelayed(lastRunnable, 1);
        }
        return true;
    }
}