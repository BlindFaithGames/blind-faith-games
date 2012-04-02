package com.juego4.game;

import java.util.ArrayList;

public class SceneManager {

	private Scene currentScene;
	
	private ArrayList<Scene> sceneBuffer;
	private int numScenes;
	
	public SceneManager(ArrayList<Scene> scenes){
		sceneBuffer = scenes;
	}

	
}
