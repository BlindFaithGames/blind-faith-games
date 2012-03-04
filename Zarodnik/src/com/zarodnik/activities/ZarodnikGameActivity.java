package com.zarodnik.activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import com.accgames.XML.KeyboardReader;
import com.accgames.XML.XMLKeyboard;
import com.accgames.tinyEngineClasses.DrawablePanel;
import com.accgames.tinyEngineClasses.Game;
import com.accgames.tinyEngineClasses.GameState;
import com.accgames.tinyEngineClasses.Input;
import com.accgames.tinyEngineClasses.SoundManager;
import com.accgames.tinyEngineClasses.TTS;
import com.zarodnik.R;
import com.zarodnik.game.ZarodnikGameOver;
import com.zarodnik.game.ZarodnikGameplay;
import com.zarodnik.game.ZarodnikIntro;
import com.zarodnik.game.ZarodnikTutorial;
import com.zarodnik.game.ZarodnikTutorial.TutorialID;

public class ZarodnikGameActivity extends Activity {
	private static String TAG = "ZarodnikGameActivity";

	private TTS textToSpeech;
	private Game game;
	
	// Cargamos la conf desde un .xml
	private XMLKeyboard keyboard;
	
	public static final int INTRO_ID = 0;
	public static final int TUTORIAL0_ID = 1;
	public static final int TUTORIAL1_ID = 2;
	public static final int TUTORIAL2_ID = 3;
	public static final int TUTORIAL3_ID = 4;
	public static final int GAMEPLAY_ID = 5;
	public static final int GAME_OVER_ID = 6;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		
		keyboard = Input.getKeyboard();
		
		DrawablePanel zarodnikView = new ZarodnikGamePanel(this);
		setContentView(zarodnikView);
		
		createGame(zarodnikView);
    }
    
    private void createGame(DrawablePanel zarodnikView) {
    	ArrayList<Integer> order = new ArrayList<Integer>();
    	order.add(INTRO_ID);
    	order.add(TUTORIAL0_ID);
    	order.add(GAMEPLAY_ID);
    	order.add(GAME_OVER_ID);

    	game = new Game();
		
    	ArrayList<GameState> gameStates = new ArrayList<GameState>();
		gameStates.add(INTRO_ID, new ZarodnikIntro(zarodnikView,textToSpeech,this,game));
		gameStates.add(TUTORIAL0_ID, new ZarodnikTutorial(zarodnikView,textToSpeech,this,TutorialID.TUTORIAL0,game));
		gameStates.add(TUTORIAL1_ID, new ZarodnikTutorial(zarodnikView,textToSpeech,this,TutorialID.TUTORIAL1,game));
		gameStates.add(TUTORIAL2_ID, new ZarodnikTutorial(zarodnikView,textToSpeech,this,TutorialID.TUTORIAL2,game));
		gameStates.add(TUTORIAL3_ID, new ZarodnikTutorial(zarodnikView,textToSpeech,this,TutorialID.TUTORIAL3,game));
		gameStates.add(GAMEPLAY_ID, new ZarodnikGameplay(zarodnikView,textToSpeech,this,game));
 		gameStates.add(GAME_OVER_ID, new ZarodnikGameOver(zarodnikView,textToSpeech,this,game));
		
		game.initialize(gameStates, order);
	}

	@Override
    protected void onDestroy() {
    	SoundManager.getSoundManager(this).stopAllSources();
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