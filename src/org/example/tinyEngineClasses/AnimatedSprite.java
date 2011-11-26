package org.example.tinyEngineClasses;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimatedSprite {
	
	private Bitmap animation;
	private int xPos;
	private int yPos;
	private Rect sRectangle;

	private int numFrames;
	private int currentFrame;
	private int spriteHeight;
	private int spriteWidth;
	private boolean stop;
	
	private int frameDelay;
	private int triggerDelay;
	
	public AnimatedSprite() {
		sRectangle = new Rect(0, 0, 0, 0);

		frameDelay = 5;
		triggerDelay = 5;
	}
	
	public void Initialize(Bitmap bitmap, int height, int width, int frameCount) {
		this.animation = bitmap;
		this.spriteHeight = height;
		this.spriteWidth = width;
		this.sRectangle.top = 0;
		this.sRectangle.bottom = spriteHeight;
		this.sRectangle.left = 0;
		this.sRectangle.right = spriteWidth;
		this.numFrames = frameCount;
		stop = true;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setXPos(int value) {
		xPos = value;
	}
	
	public void setYPos(int value) {
		yPos = value;
	}
	
	public void play() {
		stop = false;
	}
	
	public void play(int frameDelay){
		stop = false;
		this.frameDelay = frameDelay;
		triggerDelay = this.frameDelay;
	}
	
	public void stop() {
		stop = true;
	}
	
	public void onUpdate() {
		if (frameDelay >= 0 && --triggerDelay <= 0){
			// reset the frame trigger
			triggerDelay = frameDelay;
			
			// Increment the frame
			if(!stop){
				currentFrame += 1;
				
				if( currentFrame >= numFrames ) {
					currentFrame = 0;
				}
				
				sRectangle.left = currentFrame * spriteWidth;
				sRectangle.right = sRectangle.left + spriteWidth;
				
				
				}
			}
	}
	
	public void onDraw(int x, int y, Canvas canvas) {
		Rect dest = new Rect(x, y, x + spriteWidth,
										y + spriteHeight);
		canvas.drawBitmap(animation, sRectangle, dest, null);
	}
}
