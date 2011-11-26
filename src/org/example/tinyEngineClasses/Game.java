package org.example.tinyEngineClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;


public abstract class Game {
	
	protected View v;
	
	protected Activity context;
	
    boolean game_is_running;
	
	private List<Entity> entities;
	private List<Entity> collidables;
	private List<Entity> renderables;
	private List<Entity> removables;
	
	public Game(View v, Context context){
		game_is_running = false;
		this.context = (Activity) context;
		this.v = v;
		entities = new ArrayList<Entity>();
		collidables = new ArrayList<Entity>();
		renderables = new ArrayList<Entity>();
		removables = new ArrayList<Entity>();
	}
	
	protected void onDraw(Canvas canvas) {
		
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
}
