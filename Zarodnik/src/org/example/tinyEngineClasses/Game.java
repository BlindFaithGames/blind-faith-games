package org.example.tinyEngineClasses;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Input.EventType;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


public abstract class Game {
	
	protected View v;
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	protected Activity context;
	
	private Bitmap background = null;
	
    boolean game_is_running;
	
	private List<Entity> entities;
	private List<Entity> collidables;
	private List<Entity> renderables;
	private List<Entity> removables;

	private TTS textToSpeech;

	private Paint brush;
	
	public Game(View v, Context context, TTS textToSpeech){
		game_is_running = false;
		this.context = (Activity) context;
		this.v = v;
		this.textToSpeech = textToSpeech;
		entities = new ArrayList<Entity>();
		collidables = new ArrayList<Entity>();
		renderables = new ArrayList<Entity>();
		removables = new ArrayList<Entity>();
		brush = null;
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		SCREEN_WIDTH = display.getWidth();
		SCREEN_HEIGHT = display.getHeight();
	}
	
	public void onInit() {
		Iterator<Entity> it = entities.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			e.onInit();
		}
	}
	
	protected void onDraw(Canvas canvas) {
		
		canvas.drawBitmap(background,0,0,brush);
		
		Iterator<Entity> it = renderables.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			e.onDraw(canvas);
		}
		
		renderables.clear();
	}
	
	protected void onUpdate(){
		
		Iterator<Entity> it = entities.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			if(e.isCollidable() && e.isEnabled())
				collidables.add(e);
			if(e.isRenderable() && e.isEnabled())
				renderables.add(e);
			if(e.isRemovable())
				removables.add(e);
			e.onUpdate();
		}
		
		Iterator<Entity> it1 = collidables.iterator();
		Iterator<Entity> it2;
		Entity e1,e2;
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
			entities.remove(it);
			collidables.remove(it);
			renderables.remove(it);
		}
		
		collidables.clear();
		
		EventType event = Input.getInput().removeEvent("onKeySearch");
		if(event != null){
			RuntimeConfig.IS_DEBUG_MODE = !RuntimeConfig.IS_DEBUG_MODE;
		}
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
	
	public View getView(){
		return v;
	}
	
	public Activity getContext(){
		return context;
	}
	
	/**
	 * if the previous background is not freed this method recycled it.
	 * 
	 * */
	protected void setBackground(Bitmap background) {
		if(this.background != null)
			this.background.recycle();
		this.background = background;
	}
	
	public TTS getTTS(){
		return textToSpeech;
	}
	
	public void editBackground(Paint brush){
		this.brush = brush;
	}
}