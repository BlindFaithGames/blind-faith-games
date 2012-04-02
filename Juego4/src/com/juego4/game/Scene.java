package com.juego4.game;

import java.util.ArrayList;


public class Scene {

	private int npc;
	private ArrayList<NPC> npcs;
	private int id;
	private SceneType type;
	private ArrayList<Integer> nextScenes;		/* Scene ids from the next possible scenes */
	private String introMsg, description;
	
	
	public Scene(ArrayList<NPC> npcs, int id, SceneType type, ArrayList<Integer> next, String introMsg, String description){
		this.npcs = npcs;
		this.id = id;
		this.type = type;
		this.nextScenes = next;
		this.introMsg = introMsg;
		this.description = description;
	}
	
}
