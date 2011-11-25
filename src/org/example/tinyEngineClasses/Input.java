package org.example.tinyEngineClasses;

import java.util.HashMap;
import java.util.Map;

import org.example.golf.XML.XMLKeyboard;

import android.view.MotionEvent;

/**
 * Mapeado de eventos sobre vista. Cuando se produzca un evento en la vista de
 * un juego se almacenara en buffers de esta clase que seran consultados por las
 * entidades del juego en sus respectivos onUpdate. Candidato a singleton
 * */

public class Input {

//	private View v;
	private Map<String,EventType> events;
	private static Input input = null;

	private static XMLKeyboard keyboard;
	
	public class EventType{
		private String type;
		private float dvx, dvy;
		private MotionEvent e, e2;

		public EventType(String type, MotionEvent e, MotionEvent e2, float dvx, float dvy){
			this.type = type;
			this.e = e;
			this.e2 = e2;
			this.dvx = dvx;
			this.dvy = dvy;
		}
			
		public String getType() {
			return type;
		}

		public float getDvx() {
			return dvx;
		}

		public float getDvy() {
			return dvy;
		}

		public MotionEvent getE() {
			return e;
		}

		public MotionEvent getE2() {
			return e2;
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
			// By default
		}
		return keyboard;
	}

	public void addEvent(String type, MotionEvent e, MotionEvent e2, float dvx, float dvy) {
		events.put(type,new EventType(type,e,e2,dvx,dvy));
	}
	
	public EventType getEvent(String key){
		EventType e = events.get(key);
		events.remove(key);
		return e;
	}
	
	public boolean hasEvents(){
		return events.size() != 0;
	}
}


