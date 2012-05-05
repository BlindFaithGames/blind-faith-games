package es.eucm.blindfaithgames.engine.input;

import java.util.HashMap;
import java.util.Map;



import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 
 * Event Mapped on view. When an event take place on the 
 * view the game stores it in the buffers of this class t
 * that will be consulted by its own onUpdate.  
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */

public class Input {

	private Map<String,EventType> events; // Event Mapped 
	private static Input input = null; // singleton 

	private static XMLKeyboard keyboard; // XML keyboard for custom configuration of controls
	
	/**
	 * Encapsulates information about a generic event.
	 * 
	 * @author Javier Álvarez & Gloria Pozuelo.
	 * 
	 * */	
	public class EventType{
		private float dvx, dvy;
		private Object e, e2;
		
		/**
		 * Unique constructor of the class.
		 * 
		 * @param e initial MotionEvent that starts event.
		 * @param e2  final MotionEvent.
		 * @param dvx e2 offset on the x axis 
		 * @param dvy e2 offset on the y axis 
		 * 
		 * */
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
	
	/**
	 * Unique constructor of the class.
	 * 
	 * */
	public Input() {
		events = new HashMap<String, Input.EventType>();
	}
	
	/**
	 * Gets a unique instance of Input. Singleton pattern.
	 * 
	 * @return An instance of Input.
	 * 
	 * */
	public static Input getInput() {
		if (input == null) {
			input = new Input();
		}
		return input;
	}
	
	/**
	 * Gets a unique instance of keyboard. Singleton pattern.
	 * 
	 * @return An instance of XMLKeyboard.
	 * 
	 * */
	public static XMLKeyboard getKeyboard() {
		if (keyboard == null) {
			keyboard = new XMLKeyboard();
		}
		return keyboard;
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------

	public EventType getEvent(String key){
		return events.get(key);
	}
	
	public boolean hasEvents(){
		return events.size() != 0;
	}

// ----------------------------------------------------------- Others -----------------------------------------------------------
	
	/**
	 * Adds a motion event.
	 * 
	 * @param type the type of MotionEvent.
	 * @param e initial MotionEvent that starts event.
	 * @param e2 final MotionEvent.
	 * @param dvx e2 offset on the x axis 
	 * @param dvy e2 offset on the y axis 
	 * */
	public void addEvent(String type, MotionEvent e, MotionEvent e2, float dvx, float dvy) {
		events.put(type, new EventType(e,e2,dvx,dvy));
	}
	
	/**
	 * Adds a key event.
	 * 
	 * @param type the type of KeyEvent.
	 * @param event
	 * @param dvx event offset on the x axis 
	 * @param dvy event offset on the y axis 
	 * 
	 * */
	public void addEvent(String type, KeyEvent event, int dvx,int dvy) {
		events.put(type, new EventType(event,null,dvx,dvy));
	}
	
	/**
	 * Removes a event  the given key.
	 * @param key Event name that was given when it was added to the class buffer.
	 * 
	 * */
	public void remove(String key){
		events.remove(key);
	}
	
	/**
	 * Removes an event from the class buffer.
	 * 
	 * @param key Event name that was given when it was added to the class buffer.
	 * 
	 * @return the event that was removed.
	 * 
	 * */
	public EventType removeEvent(String key){
		EventType e = events.get(key);
		events.remove(key);
		return e;
	}
	
	/**
	 * Removes all event in the class buffer.
	 * 
	 * */
	public void clean() {
		events.clear();
	}
	
}


