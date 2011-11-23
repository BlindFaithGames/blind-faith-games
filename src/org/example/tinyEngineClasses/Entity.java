package org.example.tinyEngineClasses;

import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;

public abstract class Entity {

	private String id;
	
	private float x,y; //coordenadas
	
	private Bitmap img; // imagen
	
	private boolean enabled; // habilitada
	private boolean collidable; // colisiona con otras entidades?
	private boolean visible; // ¿visible? esto determina si se dibuja o no 
	private boolean frozen; // a decidir si se mantiene xD
	
    private int[] timers;
	
    private List<Mask> mask; // Mascara para colisiones posiblemente un Rect
    
	private Game game;
	
	public Entity(float x, float y, Bitmap img, Game game, List<Mask> mask){
		this.x = x;
		this.y = y;
		this.img = img;
		this.game = game;
		this.mask = mask;
		enabled = true;
		collidable = true;
		visible = true;
		frozen = false;
	}
    
	public abstract void onDraw();
	
    /** Evento de actualización
     Contendrá las acciones a realizar durante un paso del juego. */
	public abstract void onUpdate();
	
	
    /** Evento de colisión
    
            Contendrá las acciones a realizar cuando se produzca una colisión.
            @param other el par de colisión entre las máscaras, donde el primer campo se refiere a esta entidad y el segundo a la otra.
            @param e la entidad con la que colisiona
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

	private List<Mask> getMask() {
		return mask;
	}
	
	public boolean isRenderable() {
		return visible;
	}

	public boolean collides(Entity e2) {
		Iterator<Mask> it1 = mask.iterator();
		Iterator<Mask> it2;
		Mask m1,m2;
		boolean found = false;
		while(it1.hasNext() && !found){
			m1 = it1.next();
			it2 = e2.getMask().iterator();
			while(it2.hasNext() && !found){
				m2 = it2.next();
				found = m1.collide(m2);
			}
		}
		return found;
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
	

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

}
