package com.juego4.activities;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.accgames.general.DrawablePanel;
import com.accgames.general.Game;
import com.accgames.general.GameState;
import com.accgames.input.Input;
import com.accgames.input.XMLKeyboard;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Music;
import com.accgames.sound.Sound3DManager;
import com.accgames.sound.SubtitleInfo;
import com.accgames.sound.TTS;
import com.juego4.R;
import com.juego4.game.Gameplay;
import com.juego4.game.Juego4MusicSources;

public class Juego4GameActivity extends Activity {
	private TTS textToSpeech;
	private Game game;
	
	// Cargamos la conf desde un XML
	private XMLKeyboard keyboard;
	
	public static final int GAMEPLAY_ID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		
		if(SettingsActivity.getTranscription(this)){
			
			Map<Integer, String> onomatopeias = Juego4MusicSources.getMap(this);
			
			SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
					R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
			
			textToSpeech.enableTranscription(s);
			Music.getInstanceMusic().enableTranscription(this, s);
		}else{
			textToSpeech.disableTranscription();
			Music.getInstanceMusic().disableTranscription();
		}
		
		keyboard = Input.getKeyboard();
		
		DrawablePanel zarodnikView = new ZarodnikGamePanel(this);
		setContentView(zarodnikView);
		
		createGame(zarodnikView);
		
		if (SettingsActivity.getBlindMode(this)) 
			game.setDisabled(true);
    }
    
    private void createGame(DrawablePanel zarodnikView) {
    	ArrayList<Integer> order = new ArrayList<Integer>();
    	order.add(GAMEPLAY_ID);

    	game = new Game();
		
    	ArrayList<GameState> gameStates = new ArrayList<GameState>();
		gameStates.add(GAMEPLAY_ID, new Gameplay(zarodnikView,textToSpeech,this,game));
		
		game.initialize(gameStates, order);
	}

	@Override
    protected void onDestroy() {
		textToSpeech.stop();
    	Sound3DManager.getSoundManager(this).stopAllSources();
    	//Music.getInstanceMusic().stopAllResources();
    	super.onDestroy();
    }
    
    /*---------------------------------------------------------------------
     *  GESTURE DETECTOR
     ----------------------------------------------------------------------*/
    class MyGestureDetector extends SimpleOnGestureListener {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
            	Input.getInput().addEvent("onDoubleTap",  MotionEvent.obtain(e), null, -1, -1);
            	return true;
            }
            
            @Override
            public boolean onDown(MotionEvent e) {
            	Input.getInput().addEvent("onDown",  MotionEvent.obtain(e), null, -1, -1);
            	return true;
            }
            
            @Override
            public void onLongPress(MotionEvent e) {
            	 Input.getInput().addEvent("onLongPress", e, null, -1, -1);
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
		
		private boolean dragging;
    	
		public ZarodnikGamePanel(Context context) {
			super(context);
			mGestureDetector = new GestureDetector(new MyGestureDetector());
		}
		
		@Override
		public void onInitalize() {
			game.onInit();
		}
		
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			game.onDraw(canvas);
		}
		
		@Override
		public void onUpdate() {
			game.onUpdate();
			if(game.isEndGame())
				((Activity) this.getContext()).finish();
		}
    	
        @Override
        public boolean onTouchEvent(MotionEvent event) {
        	onDrag(event);
            return mGestureDetector.onTouchEvent(event);
        }
        
        private void onDrag(MotionEvent event) {
    		if (event.getAction() == MotionEvent.ACTION_DOWN) {
    			dragging = true;
    			Input.getInput().addEvent("onDrag", MotionEvent.obtain(event), null, -1, -1);
    		}
    		if (event.getAction() == MotionEvent.ACTION_UP) {
    			dragging = false;
    		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
    			if (dragging) {
    				Input.getInput().addEvent("onDrag", MotionEvent.obtain(event), null, -1, -1);
    			}
    		}	
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
				    if (found){/*
				    	if (keyboard.getAction(keyCode).equals(KeyConfActivity.ACTION_BLIND_MODE)){
				    		game.setDisabled(!game.getDisabled());
							RuntimeConfig.blindMode = !RuntimeConfig.blindMode;
				    	}*/
				    }
			}
			return found;
		}
    }
}