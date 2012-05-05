package es.eucm.blindfaithgames.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.accgames.sound.SubtitleInfo;
import com.accgames.sound.TTS;

public class SplashScreen extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 5000;
	private TTS textToSpeech;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
      
		SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
				R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, null);
       
		// Checking if TTS is installed on device
		textToSpeech = new TTS(this, getString(R.string.mode) + "," + getString(R.string.group_name), TTS.QUEUE_FLUSH, s);
		
        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(300);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    startActivity(new Intent("es.eucm.blindfaithgames.minesweeper.MinesweeperActivity"));
                    stop();
                }
            }
        };
        splashTread.start();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }
}