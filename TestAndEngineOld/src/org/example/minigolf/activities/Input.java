package org.example.minigolf.activities;

import java.util.ArrayList;
import java.util.Iterator;

import org.example.minigolf.XML.XMLKeyboard;

import android.view.MotionEvent;

/**
 * Mapeado de eventos sobre vista. Cuando se produzca un evento en la vista de
 * un juego se almacenara en buffers de esta clase que seran consultados por las
 * entidades del juego en sus respectivos onUpdate. Candidato a singleton
 * */

public class Input {

//	private View v;
	private ArrayList<EventType> events;
	private static Input input = null;

	private static XMLKeyboard keyboard;
	
	public class Point{
		float x;
		float y;
		
		public Point(float x, float y){
			this.x = x;
			this.y = y;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}
		
		
	}
	
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
		events = new ArrayList<EventType>();
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
		events.add(new EventType(type,e,e2,dvx,dvy));
	}
	
	/**
	 * Removes the first event
	 */
	public void removeEvent(){
		if (events.size() != 0)
			events.remove(0);
	}
	
	public String getTypeNextEvent(){
		if (events.size() != 0)
			return events.get(0).getType();
		else return "";	
	}
	
	public boolean isScroll(){
		return this.getTypeNextEvent().equals("onScroll");
	}
	
	public void removeNextScroll(){
		Iterator<EventType> it = events.iterator();
		boolean ok = true;
		int i = 0;
		if (isScroll()){
			while (ok && it.hasNext()){
				EventType e = it.next();
				ok = e.getType().equals("onScroll");
				if (ok) i++;
			}
		}
		while (i != 0){
			events.remove(i);
			i--;
		}
	}
	
	public boolean hasEvents(){
		return events.size() != 0;
	}
	
	public String getType(int i){
		if (i < events.size())
			return events.get(i).getType();
		else return "";
	}

	/**
	 * Returns the total distance if the next event is a scroll type. If not returns (0,0).
	 * @return
	 */
	public Point getDistance(){
		float totalX = 0; float totalY = 0;
		Iterator<EventType> it = events.iterator();
		boolean ok = true;
		if (isScroll()){
			while (ok && it.hasNext()){
				EventType e = it.next();
				ok = e.getType().equals("onScroll");
				if (ok){
					totalX += e.getDvx();
					totalY += e.getDvy();
				}
			}
		}
		return new Point(totalX, totalY);
	}
}
