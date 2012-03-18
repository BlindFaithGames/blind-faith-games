package com.accgames.graphics;

import java.util.ArrayList;

/**
 * This class contains only a simple animation.
 * 
 * @author Javier Álvarez & Gloria Pozuelo 
 * 
 * */

public class Animation {

	private String name; // animation name
	private ArrayList<Integer> frameList; // 
	private boolean loop;
	private int frameCount;
	private int framesPerStep;
	
	public Animation(String name, ArrayList<Integer> frameList, int framesPerStep, boolean loop){
		this.name = name;
		this.frameList = frameList;
		this.framesPerStep = framesPerStep;
		this.loop = loop;
		this.frameCount = frameList.size();
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------  
	
	public boolean isLoop() {
		return loop;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Integer> getFrameList() {
		return frameList;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public int getFramesPerStep() {
		return framesPerStep;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void setFrameDelay(int framesPerStep) {
		this.framesPerStep = framesPerStep;
	}
}
