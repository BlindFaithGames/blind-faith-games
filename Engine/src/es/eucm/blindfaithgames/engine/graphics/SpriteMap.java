package es.eucm.blindfaithgames.engine.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * This class provides a set of animation objects which will be used comfortably.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */
@SuppressWarnings("unused")
public class SpriteMap {
	
	private HashMap<String, Animation> map; // Animations map - <animation name, animation>
	private Animation currentAnim; // Selected animation from map 
	private Bitmap bitmap; // Sprite sheet
	
	private int frame; // Current frame within the selected animation (from 0 to frameCount-1)
	private int lastFrame; // Last frame in last animation (from 0 to nCol*nRow-1).*/
	private int step; // Game steps since last animation change 
	private int nCol; // Column number of the sprite sheet
	private int nRow; // Row number of the sprite sheet
	private boolean finished; // Indicates if the current animation has finished
	
	private int currentFrame; // Previous frame to the Current frame within the selected animation 
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param rows Row number of the sprite sheet.
	 * @param cols Column number of the sprite sheet.
	 * @param bitmap Sprite sheet which contains every animation contained in this set.
	 * @param frame First animation frame.
	 * 
	 * */
	public SpriteMap(int rows, int cols, Bitmap bitmap, int frame){
		this.bitmap = bitmap;
		nRow = rows;
		nCol = cols;
		
		map = new HashMap<String, Animation>();
		
		this.frame = frame;
		finished = false;
		step = 0;
		lastFrame = frame;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------   
	
	public int getWidth(){
		int w = bitmap.getWidth() / nCol;
		return w;
	}
	public int getHeight(){
		int h = bitmap.getHeight() / nRow;
		return h;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------   
	/**
	 * Adds a new animation to the animation set.
	 * @param name Animation name.
	 * @param frameList Frames from the sprite sheet which compounds the animation added.
	 * @param framesPerStep Animation frames per game step.
	 * @param loop Determines if animation ends or doesn't.
	 */
	public void addAnim(String name, ArrayList<Integer> frameList, int framesPerStep, boolean loop){
		Animation aux = new Animation(name,frameList,framesPerStep,loop);
	
		map.put(name, aux);
	}

	/**
	 * Plays an animation from the set. Previously It has to be added.
	 * 
	 * @param name Animation name which will be played.
	 * 
	 * */
	@SuppressWarnings("rawtypes")
	public void playAnim(String name){
		// Saves the current animation
		Animation oldAnim = currentAnim;

		// 
		Iterator<Animation> it = map.values().iterator();

		Map.Entry e = null;
		boolean found = false;
		while (!found && it.hasNext()){
			found = it.next().equals(map.get(name));
		}
		
		if (!found)
			currentAnim = oldAnim; // Could not find animation
		else {
			currentAnim = map.get(name);
			if (oldAnim != null) {
				// If there was an animation it could be the same.
				if (!oldAnim.equals(currentAnim))	{
					frame = 0;  // Reset animation because it has changed
					finished = false;
				}
			}
			else{
				frame = 0; // Reset animation
				finished = false;
			}
		}
	}

	/** 
	 * Plays an animation from the set but changing its velocity and its frames per game step.
	 * @param name Animation name which will be played.
	 * @param framesPerStep Number of animation frames per game step.
	 * @param loop Determines if animation ends or doesn't.
	 **/
	public void playAnim(String name, int framesPerStep, boolean loop) {	
		playAnim(name);
		if (currentAnim != null){
			currentAnim.setFrameDelay(framesPerStep);
			currentAnim.setLoop(loop);
		}
	}
	
	/** 
	 * Stops the current animation.
	 **/
	public void stopAnim() {
		if (currentAnim != null) {
			//currentAnim.stop();
		}
	}
	
	/***
	 * Increments the frame counter, checking if animation hasn't finished.
	 * 
	 * @return If the current animation has finished.
	 **/
	private boolean nextFrame() {
		if (step >= currentAnim.getFramesPerStep() && currentAnim.getFramesPerStep() > 0) {
			step = 0;
			return true;
		}
		else {
			step++;
			return false;
		}
	}

	/**
	 * Updates the logic of the animation.
	 * 
	 * */
	public void onUpdate() {
		if (currentAnim != null){
			if (!finished && nextFrame())
			{
				frame++;
				if (frame >= currentAnim.getFrameCount()) {
					if (!currentAnim.isLoop()) {
						finished = true;
						step = 0;
						lastFrame = currentAnim.getFrameList().get((currentAnim.getFrameCount())-1);
						frame = currentAnim.getFrameCount()-1;
					}
					else frame = 0;
				}
			}
		}
	}

	/**
	 * Draws the current frame of the current animation.
	 * 
	 * @param x the coordinate on the x axis where the current frame will be painted.
	 * @param y the coordinate on the y axis where the current frame will be painted.
	 * @param canvas Surface which will be painted.
	 * 
	 * */
	public void onDraw(int x, int y, Canvas canvas) {
		int w = bitmap.getWidth() / nCol;
		int h = bitmap.getHeight() / nRow;
		
		if (currentAnim != null){
			
			if(frame < currentAnim.getFrameList().size()){
				Integer posnum = currentAnim.getFrameList().get(frame);
			
				Point pos = numToXY(posnum);
				
				Rect currentFrame = new Rect(pos.x*w, pos.y*h, pos.x*w + w, pos.y*h + h);
				Rect dest = new Rect(x,y,x + w,y + h);
				canvas.drawBitmap(bitmap, currentFrame, dest, null);
			}
		}
	}
	
	/**
	 * From number of frame to XY positions.
	 * 
	 * @param num Number of frame which will be converted.
	 * @return XY positions of the frame, on the sprite sheet.
	 * */
	private Point numToXY (int num) {
		return new Point(num % nCol, num / nCol);
	}

	/**
	 * From XY positions to number of frame.
	 * 
	 *  @param x Column on the sprite sheet where frame is.
	 *  @param y Row on the sprite sheet where frame is.
	 *  @return Number of frame associated with XY.
	 * */
	private int XYToNum(int x, int y) {
		return x*nCol+y;
	}

	public void delete() {
		if(bitmap != null){
			if(!bitmap.isRecycled()){
				bitmap.recycle();
				bitmap = null;
			}
		}
	}
}