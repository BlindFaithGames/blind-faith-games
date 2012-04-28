package com.accgames.general;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pielot.openal.Source;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.accgames.graphics.SpriteMap;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Sound2D;
import com.accgames.sound.Sound3DManager;

/**
 * The higher abstraction in a game. Every element within a game it's a entity.
 * 
 * 
 *  @author Javier Álvarez & Gloria Pozuelo. 
 * 
 * */

public abstract class Entity {

	private static final int N_TIMERS = 5; // the number of timers associated to Entity
	
	private String id; // Unique Entity id  

	protected int x; // Coordinates on the screen
	protected int y;
	
	private Bitmap img; // Image associated with an entity
	private SpriteMap animations; // Animation associated with an entity
	
	private boolean enabled; // Can the instance be updated and painted?
	private boolean collidable; // Can the instance collide with other entities?
	private boolean visible; // Can the instance be painted?
	private boolean frozen;  // Can the instance be updated?
	private boolean removable;  // Can the instance be removed?
	private boolean transcription; // Indicates if we want to transcribe sources
	
    private int[] timers; // Can the instance be painted?
	
    private List<Mask> mask; // Collision mask set
    
    private List<Sound2D> sources; // 3D sound sources associated with an entity
    private boolean isPlaying; // Indicates if some sound is being played
    
	protected GameState gameState; // GameState where the instance of the class is active

	/**
	 *  Unique class constructor.
	 *  
	 *  @param x Coordinates in the x axis on the screen.
	 *  @param y Coordinates in the y axis on the screen.
	 *  @param img Static image associated with the entity.
	 *  @param gameState GameState where the instance is active.
	 *  @param mask Collision mask set.
	 *  @param animations  Animation associated with the entity.
	 *  @param soundName Name of the sound file that It'll be played like a 3D sound.
	 *  @param soundOffset Offset relative to the entity position.
	 *  @param collide Indicates if the instance can collide with other entities.
	 *
	 * */
	
	public Entity(int x, int y, Bitmap img, GameState gameState, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, boolean collide){
		this.x = x;
		this.y = y;
		this.img = img;
		this.gameState = gameState;
		this.mask = mask;
		enabled = true;
		collidable = collide;
		visible = true;
		frozen = false;
		transcription = false;
		this.animations = animations;
		timers = new int[N_TIMERS];
		for(int i = 0; i < N_TIMERS; i++){
			timers[i] = -1;
		}
		if(soundName != null) {
			Sound3DManager sm = Sound3DManager.getSoundManager(gameState.getContext());
			Source s = sm.addSource(soundName);
			s.stop();
			Sound2D sound = new Sound2D(soundOffset, s);
			if(s != null){
				sources = new ArrayList<Sound2D>();
				sources.add(sound);
			}
			isPlaying = false;
		}
	}
	

