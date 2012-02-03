package org.example.tinyEngineClasses;

import java.util.Iterator;
import java.util.List;

import org.example.others.RuntimeConfig;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Entity {

	private String id;
	
	protected int x; //coordenadas

	protected int y;
	
	private Bitmap img; // imagen
	private AnimatedSprite anim; // animacion
	
	private boolean enabled; // habilitada
	private boolean collidable; // colisiona con otras entidades?
	private boolean visible; // ¿visible? esto determina si se dibuja o no 
	private boolean animated;

    private List<Mask> mask; // Mascara para colisiones posiblemente un Rect
    
	protected Game game;
	
	public Entity(int x, int y, Bitmap img, Game game, List<Mask> mask, boolean animated, int frameCount){
		this.x = x;
		this.y = y;
		this.img = img;
		this.game = game;
		this.mask = mask;
		this.animated = animated;
		enabled = true;
		collidable = true;
		visible = true;
		if(animated){
			anim = new AnimatedSprite();
			anim.Initialize(img, img.getHeight(), img.getWidth()/frameCount, frameCount);
			anim.play();
		}
	}
    
	/**
	 * By default draws the graphic of the entity
	 * 
	 * */
	protected  void onDraw(Canvas canvas){
		if(animated)
			anim.onDraw((int)x, (int)y,canvas);
		else{
			if(img != null)
				canvas.drawBitmap(img, x, y, null);
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
	
    /** Updates the animation entity and its mask set.*/
	protected void onUpdate(){
		if(animated)
			anim.onUpdate();
		
		if(mask != null){
			Iterator<Mask> it = mask.iterator();
			Mask m;
			while(it.hasNext()){
				m = it.next();
				m.onUpdate(x,y);
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
	
	public boolean equals(Object o){
		if(o instanceof Entity){
			Entity e = (Entity) o;
			return e.getId().equals(id);
		}
		else
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
	
	// SETTERS
	

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	public void playAnim(){
		if(animated)
			anim.play();
	}
	
	public void stopAnim(){
		if(animated)
			anim.stop();
	}
	
	public int getImgWidth(){
		if(!animated)
			return img.getWidth();	
		else
			return img.getWidth()/anim.getNumFrames();
	}
	
	public int getImgHeight(){
		return img.getHeight();	
	}
}