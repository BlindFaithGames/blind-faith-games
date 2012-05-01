package com.accgames.general;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.accgames.sound.TTS;

/**
 * This class represents a game state, a part of a game for example, the introduction, gameplay and game over.
 * 
 * @author Javier Alvarez & Gloria Pozuelo.
 * 
 * */

public abstract class GameState {
	
	public static int SCREEN_WIDTH;  
	public static int SCREEN_HEIGHT;
	public static float scale; 
	
	protected View v; // View associated with this game state
	
	protected Activity context; // Activity associated with this game state
	
	private Bitmap background = null; // If it's required, a background can be painted by default on the view
	
    private boolean game_is_running; // Indicates if the game state is finished
	
	private List<Entity> entities; 
	private List<Entity> collidables; 
	private List<Entity> renderables;
	private List<Entity> removables;

	private TTS textToSpeech; // To have access to the voice synthesizer

	private Paint brush; 
	
	protected Game game; // To have access to the game
	private boolean onInitialized; // To check if onInit method has been called any time from this instance
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param v View associated with this game state.
	 * @param context Context where game is running.
	 * @param textToSpeech Instance of TTS class to have access from the entities.
	 * @param game Reference to Game class to have access from the entities.
	 * 
	 * */
	public GameState(View v, Context context, TTS textToSpeech, Game game){
		game_is_running = true;
		this.game = game;
		this.context = (Activity) context;
		this.v = v;
		this.setTextToSpeech(textToSpeech);
		entities = new ArrayList<Entity>();
		collidables = new ArrayList<Entity>();
		renderables = new ArrayList<Entity>();
		removables = new ArrayList<Entity>();
		brush = null;
		
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		SCREEN_WIDTH = display.getWidth();
		SCREEN_HEIGHT = display.getHeight();
		// Get the screen's density scale
		scale = context.getResources().getDisplayMetrics().density;
		
		onInitialized = false;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------

	public boolean isRunning(){
		return game_is_running;
	}
	
	public View getView(){
		return v;
	}
	
	public Activity getContext(){
		return context;
	}
	
	public Bitmap getBackground(){
		return background;
	}
	
	public List<Entity> getEntities(){
		return entities;
	}
	
	public List<Entity> getRenderables(){
		return renderables;
	}
	public TTS getTTS(){
		return textToSpeech;
	}
	
	public boolean isOnInitialized() {
		return onInitialized;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	public void editBackground(Paint brush){
		this.brush = brush;
	}

	public void setTextToSpeech(TTS textToSpeech) {
		this.textToSpeech = textToSpeech;
	}
	
	public void run(){
		game_is_running = true;
	}
	
	public void stop(){
		game_is_running = false;
	}
	
	public void addEntity(Entity e){	
		entities.add(e);
	}
	
	public void removeEntity(Entity e){	
		removables.add(e);
	}
	
	/**
	 * If the previous background is not freed this method recycled it.
	 * 
	 * @param background the image that will be painted on the view background.
	 * */
	protected void setBackground(Bitmap background) {
		if(this.background != null){
			this.background.recycle();
		}
		this.background = background;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	/**
	 * Called before game loop beginning. It calls every onInit method of Entity.
	 * 
	 * */
	public void onInit() {
		Iterator<Entity> it = entities.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			e.onInit();
		}
		onInitialized = true;
	}
	
	/**
	 * Draws the background if this exits. Also It calls every onDraw method of Entity.      
	 * 
	 * @param canvas
	 * */
	protected void onDraw(Canvas canvas) {
		if(background != null)
			canvas.drawBitmap(background,0,0,brush);
		
		Iterator<Entity> it = renderables.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			e.onDraw(canvas);
		}
		
		renderables.clear();
	}
	
	/**
	 * Updates the background if this exits. Also It calls every onUpdate method of Entity
	 * and updates the buffers of game state.
	 * 
	 * */
	protected void onUpdate(){
		Entity e1,e2,e = null;
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext()){
			e = it.next();
			if(e.isCollidable() && e.isEnabled())
				collidables.add(e);
			if(e.isRenderable() && e.isEnabled())
				renderables.add(e);
			if(e.isRemovable())
				removables.add(e);
			if(!e.isFrozen()){
				e.onUpdate();
			}
		}
		
		Iterator<Entity> it1 = collidables.iterator();
		Iterator<Entity> it2;
		while(it1.hasNext()){
			e1 = it1.next();
			it2 = collidables.iterator();
			while(it2.hasNext()){
				e2 = it2.next();
				if (e1 != e2 & e1.collides(e2)) 
					e1.onCollision(e2);
			}
			
		}
		
		it  = removables.iterator();
		while(it.hasNext()){
			e = it.next();
			e.onRemove();
			entities.remove(e);
			collidables.remove(e);
			renderables.remove(e);
			e.delete();
		}
		
		collidables.clear();
		removables.clear();
		
	}
	
	/**
	 * Checks if e1 collides with another entity in the buffer Entities.
	 * 
	 * @param e1 Entity to be tested.
	 * @return The result of the test.
	 * */
	public boolean positionFreeEntities(Entity e1){
		Iterator<Entity> it = entities.iterator();
		boolean collision = false;
		Entity e;
		while (!collision && it.hasNext()){
			e = it.next();
			if (e.isCollidable() && e != e1){
				collision = e.collides(e1);
			}
		}
		return !collision;
	}


}