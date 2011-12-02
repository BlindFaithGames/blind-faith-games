package org.example.activities;

import org.example.golf.GolfGame;
import org.example.tinyEngineClasses.DrawablePanel;
import org.example.tinyEngineClasses.Input;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GolfGameActivity extends Activity {
	private static String TAG = "GolfGameActivity";
	private GolfGame game;
	
	private boolean UP_MODE = true;
	
	private boolean mIsScrolling = false;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UP_MODE = SettingsActivity.getOnUp(this);
       
        DrawablePanel golfView = new GolfGamePanel(this);
        setContentView(golfView);
         
        game = new GolfGame(golfView,this);
    }
	
	/*---------------------------------------------------------------------
	 *  GESTURE DETECTOR
	 ----------------------------------------------------------------------*/
    class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			Input.getInput().addEvent("onDown",e,null,-1,-1);
			System.out.println("On Down: " + e.toString());
			Log.d(TAG, "onDown");
			return true;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Log.d(TAG, "VX: "+velocityX + "\n VY: " + velocityY);
			/* We should register the event only if up mode is disable */
			if (!UP_MODE) {
				Input.getInput().addEvent("onFling", e1, e2 ,velocityX, velocityY);
				return true;
			}
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Log.d(TAG, "X: "+distanceX + "\n Y: " + distanceY);
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
    
    class GolfGamePanel extends DrawablePanel{
    	private GestureDetector mGestureDetector;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mIsScrolling) {
                    Log.d(TAG,"Scroll finished");
                    mIsScrolling  = false;
                    Input.getInput().addEvent("onUp", event, null, -1, -1);
                }
            }
            
            return false;
        }
        
		public GolfGamePanel(Context context) {
			super(context);
	        mGestureDetector = new GestureDetector(new MyGestureDetector());
		}
		
		
		@Override
		public void onInitalize() {
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
		
    }

}