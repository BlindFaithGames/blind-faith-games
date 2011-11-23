package org.example.golf;


import org.example.R;
import org.example.tinyEngineClasses.Game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class GolfGameView extends View{

	private Game game;
	
	public GolfGameView(Context context) {
		super(context);
		
		game = new GolfGame(this);
		
		game.run();
		
		requestFocus();
		setFocusableInTouchMode(true);
	}

	
	
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw the background...
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), 
							R.drawable.golfgame_background),
							null, 
							new Rect(0,0,getWidth(),getHeight()), 
							null);
		
		game.gameLoop(canvas);
		
	}
	
}
