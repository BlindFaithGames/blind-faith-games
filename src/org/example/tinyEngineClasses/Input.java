package org.example.tinyEngineClasses;

import java.util.ArrayList;

import org.example.golf.XML.XMLKeyboard;

import android.view.MotionEvent;
import android.view.View;


/** 
 * Mapeado de eventos sobre vista. Cuando se produzca un evento en la vista de un juego se almacenara en buffers de
 * esta clase que seran consultados por las entidades del juego en sus respectivos onUpdate. Candidato a singleton
 * */

public class Input {

	private View v;
	private ArrayList<MotionEvent> events;
	private static Input input = null;
	
	private static XMLKeyboard keyboard;
	
	private Input(View v) {
		this.v = v;
		events = new ArrayList<MotionEvent>();
	}
	
	public Input getInput(View v){
		if(input != null)
			return input;
		else{
			input = new Input(v);
			return input; 
		}
	}

	public static XMLKeyboard getKeyboard(){
		if (keyboard == null){
			keyboard = new XMLKeyboard();
			// By default
		}
		return keyboard;
	}
	
	public void addEvent(MotionEvent e){
		events.add(e);
	}
	
}
