package org.example.tinyEngineClasses;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;

public class Game {

	private List<GameState> gameStates;
	private GameState actualState;
	
	private List<Integer> order; // Defines a default order for the states transition. It can be changed during the game execution
	
	private int next;
	private boolean endGame;
	private boolean stateChangedLastStep;
	
	public Game(List<GameState> gameStates, ArrayList<Integer> order){
		this.gameStates = gameStates;
		this.order = order;
		next = 0;
		if(gameStates != null  && gameStates.size() > 0){
			actualState = gameStates.get(next);
		}
		stateChangedLastStep = false;
	}
	
	public void onInit() {
		actualState.onInit();
	}

	public void onDraw(Canvas canvas) {
		actualState.onDraw(canvas);
		if(!actualState.isRunning()){
			canvas.drawColor(Color.BLACK);
		}
	}

	public void onUpdate() {
		if(stateChangedLastStep){
			actualState.onInit();
			stateChangedLastStep = false;
		}
		
		actualState.onUpdate();
		isThereChangeState();
	}
	
	public void isThereChangeState(){
		if(!actualState.isRunning()){
			next++;
			if(next < order.size()){
				actualState = gameStates.get(order.get(next));
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
}
