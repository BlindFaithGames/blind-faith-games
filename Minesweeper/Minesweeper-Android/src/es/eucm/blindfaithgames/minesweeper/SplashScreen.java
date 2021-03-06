package es.eucm.blindfaithgames.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;
import es.eucm.blindfaithgames.engine.sound.SubtitleInfo;
import es.eucm.blindfaithgames.engine.sound.TTS;

public class SplashScreen extends Activity {
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
        setContentView(R.layout.splash);
      
		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, null);
       
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.mode) + " "  + getString(R.string.group_name), TTS.QUEUE_FLUSH, s);
        
        handler = new Handler();

	    lastRunnable = new Runnable() {
	            public void run() {
	            	finish();
	                Intent i = new Intent("es.eucm.blindfaithgames.minesweeper.MinesweeperActivity");
	                startActivity(i);
	            }
	    };
        handler.postDelayed(lastRunnable, 15000);
    }
    
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.removeCallbacks(lastRunnable);
            handler.postDelayed(lastRunnable, 1);
        }
        return true;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	
}