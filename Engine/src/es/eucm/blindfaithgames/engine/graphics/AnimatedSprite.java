package es.eucm.blindfaithgames.engine.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * This class contains a simple animation. It's supposed the frames of the graphic are only in horizontal sense.
 * 
 * @author Javier Álvarez & Gloria Pozuelo. 
 * 
 * */

public class AnimatedSprite {
	
	private Bitmap animation; // The sprite sheet
	private int xPos; // The animation position on the x axis
	private int yPos; // The animation position on the y axis
	private Rect sRectangle; // Indicates the current frame on the sprite sheet

	private int numFrames; // Number of frames in the sheet
	private int currentFrame; // Current frame 
	private int spriteHeight; // Frame height
	private int spriteWidth; // Frame width
	private boolean stop; // Enables the animation
	
	private int frameDelay;
	private int triggerDelay;
	
	/**
	 * Default constructor.
	 * 
	 * */
	public AnimatedSprite() {
		sRectangle = new Rect(0, 0, 0, 0);

		frameDelay = 5;
		triggerDelay = 5;
	}
// ----------------------------------------------------------- Getters ----------------------------------------------------------- 

	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public int getFrameCount() {
		return numFrames;
	}
	
	public int getNumFrames(){
		return numFrames;
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public boolean isFinished(){
		return stop;
	}

// ----------------------------------------------------------- Setters -----------------------------------------------------------  
	
	public void setXPos(int value) {
		xPos = value;
	}
	
	public void setYPos(int value) {
		yPos = value;
	}

	public void setFrameDelay(int framesPerStep) {
		frameDelay = framesPerStep;
	}

	public void play() {
		stop = false;
	}
	
	public void stop() {
		stop = true;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------  
	
	/**
	 * To initialize an animation.
	 * 
	 * @param bitmap // The sprite sheet.
	 * @param height // Sprite sheet height.
	 * @param width // Sprite sheet width.
	 * @param frameCount // Number of frames in the sheet.
	 * 
	 * */
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
	
	/**
	 * Sets the delay of the image.
	 * 
	 * @param frameDelay To hang up the animation.
	 * */
	public void play(int frameDelay){
		stop = false;
		this.frameDelay = frameDelay;
		triggerDelay = this.frameDelay;
	}

	/**
	 * Updates the logic of animation. It checks which is the current frame and computes the 
	 * rectangle of the current frame.
	 * 
	 * */
	public void onUpdate() {
		if (frameDelay >= 0 && --triggerDelay <= 0){
			// Reset the frame trigger
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
	
	/**
	 * Draws on the screen the actual frame.
	 * 
	 * @param canvas Canvas object which will be painted.
	 * @param x Coordinate on x axis where the actual frame will be painted.
	 * @param y Coordinate on y axis where the actual frame will be painted.
	 * 
	 * */
	public void onDraw(int x, int y, Canvas canvas) {
		Rect dest = new Rect(x, y, x + spriteWidth,
										y + spriteHeight);
		canvas.drawBitmap(animation, sRectangle, dest, null);
	}
}