package org.example.activities;

import org.example.golf.GolfGame;
import org.example.tinyEngineClasses.DrawablePanel;
import org.example.tinyEngineClasses.Input;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class GolfGameActivity extends Activity {
	private static String TAG = "GolfGameActivity";
	private GolfGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        DrawablePanel golfView = new GolfGamePanel(this);
        setContentView(golfView);
        
        game = new GolfGame(golfView,this);
    }
    
    class GolfGamePanel extends DrawablePanel implements OnGestureListener {
        private GestureDetector gestureScanner;
        
		public GolfGamePanel(Context context) {
			super(context);
	        gestureScanner = new GestureDetector(this);
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
		
		public boolean onTouchEvent(MotionEvent event) {
			return gestureScanner.onTouchEvent(event);
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			Input.getInput().addEvent("onDown",e,null,-1,-1);
			System.out.println("On Down: " + e.toString());
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			System.out.println("Fling: " + e1.toString()); System.out.println("Fling: " + e2.toString());
			System.out.println("VX: "+velocityX + "\n VY: " + velocityY);
//			Log.d(TAG, "VX: "+velocityX + "\n VY: " + velocityY);
			Input.getInput().addEvent("onFling", e1, e2 ,velocityX, velocityY);

			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Input.getInput().addEvent("onLongPress",e,null,-1,-1);
			System.out.println("Long Pressed: " + e.toString());
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			System.out.println("Scroll: " + e1.toString()); System.out.println("Scroll: " + e2.toString());
			System.out.println("X: "+distanceX + "\n Y: " + distanceY);
//			Log.d(TAG, "X: "+distanceX + "\n Y: " + distanceY);
			Input.getInput().addEvent("onScroll", e1, e2, distanceX, distanceY);
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			Input.getInput().addEvent("onShowPress", e, null, -1, -1);
			System.out.println("On Show Press: " + e.toString());
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Input.getInput().addEvent("onSingleTap", e, null, -1, -1);
			System.out.println("On Single Tap: " + e.toString());
			return true;
		}
		
		
    }

}