package org.example.activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.example.R;
import org.example.tinyEngineClasses.DrawablePanel;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.SoundManager;
import org.example.tinyEngineClasses.TTS;
import org.example.zarodnik.ZarodnikGame;
import org.example.zarodnik.XML.KeyboardReader;
import org.example.zarodnik.XML.XMLKeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

public class ZarodnikGameActivity extends Activity {
	private static String TAG = "ZarodnikGameActivity";
	
	private static final int intro_sound = R.raw.pacman_intro;
	private TTS textToSpeech;
	private ZarodnikGame game;
	
	private boolean mIsScrolling = false;
	
	// Cargamos la conf desde un .xml
	private XMLKeyboard keyboard;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		
		loadKeyConfiguration();
		
		DrawablePanel zarodnikView = new ZarodnikGamePanel(this);
		setContentView(zarodnikView);
		
		game = new ZarodnikGame(zarodnikView,textToSpeech,this);
    }
    
    @Override
    protected void onDestroy() {
    	SoundManager.getSoundManager(this).stopAllSources();
    	super.onDestroy();
    }
    
    private void loadKeyConfiguration() {
		KeyboardReader reader = new KeyboardReader();
		FileInputStream fis = null;
		try {
			fis = openFileInput(getString(R.string.app_name) + ".xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		keyboard = reader.loadEditedKeyboard(fis);
	}
    
	/*---------------------------------------------------------------------
	 *  GESTURE DETECTOR
	 ----------------------------------------------------------------------*/
    class MyGestureDetector extends SimpleOnGestureListener {

		//@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Log.d(TAG, "Scroll: " + e1.toString() + "\n" + e2.toString());
			mIsScrolling = true; 
			Input.getInput().addEvent("onScroll", e1, e2, distanceX, distanceY);
			return true;
		}
    }
	/*---------------------------------------------------------------------
	 *  FIN GESTURE DETECTOR
	 ----------------------------------------------------------------------*/

    
	/*---------------------------------------------------------------------
	 *  DRAWABLE PANEL
	 ----------------------------------------------------------------------*/
    
    class ZarodnikGamePanel extends DrawablePanel{
    	private GestureDetector mGestureDetector;
    	
		public ZarodnikGamePanel(Context context) {
			super(context);
	        mGestureDetector = new GestureDetector(new MyGestureDetector());
		}
		
		@Override
		public void onInitalize() {
			game.onInit();
//			Music.getInstanceMusic().playWithBlock(this.getContext(), intro_sound, false);
			SoundManager.getSoundManager(game.getContext()).playAllSources();
		}
		
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			game.onDraw(canvas);
		}
		
		@Override
		public void onUpdate() {
			game.onUpdate();
		}
    	
        @Override
        public boolean onTouchEvent(MotionEvent event) {
        	if (mGestureDetector.onTouchEvent(event)){
        		return true;
        	}
        	else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mIsScrolling) {
                    Log.d(TAG,"onUp: " + event.toString());
                    mIsScrolling  = false;
                    Input.getInput().addEvent("onUp", MotionEvent.obtain(event), null, -1, -1);
                    return true;
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_MOVE){
            	mIsScrolling = true;
            }
            else if (event.getAction() == MotionEvent.ACTION_DOWN){
            	Log.d(TAG, "onDown: " + event.toString());
            	Input.getInput().addEvent("onDown",  MotionEvent.obtain(event), null, -1, -1);
    			return true;
            }
            return false;
        }
        
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event){
        	boolean found = manageCustomConfigurationKeys(keyCode,event);
        	if(!found){
        		manageDefaultConfigurationKeys(keyCode,event);
        	}
            return true;
        }
        
		private void manageDefaultConfigurationKeys(int keyCode, KeyEvent event) {
        	switch(keyCode){
	        	case KeyEvent.KEYCODE_SEARCH:
	        		Input.getInput().addEvent("onKeySearch", event, -1, -1);
	            break;
	        	case KeyEvent.KEYCODE_BACK:
	        		finish();
	            break;
	        	case KeyEvent.KEYCODE_VOLUME_DOWN:
	        		Input.getInput().addEvent("onVolDown", event, -1, -1);
	        		break;
	        	case KeyEvent.KEYCODE_VOLUME_UP:
	        		Input.getInput().addEvent("onVolUp", event, -1, -1);
	        		break;
        	}
		}

		private boolean manageCustomConfigurationKeys(int keyCode, KeyEvent e) {
		    int i = 0; 
		    boolean found = false;
			if(e.getAction() == KeyEvent.ACTION_DOWN){
				 while (!found && i < keyboard.getNum()){
				    	found = keyboard.getAction(keyCode) != null;   
				    	i++;
				    }
				    if (found){
				    	if (keyboard.getAction(keyCode).equals(KeyConfActivity.ACTION_RECORD)){
				    		Input.getInput().addEvent(KeyConfActivity.ACTION_RECORD, e, -1, -1);
				    	}
				    }
			}
			return found;
		}
		
		
    }
}