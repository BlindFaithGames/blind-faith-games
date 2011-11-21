package org.example.tinyEngineClasses;

import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.view.View;


public abstract class Game {
	
	private View v;
	
	public static int DELAY = 1000/60;
	public static int FRAMES_PER_SECOND = 25;
	public static int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
	
    boolean game_is_running;
	
	private List<Entity> entities;
	private List<Entity> collidables;
	private List<Entity> renderables;
	private List<Entity> removables;
	// Más buffers si se necesitan .. private List<Entity> ;
	
	
	public Game(View v){
		game_is_running = false;
		this.v = v;
	}
	
	private void _onDraw(Canvas canvas) {
		
		Iterator<Entity> it = collidables.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			e.onDraw();
		}
		
		onDraw(canvas);
		
		renderables.clear();
	}
	
	private void _onUpdate(){
		
		Iterator<Entity> it = entities.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			if(e.isCollidable())
				collidables.add(e);
			if(e.isRenderable())
				renderables.add(e);
			e.onUpdate();
		}
		
		Iterator<Entity> it1 = collidables.iterator();
		Iterator<Entity> it2 = collidables.iterator();
		Entity e1,e2;
		while(it1.hasNext()){
			e1 = it1.next();
			while(it2.hasNext()){
				e2 = it2.next();
				e1.onCollision(e2);
			}
			
		}
				
		onUpdate();
		
		it  = removables.iterator();
		while(it.hasNext()){
			entities.remove(it);
			collidables.remove(it);
			renderables.remove(it);
		}
		
		collidables.clear();
	}
	
	public void run(){
		game_is_running = true;
	}
	
	public void stop(){
		game_is_running = false;
	}
	
	public void gameLoop(Canvas canvas){

	    long next_game_tick = System.currentTimeMillis();

	    long sleep_time = 0;

		while(game_is_running){
			
			next_game_tick += SKIP_TICKS;
			
			_onDraw(canvas);
			_onUpdate();
			
	        sleep_time = next_game_tick - System.currentTimeMillis();
	        
	        if(sleep_time >= 0) {
	            try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	        else {
	            // we are running behind!
	        }
			
		}
	}
	
	protected void addEntity(Entity e){	
		entities.add(e);
	}
	
	protected void removeEntity(Entity e){	
		removables.add(e);
	}
	
	protected abstract void onDraw(Canvas canvas);
	
	protected abstract void onUpdate();
	
}
