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
		game_is_running = true;
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
			e.onUpdate();
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
		
		/*it = entities.iterator();
		while(it.hasNext()){
			e = it.next();
			if(e.isRemovable())
				removables.add(e);
		}*/
	
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
	
	public boolean isRunning(){
		return game_is_running;
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
	
	public boolean positionFreeEntities(Entity e1){
		Iterator<Entity> it = entities.iterator();
		Iterator<Mask> i;
		boolean empty = true;
		Entity e; Mask m; int w,h;
		while (empty && it.hasNext()){
			e = it.next();
			if (e.isCollidable()){
				i = e.getMask().iterator();
				while (empty && i.hasNext()){
					m = i.next();
					if (m instanceof MaskBox){
						w = ((MaskBox)m).getWidth();
						h = ((MaskBox)m).getHeight();
					} else{
						w = h = ((MaskCircle)m).getRadius();
					}
						
					empty = !encloses(w, h, e1, e.getX(),e.getY());
				}
			}
		}
		return empty;
	}

	private boolean encloses(int w1, int h1, Entity e, float ex, float ey) {
		// para cada máscara de e
		Iterator<Mask> i = e.getMask().iterator();
		boolean ok = true; Mask m;
		int w, h;
		while (ok && i.hasNext()){
			m = i.next();
			if (m instanceof MaskBox){
				w = ((MaskBox)m).getWidth();
				h = ((MaskBox)m).getHeight();
			} else{
				w = h = ((MaskCircle)m).getRadius();
			}
			if ((ey <= e.getY() + h) && (ey + h1 >= e.getY()))			// width match
				if ((ex <= e.getX() + w) && (ex + w1 >= e.getX()))		// heigh match
					ok = false;
			
		}

		return !ok;
	}
}