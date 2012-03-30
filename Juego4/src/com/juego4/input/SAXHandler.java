package com.juego4.input;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.juego4.game.SceneManager;
/**
 * 
 * Class to manage events sent by a parser SAX.
 * 
 * @author Gloria Pozuelo and Javier Álvarez.
 */
public class SAXHandler extends DefaultHandler {
	private SceneManager k;
	private int counter;
	private String action;
	private int key;

	
	public SceneManager getSceneManager() {
		return k;
	}
	
// --------------------------------------------------------------------- //
/* DEFINE METHODS OF DefaultHandler */
// --------------------------------------------------------------------- //
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}
	
	public void startDocument(){	
		counter = 0;
	}	
	
	public void startElement(String uri, String localName, String qName, Attributes att){
		if (qName.equals("keyboard")){			
			k = new SceneManager();
		//	k.setNum(Integer.parseInt(att.getValue("num")));
		}
		else if (qName.equals("rowmap")){
			action = att.getValue("action");
			key = Integer.parseInt(att.getValue("key"));
		}	
	}

	public void endElement(String uri, String localName, String qName){
		if (qName.equals("rowmap")){
			//k.addObject(key, action);
			counter++;
		}		
		else if (qName.equals("keyboard")){
		//	if (counter != k.getNum())
			//	k.riseNumberOfErrors(1);
		}
	}
}

