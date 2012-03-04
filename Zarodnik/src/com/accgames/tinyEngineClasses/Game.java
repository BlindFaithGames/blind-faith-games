package com.accgames.tinyEngineClasses;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

public class Game {
	
	private List<GameState> gameStates;
	private GameState actualState;
	
	private List<Integer> order; // Defines a default order for the states transition. It can be changed during the game execution
	
	private int next;
	private boolean endGame;
	private boolean stateChangedLastStep;
	
	int clearCanvas = 0; // two gameSteeps needed to clear the canvas content.
	
	public Game() {
	}
	
	public Game(List<GameState> gameStates, ArrayList<Integer> order){
		this.gameStates = gameStates;
		this.order = order;
		next = 0;
		if(next < order.size()){
			if(gameStates != null  && gameStates.size() > 0){
				actualState = gameStates.get(order.get(next));
			}
		}
		stateChangedLastStep = false;
	}
	
	public int getNext() {
		return next;
	}
	
	public List<Integer> getOrder(){
		return order;
	}
	
	public void onInit() {
		actualState.onInit();
	}

	public void onDraw(Canvas canvas) {
		if(stateChangedLastStep){
			clearCanvas(canvas);
		}
		actualState.onDraw(canvas);
	}

	public void onUpdate() {
		if(stateChangedLastStep){
			if(!actualState.isOnInitialized())
				actualState.onInit();
		}
		
		actualState.onUpdate();
		isThereChangeState();
	}
	
	public void isThereChangeState(){
		if(!actualState.isRunning()){
			next++;
			if(next < order.size()){
				actualState = gameStates.get(order.get(next));
				actualState.run();
				stateChangedLastStep = true;
			}
			else
				endGame = true;
		}
	}
	
	public void changeOrder(ArrayList<Integer> order){
		this.order = order;
	}
	
	public boolean isEndGame(){
		return endGame;
	}
	
	private void clearCanvas(Canvas canvas) {
		clearCanvas++;
		if(clearCanvas == 2){
			clearCanvas = 0;
			stateChangedLastStep = false;
		}
		canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
	}

	public void initialize(ArrayList<GameState> gameStates,ArrayList<Integer> order) {
		this.gameStates = gameStates;
		this.order = order;
		next = 0;
		if(next < order.size()){
			if(gameStates != null  && gameStates.size() > 0){
				actualState = gameStates.get(order.get(next));
			}
		}
		stateChangedLastStep = false;
	}
}

	