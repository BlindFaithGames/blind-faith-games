package org.example.tinyEngineClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class Game {
	
	private List<Entity> entities;
	private List<Entity> collidables;
	private List<Entity> renderables;
	private List<Entity> removables;
	// mas buffers si se necesitan .. private List<Entity> ;
	
	protected abstract void onDraw();
	
	protected abstract void onUpdate();
	
	private void _onDraw() {
		
		Iterator<Entity> it = collidables.iterator();
		Entity e;
		while(it.hasNext()){
			e = it.next();
			e.onDraw();
		}
		
		// cosas
		onDraw();
		
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
		List<Entity> collidablesCopy = new ArrayList<Entity>(collidables);
		while(it1.hasNext()){
			e1 = it1.next();
			while(it2.hasNext()){
				e2 = it2.next();
				e1.onCollision(e2);
			}
			
		}

		onUpdate();
		
		collidables.clear();
	}
	
	private void gameStep(){
		
		if(FPS.control()){
			_onUpdate();
			_onDraw();
		}
		
	}
	
	protected void addEntity(Entity e){	
		entities.add(e);
	}
	
	protected void removeEntity(Entity e){	
		removables.add(e);
	}
}
