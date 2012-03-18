package com.accgames.input;

import java.util.HashMap;
import java.util.Map;



import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 
 * Event Mapped on view. When an event take place on the 
 * view the game stores it in the buffers of this class t
 * that will be consulted by in its own onUpdate.  
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */

public class Input {

	private Map<String,EventType> events; // Event Mapped 
	private static Input input = null;

	private static XMLKeyboard keyboard; // XML keyboard for custom configuration of controls
	
	/**
	 * Encapsulates information about generic events.
	 * 
	 * */
	public class EventType{
		private float dvx, dvy;
		private Object e, e2;

		public EventType(Object e, Object e2, float dvx, float dvy){
			this.e = e;
			this.e2 = e2;
			this.dvx = dvx;
			this.dvy = dvy;
		}
		
		// ----------------------------------------------------------- Getters ----------------------------------------------------------- 
		public Point getDistance(){
			MotionEvent e = (MotionEvent) this.e;
			MotionEvent e2 = (MotionEvent) this.e2;
			return new Point((int)(e.getRawX() - e2.getRawX()),(int) (e.getRawY() - e2.getRawY()));
		}
		
		public float getDvx() {
			return dvx;
		}

		public float getDvy() {
			return dvy;
		}

		public KeyEvent getKeyEventE1() {
			return (KeyEvent) e;
		}

		public KeyEvent getKeyEventE2() {
			return (KeyEvent) e2;
		}
	
		public MotionEvent getMotionEventE1() {
			return (MotionEvent) e;
		}

		public MotionEvent getMotionEventE2() {
			return (MotionEvent) e2;
		}
		
	}
	
	public Input() {
		events = new HashMap<String, Input.EventType>();
	}
	
	/**
	 * Get a unique instance of Input
	 * 
	 * @return An instance of Input
	 * 
	 * */
	public static Input getInput() {
		if (input == null) {
			input = new Input();
		}
		return input;
	}
	
	/**
	 * Get a unique instance of keyboard
	 * 
	 * @return An instance of XMLKeyboard
	 * 
	 * */
	public static XMLKeyboard getKeyboard() {
		if (keyboard == null) {
			keyboard = new XMLKeyboard();
		}
		return keyboard;
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------

	/**
     *  Gets an event by action
     * */
	public EventType getEvent(String key){
		return events.get(key);
	}
	
    /**
     * To check if there are some event
     * */
	public boolean hasEvents(){
		return events.size() != 0;
	}

// ----------------------------------------------------------- Setters -----------------------------------------------------------
	
	/**
	 * Adds a motion event
	 * */
	public void addEvent(String type, MotionEvent e, MotionEvent e2, float dvx, float dvy) {
		events.put(type, new EventType(e,e2,dvx,dvy));
	}
	
	/**
	 * Adds a key event
	 * */
	public void addEvent(String type, KeyEvent event, int dvx,int dvy) {
		events.put(type, new EventType(event,null,dvx,dvy));
	}
	
	/**
	 * Adds a key event
	 * */
	public void remove(String key){
		events.remove(key);
	}
	
	/**
	 * Adds a key event
	 * */
	public EventType removeEvent(String key){
		EventType e = events.get(key);
		events.remove(key);
		return e;
	}
	
	public void clean() {
		events.clear();
	}
	
}


