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

public abstract class Entity {

	private static final int N_TIMERS = 5;
	
	private String id;
	
	protected int x; //coordenadas

	protected int y;
	
	private Bitmap img; // imagen
	private SpriteMap animations;
	
	private boolean enabled; // habilitada
	private boolean collidable; // colisiona con otras entidades?
	private boolean visible; // �visible? esto determina si se dibuja o no 
	private boolean frozen; 
	private boolean removable;
	
    private int[] timers;
	
    private List<Mask> mask; // Mascara para colisiones posiblemente un Rect
    
    private List<Sound2D> sources; // sources list
    private boolean isPlaying;
    
	protected GameState gameState;

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
		this.animations = animations;
		timers = new int[N_TIMERS];
		for(int i = 0; i < N_TIMERS; i++){
			timers[i] = -1;
		}
		if(soundName != null) {
			Sound3DManager sm = Sound3DManager.getSoundManager(gameState.getContext());
			Source s = sm.addSource(soundName);
			Sound2D sound = new Sound2D(soundOffset, s);
			if(s != null){
				sources = new ArrayList<Sound2D>();
				sources.add(sound);
			}
			isPlaying = false;
		}
	}
    
	/**
	 * By default draws the graphic of the entity
	 * 
	 * */
	public  void onDraw(Canvas canvas){
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
	 * By default draws the graphic of the entity
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
	
	
    /** Updates the animation entity and its mask set.*/
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
	
	/** Collision event
            Actions to do when two entities collides.
            @param e the entity that collides with this
    */
	public abstract void onCollision(Entity e);
	
    /** Eventos de los Timers
    
            Contendr� las acciones a realizar cuando se active un timer.
            @param timer indica que timer se activa
    */
	public abstract void onTimer(int timer);

    /**  Comprueba el valor de un timer
            @param number se�ala el timer del que se devolver� el valor
            @return el valor del timer seleccionado
    */
    int getTimer(int number) {
    	return  0;
	}

    /** Establece el valor, en steps, de uno de los timers
            @param number selecciona el timer
            @param count selecciona el tiempo que le vamos a dar al timer
    */
    public void setTimer(int number,int count) {
    	if(number < N_TIMERS && number >= 0){
    		timers[number] = count;
    	}
	}
    
    /** Evento de Inicializaci�n
     Contendr� las acciones a realizar durante la inicializaci�n. 
     */
    public abstract void onInit();

    public abstract void onRemove();
	
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
	
	public void remove() {
		removable = true;
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
	
	/**
	 * Two entities collides if one of their mask collides with a mask of the other.
	 * 
	 * @param e2 entity that collides with entity this
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
	
	// GETTERS
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
	
	
	// SETTERS
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

	public void playAnim(String name, int framesPerStep, boolean loop){
		if(animations != null)
			animations.playAnim(name, framesPerStep, loop);
	}
	
	public void stopAnim(String name){
		if(animations != null)
			animations.stopAnim();
	}
	
	public int getImgWidth(){
		if(animations == null)
			return img.getWidth();	
		else
			return animations.getWidth();
	}
	
	public int getImgHeight(){
		if(animations == null)
			return img.getHeight();	
		else
			return animations.getHeight();
	}
	
	public List<Sound2D> getSources(){
		return sources;
	}
	
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
	}
	
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