package com.accgames.graphics;

import java.util.ArrayList;

/**
 * This class represents a simple animation.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */

public class Animation {

	private String name; //  name of the animation
	private ArrayList<Integer> frameList; // List that contains the order which will be played this animation
	private boolean loop; // Indicates if the animation loops
	private int frameCount; // Counts the last frame showed
	private int framesPerStep; // Number of frames per game step
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param name Name of the animation.
	 * @param frameList List that contains the order which will be used to play this animation.
	 * @param framesPerStep Number of frames per game step.
	 * @param loop Indicates if the animation loops.
	 * 
	 * */
	
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
