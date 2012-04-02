package com.juego4.input;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.juego4.game.NPC;
import com.juego4.game.Scene;
import com.juego4.game.SceneManager;
import com.juego4.game.SceneType;
/**
 * 
 * Class to manage events sent by a parser SAX.
 * 
 * @author Gloria Pozuelo and Javier Álvarez.
 */
public class SAXHandler extends DefaultHandler {
	// Scenes
	private ArrayList<Scene> scenes;
	private ArrayList<NPC> npcs;
	private SceneManager sceneManager;
	private SceneType type;
	private int id;
	private ArrayList<Integer> nextScenes;
	private boolean isDescription = false, isIntroMsg = false;
	private String description, introMsg;
	
	// NPCs
	private boolean isIntro = false;
	private String intro, name;

	
	public SceneManager getSceneManager() {
		return sceneManager;
	}
	
// --------------------------------------------------------------------- //
/* DEFINE METHODS OF DefaultHandler */
// --------------------------------------------------------------------- //
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}
	
	public void startDocument(){	
		nextScenes = new ArrayList<Integer>();
		scenes = new ArrayList<Scene>();
		npcs = new ArrayList<NPC>();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes att){
		if (qName.equals("scene")){
			type = SceneType.valueOf(att.getValue("type"));
			id = Integer.parseInt(att.getValue("id"));
		}
		else if (qName.equals("introMessage")){
			isIntroMsg = true;
		}
		else if (qName.equals("description")){
			isDescription = true;
		}
		else if (qName.equals("idScene")){
			nextScenes.add(Integer.parseInt(att.getValue("id")));
		}
		else if (qName.equals("endSceneCondition")){
			// TODO
		}
		// NPC
		else if (qName.equals("npc")){
			name = att.getValue("name");
		}
		else if (qName.equals("intro")){
			isIntro = true;
		}
		else if (qName.equals("dialog")){
			
		}
	}


	public void endElement(String uri, String localName, String qName){
		if (qName.equals("scene")){
			scenes.add(new Scene(npcs, id, type, nextScenes, introMsg, description));
			npcs.clear();
			nextScenes.clear();
		}		
		else if (qName.equals("npc")){
//			npcs.add(new NPC(name, intro, dialog));
//			dialog.clear();
		}
		else if (qName.equals("sceneManager")){
			sceneManager = new SceneManager(scenes);
		}
	}
	
	public void characters(char ch[], int start, int length) { 
		if (isDescription) {
			description = new String(ch, start, length);
			isDescription = false;
		}
		if (isIntroMsg) {
			introMsg = new String(ch, start, length);
			isIntroMsg = false;
		}
		if (isIntro){
			intro = new String(ch, start, length);
			isIntro = false;
		}
 
	}
}