	// ----------------------------------------------------------- Getters -----------------------------------------------------------
	public String getId() {
		return id;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public Bitmap getImg() {
		return img;
	}
	
	public boolean isCollidable() {
		return collidable;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isFrozen() {
		return frozen;
	}

	public List<Mask> getMask() {
		return mask;
	}
	
	public boolean isRenderable() {
		return visible;
	}
	
	public boolean isRemovable() {
		return removable;
	}
	
	/**
	 * Gets the image width.
	 * 
	 * @return If it's animated the frame width else the static image width.
	 * */
	public int getImgWidth(){
		if(animations == null)
			return img.getWidth();	
		else
			return animations.getWidth();
	}
	
	/**
	 * Gets the image height.
	 * 
	 * @return If it's animated the frame width else the static image height.
	 * */
	public int getImgHeight(){
		if(animations == null)
			return img.getHeight();	
		else
			return animations.getHeight();
	}
	
	public List<Sound2D> getSources(){
		return sources;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setImg(Bitmap img) {
		this.img = img;
	}
	
	public void setMask(List<Mask> maskList) {
		this.mask = maskList;
	}
	
    public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}
    
    public void setFrozen(boolean frozen) {
    	this.frozen = frozen;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setSpriteMap(SpriteMap animations) {
		this.animations= animations;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setTranscription(boolean transcription){
		this.transcription = transcription;
	}

// ----------------------------------------------------------- Others -----------------------------------------------------------    
	/**
	 *  Draws the graphic of the entity.
	 *  If it's animated the animation object
	 *  else the image associated. 
	 *  In other case it do nothing.
	 *  
	 *  @param canvas The surface that will be painted.
	 *
	 * */
	public  void onDraw(Canvas canvas){
		if(animations != null)
			animations.onDraw((int)x, (int)y,canvas);
		else
			if(img != null)
				canvas.drawBitmap(img, x, y, null);
		
		if(transcription && sources != null){
			for(Sound2D s: sources){
				s.onDraw(canvas, x, y);
			}
		}
		
		if(RuntimeConfig.IS_DEBUG_MODE){
			if(mask != null){
				Iterator<Mask> it = mask.iterator();
				while(it.hasNext()){
					Mask m = it.next();
					m.onDraw(canvas);
				}
			}
		}
	}
	
	/**
	 *  Draws the graphic of the entity.
	 *  If it's animated the object animation
	 *  else the image associated on the specified coordinates.
	 *  
	 *   @param x the global screen coordinate in the x axis where.
	 *   @param y the global screen coordinate in the y axis.
	 *	 @param canvas The surface that will be painted.
	 *
	 * */
	public  void onDraw(int x, int y, Canvas canvas){
		if(animations != null)
			animations.onDraw((int)x, (int)y,canvas);
		else
			if(img != null)
				canvas.drawBitmap(img, x, y, null);
		
		if(RuntimeConfig.IS_DEBUG_MODE){
			if(mask != null){
				Iterator<Mask> it = mask.iterator();
				while(it.hasNext()){
					Mask m = it.next();
					m.onDraw(canvas);
				}
			}
		}
	}
	
	
    /**
     *  Updates the logic of the entity. It includes the animation of the entity, its mask set and the 3D sound sources.
     * 
     * */
	public void onUpdate(){
		if(animations != null)
			animations.onUpdate();
		
		if(mask != null){
			Iterator<Mask> it = mask.iterator();
			Mask m;
			while(it.hasNext()){
				m = it.next();
				m.onUpdate(x,y);
			}
		}
		
		if(sources != null){
			Iterator<Sound2D> it = sources.iterator();
			Sound2D s;
			while(it.hasNext()){
				s = it.next();
				s.getS().setPosition(x + s.getP().x, y + s.getP().y, 0);
			}
		}
		
		for(int i = 0; i < N_TIMERS; i++){
			if(timers[i] > 0)
				timers[i]--;
			if (timers[i] == 0){
				this.onTimer(i);
				timers[i]--;
			}
		}
		
	}
	
	/** Called when this collides with another entity. Use it to specify actions to do when two entities collides.
            @param e the entity that collides with this.
    */
	public abstract void onCollision(Entity e);
	
    /** Called when the timer timer ends. Use it to specify actions to do when the count.
            @param timer Indicates the timer that will be activated [0-9].
    */
	public abstract void onTimer(int timer);

    /**  Check a value of a timer.
            @param number Indicates the timer selected [0-9].
            @return the number of game steps until the timer[number] will be called.
    */
    public int getTimer(int number) {
    	return  timers[number];
	}

    /** Sets the number of game steps until onTimer() will be called.
            @param number Indicates the timer selected [0-9].
            @param count number of game steps.
    */
    public void setTimer(int number,int count) {
    	if(number < N_TIMERS && number >= 0){
    		timers[number] = count;
    	}
	}
    
    /** 
     * Called when the entity gameState is created.
     */
    public abstract void onInit();

    /** 
     * Called when the entity is removed from the entity list. This occurs when remove is called.
     */
    public abstract void onRemove();
	
    /** 
     * This is used to remove this entity at the end of this game step.
     */
	public void remove() {
		removable = true;
	}

	
	/**
	 * Two entities collides if one of their mask collides with a mask of the other.
	 * 
	 * @param e2 entity that collides with entity this.
	 * 
	 * */
	public boolean collides(Entity e2) {
		if(mask != null){ 
			Iterator<Mask> it1 = mask.iterator();
			Iterator<Mask> it2;
			Mask m1,m2;
			boolean found = false;
			while(it1.hasNext() && !found){
				m1 = it1.next();
				if(e2.getMask() != null){
					
					it2 = e2.getMask().iterator();
					while(it2.hasNext() && !found){
						m2 = it2.next();
						found = m1.collide(m2);
					}
				}
			}
			return found;
		}
		return false;
	}
	
	/**
	 * Reproduces an animation whose name it's name.
	 * 
	 * @param name animation name.
	 * @param framesPerStep number of animation frames that the gameState will show in a game step.
	 * @param loop the animation continues playing indefinitely.
	 * */
	public void playAnim(String name, int framesPerStep, boolean loop){
		if(animations != null)
			animations.playAnim(name, framesPerStep, loop);
	}
	
	/**
	 * Stops an animation whose name it's name.
	 * 
	 * @param name animation name
	 * 
	 * */
	public void stopAnim(String name){
		if(animations != null)
			animations.stopAnim();
	}

	
	/**
	 * Removes the image and the sources that belong to this entity.
	 * 
	 * */
	public void delete() {
		if(sources != null){
			Iterator<Sound2D> it = sources.iterator();
			Sound2D s;
			while(it.hasNext()){
				s = it.next();
				s.getS().stop();
			}
		}
		if(img != null)
			img.recycle();
		
		if(animations != null)
			animations.delete();
	}
	
	/**
	 * Plays all the sources that belong to this entity.
	 * 
	 * */
	public void playAllSources(){
		if(sources != null){
			if(!isPlaying){
				Iterator<Sound2D> it = sources.iterator();
				Sound2D s;
				while(it.hasNext()){
					s = it.next();
					s.getS().play(true);
				}
				isPlaying = true;
			}
		}
	}
	
	/**
	 * Stops all the sources that belong to this entity.
	 * 
	 * */
	public void stopAllSources(){
		if(sources != null){
			Iterator<Sound2D> it = sources.iterator();
			Sound2D s;
			while(it.hasNext()){
				s = it.next();
				s.getS().stop();
			}
			isPlaying = false;
		}
	}
}