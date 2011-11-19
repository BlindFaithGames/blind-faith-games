package org.example.tinyEngineClasses;

import java.util.List;


import android.graphics.Bitmap;

public abstract class Entity {

	
	private float x,y; //coordenadas
	
	private Bitmap img; // imagen
	
	private boolean enabled; // habilitada
	private boolean collidable; // colisiona con otras entidades?
	private boolean visible; // �visible? esto determina si se dibuja o no 
	private boolean frozen; // a decidir si se mantiene xD
	
    private int[] timers;
	
    private List<Mask> mask; // Mascara para colisiones posiblemente un Rect
    
	private Game game;
    
	public abstract void onDraw();
	
    /** Evento de actualizaci�n
     Contendr� las acciones a realizar durante un paso del juego. */
	public abstract void onUpdate();
	
	
    /** Evento de colisi�n
    
            Contendr� las acciones a realizar cuando se produzca una colisi�n.
            @param other el par de colisi�n entre las m�scaras, donde el primer campo se refiere a esta entidad y el segundo a la otra.
            @param e la entidad con la que colisiona
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
    void setTimer(int number,int count) {
	}
    
    /** Evento de Inicializaci�n
     Contendr� las acciones a realizar durante la inicializaci�n. 
     */
    public abstract void onInit();

	public boolean isCollidable() {
		return collidable;
	}

	public boolean isRenderable() {
		return visible;
	}

	public void collides(Entity e2) {
		// TODO Auto-generated method stub
		
	}
}
