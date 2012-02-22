package org.example.tinyEngineClasses;

import java.util.HashMap;
import java.util.Map;

import org.example.golf.XML.XMLKeyboard;

import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Mapeado de eventos sobre vista. Cuando se produzca un evento en la vista de
 * un juego se almacenara en buffers de esta clase que seran consultados por las
 * entidades del juego en sus respectivos onUpdate. Candidato a singleton
 * */

public class Input {

	private Map<String,EventType> events;
	private static Input input = null;

	private static XMLKeyboard keyboard;
	
	public class EventType{
		private float dvx, dvy;
		private Object e, e2;

		public EventType(Object e, Object e2, float dvx, float dvy){
			this.e = e;
			this.e2 = e2;
			this.dvx = dvx;
			this.dvy = dvy;
		}
		
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
	
	public static Input getInput() {
		if (input == null) {
			input = new Input();
		}
		return input;
	}

	public static XMLKeyboard getKeyboard() {
		if (keyboard == null) {
			keyboard = new XMLKeyboard();
		}
		return keyboard;
	}

	public void addEvent(String type, MotionEvent e, MotionEvent e2, float dvx, float dvy) {
		events.put(type, new EventType(e,e2,dvx,dvy));
	}
	
	public void addEvent(String type, KeyEvent event, int dvx,int dvy) {
		events.put(type, new EventType(event,null,dvx,dvy));
	}
	
	public EventType getEvent(String key){
		return events.get(key);
	}
	
	public EventType removeEvent(String key){
		EventType e = events.get(key);
		events.remove(key);
		return e;
	}
	
	public void remove(String key){
		events.remove(key);
	}
	
	public boolean hasEvents(){
		return events.size() != 0;
	}

	public void clean() {
		events.clear();
	}

}


