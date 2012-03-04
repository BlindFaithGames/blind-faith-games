package com.accgames.tinyEngineClasses;

import java.util.ArrayList;

public class Animation {

	private String name;
	private ArrayList<Integer> frameList;
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
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void setFrameDelay(int framesPerStep) {
		this.framesPerStep = framesPerStep;
	}
}
