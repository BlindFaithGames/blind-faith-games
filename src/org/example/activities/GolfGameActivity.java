package org.example.activities;

import org.example.golf.GolfGame;
import org.example.tinyEngineClasses.AnimatedSprite;
import org.example.tinyEngineClasses.DrawablePanel;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.GestureManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

public class GolfGameActivity extends Activity {
	
	private GolfGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        DrawablePanel golfView = new GolfGamePanel(this);
        setContentView(golfView);
        
        game = new GolfGame(golfView);
    }
    
    class GolfGamePanel extends DrawablePanel {
    	
        private GestureManager gestureScanner;
        
		public GolfGamePanel(Context context) {
			super(context);
			 gestureScanner = new GestureManager(context);
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
		public void onUpdate(long gameTime) {
			game.onUpdate();
		}

    }
}
