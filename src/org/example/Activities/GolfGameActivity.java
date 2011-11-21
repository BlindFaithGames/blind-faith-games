package org.example.activities;

import org.example.R;
import org.example.golf.AnimatedSprite;
import org.example.golf.DrawablePanel;
import org.example.golf.GolfGame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;

public class GolfGameActivity extends Activity {
	
	private GolfGame game;

	private AnimatedSprite animation = new AnimatedSprite();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GolfGamePanel(this));
        
    }
    
    class GolfGamePanel extends DrawablePanel {

		private int xEvent;
		private int yEvent;
		private int dir,ndir;
		
		public GolfGamePanel(Context context) {
			super(context);
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Paint brush = new Paint();
			brush.setColor(Color.WHITE);
			canvas.drawRect(new Rect(0,0,getWidth(),getHeight()),brush);
			
			if(dir != ndir){
				dir = ndir;
				if(dir == 0)
					animation.Initialize(BitmapFactory.decodeResource(getResources(), R.drawable.pjright), 60, 50, 30, 3);
				else
					if(dir == 1)
						animation.Initialize(BitmapFactory.decodeResource(getResources(), R.drawable.pjleft), 60, 50, 30, 3);
					else
						if(dir == 2)
							animation.Initialize(BitmapFactory.decodeResource(getResources(), R.drawable.pjdown), 60, 50, 30, 3);
						else
							if(dir == 3)
								animation.Initialize(BitmapFactory.decodeResource(getResources(), R.drawable.pjup), 60, 50, 30, 3);
			}
			
			GolfGameActivity.this.animation.draw(canvas);
		}
		
		@Override
		public void onInitalize() {
			GolfGameActivity.this.animation.Initialize(
					BitmapFactory.decodeResource(
							getResources(), 
							R.drawable.pjdown), 
							60, 50, 30, 3);
			xEvent = animation.getXPos(); 
			yEvent = animation.getYPos();
			dir = 0;
			ndir = 0;
		}

		@Override
		public void onUpdate(long gameTime) {
			GolfGameActivity.this.animation.Update(gameTime);
			if(!(xEvent < animation.getXPos() + 10 && xEvent > animation.getXPos() - 10)){
				animation.play();
					if(animation.getXPos() < xEvent){
						ndir = 0;
					}
					else
						if(animation.getXPos() > xEvent){
							ndir = 1;
						}
			}
			if(!(yEvent < animation.getYPos() + 10  && yEvent > animation.getYPos() - 10)){
					if(animation.getYPos() < yEvent){	
							ndir = 2;
					}
					else
						if(animation.getYPos() > yEvent){
							ndir = 3;
						}
				}
		if((xEvent < animation.getXPos() + 10 && xEvent > animation.getXPos() - 10) && 
			(yEvent < animation.getYPos() + 10  && yEvent > animation.getYPos() - 10))
				animation.stop();
		else{
			if(ndir == 0)
				animation.setXPos(animation.getXPos()+10);
			else
				if(ndir == 1)
					animation.setXPos(animation.getXPos()-10);
				else
					if(ndir == 2)
						animation.setYPos(animation.getYPos()+10);
					else 
						if(ndir == 3)
							animation.setYPos(animation.getYPos()-10);
		}
		}
		
		public boolean onTouchEvent(MotionEvent event) {
			xEvent = (int) event.getX();
			yEvent = (int) event.getY();
			return true;
		}
		
		
    }
}
