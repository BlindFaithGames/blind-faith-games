package org.example.tinyEngineClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.example.others.RuntimeConfig;
import org.pielot.openal.Source;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

public abstract class Entity {

	private String id;
	
	protected int x; //coordenadas

	protected int y;
	
	private Bitmap img; // imagen
	//private AnimatedSprite anim; // animacion
	private SpriteMap animations;
	
	private boolean enabled; // habilitada
	private boolean collidable; // colisiona con otras entidades?
	private boolean visible; // ¿visible? esto determina si se dibuja o no 
	//private boolean frozen; 
	private boolean removable;
	
    //private int[] timers;
	
    private List<Mask> mask; // Mascara para colisiones posiblemente un Rect
    
    private List<Sound2D> sources; // sources list
    private boolean isPlaying;
    
	protected Game game;


	public Entity(int x, int y, Bitmap img, Game game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset){
		this.x = x;
		this.y = y;
		this.img = img;
		this.game = game;
		this.mask = mask;
		enabled = true;
		collidable = true;
		visible = true;
		this.animations = animations;
		if(soundName != null) {
			SoundManager sm  = SoundManager.getSoundManager(game.getContext());
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
	protected  void onDraw(Canvas canvas){
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
	protected void onUpdate(){
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
	}
	
	
    /** Collision event
            Actions to do when two entities collides.
            @param e the entity that collides with this
    */
	public abstract void onCollision(Entity e);
	
    /** Eventos de los Timers
    
            Contendrá las acciones a realizar cuando se active un timer.
            @param timer indica que timer se activa
    */
	public abstract void onTimer(int timer);

    /**  Comprueba el valor de un timer
            @param number señala el timer del que se devolverá el valor
            @return el valor del timer seleccionado
    */
    int getTimer(int number) {
    	return  0;
	}

    /** Establece el valor, en steps, de uno de los timers
            @param number selecciona el timer
            @param count selecciona el tiempo que le vamos a dar al timer
    */
    void setTimer(int number,int count) {
	}
    
    /** Evento de Inicialización
     Contendrá las acciones a realizar durante la inicialización. 
     */
    public abstract void onInit();

    public abstract void onRemove();
	
	public boolean isCollidable() {
		return collidable;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	private List<Mask> getMask() {
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
	
	public void stopAllSources(){
		Iterator<Sound2D> it = sources.iterator();
		Sound2D s;
		while(it.hasNext()){
			s = it.next();
			s.getS().stop();
		}
		isPlaying = false;
	}
}