package es.eucm.blindfaithgames.engine.general;

import java.util.ArrayList;
import java.util.List;

import org.acra.ErrorReporter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;


/**
 * State machine that contains a list of game state and an order that indicates how to be used each of them.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */

public class Game {
	
	private List<GameState> gameStates; // List of game State that contains our game
	private GameState currentState; // Active game state
	
	private List<Integer> order; // Defines a default order for the states transition. It can be changed during the game execution
	
	private int next; // Next position in the list order. It will contain an integer used to access to an appropriated game state in gameStates
	private boolean endGame; // Used to finish a game
	private boolean stateChangedLastStep; // It's used to fill the canvas with black if a state change has happened
	
	private int clearCanvas = 0; // Two game steps are needed to clear the canvas surface
	
	private boolean disabled; // To stop a game
	
	/**
	 * Default constructor.
	 * */
	public Game() {
		disabled = false;
	}
	
	/**
	 * Main constructor of the class.
	 * 
	 * @param gameStates List of game State that contains our game.
	 * @param order Defines a default order for the states transition. It can be changed during the game execution.
	 * */
	public Game(List<GameState> gameStates, ArrayList<Integer> order){
		this.gameStates = gameStates;
		this.order = order;
		next = 0;
		disabled = false;
		if(next < order.size()){
			if(gameStates != null  && gameStates.size() > 0){
				currentState = gameStates.get(order.get(next));
			}
		}
		stateChangedLastStep = false;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	
	public int getNext() {
		return next;
	}
	
	public List<Integer> getOrder(){
		return order;
	}

	public boolean getDisabled() {
		return disabled;
	}
	
	public boolean isEndGame(){
		return endGame;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------	
	
	public void setDisabled(boolean disabled){
		this.disabled = disabled;
	}
	
	public void changeOrder(ArrayList<Integer> order){
		this.order = order;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	/**
	 * Called before the game loop beginning.
	 * 
	 * */
	public void onInit() {
		currentState.onInit();
	}
	/**
	 *  
	 *  Manages the game render.
	 *  Fills the canvas with black if a state change happens calls game state onDraw method.
	 *  
	 *  @param canvas The surface that will be painted.
	 *
	 * */
	public void onDraw(Canvas canvas) {
		if(stateChangedLastStep){
			clearCanvas(canvas);
		}
		currentState.onDraw(canvas);
		if(disabled){
			canvas.drawColor(Color.BLACK);
		}
	}
	
	/**
	 * 
	 * Manages the game logic.
	 * If a state change happens calls game state onUpdate method.
	 *
	 * */
	public void onUpdate() {
		if(stateChangedLastStep){
			if(!currentState.isOnInitialized())
				currentState.onInit();
		}
		
		currentState.onUpdate();
		isThereChangeState();
	}
	
	/**
	 * Checks if the actual game state has already finished and if this happens makes a state change.
	 * 
	 * */
	private void isThereChangeState(){
		if(!currentState.isRunning()){
			next++;
			if(next < order.size()){
				try {
					currentState = gameStates.get(order.get(next));
					currentState.run();
					stateChangedLastStep = true;
				} catch (Exception e) {
					 ErrorReporter.getInstance().handleSilentException(new Exception("Desbordamiento gamestates: \n" + e.getMessage() + "\n" + 
								e.getStackTrace() + "\n Game States: " + gameStates.toString() + "\n\n"));
					endGame = true;
				}
			}
			else
				endGame = true;
		}
	}
	
	/**
	 * Fill the canvas with black to reset the view.
	 * 
	 * */
	private void clearCanvas(Canvas canvas) {
		clearCanvas++;
		if(clearCanvas == 2){
			clearCanvas = 0;
			stateChangedLastStep = false;
		}
		canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
	}

	/**
	 * This class can be used with the default constructor and before calling this method or with the main constructor of the class.
	 * 
	 * */
	public void initialize(ArrayList<GameState> gameStates,ArrayList<Integer> order) {
		this.gameStates = gameStates;
		this.order = order;
		next = 0;
		if(next < order.size()){
			if(gameStates != null  && gameStates.size() > 0){
				currentState = gameStates.get(order.get(next));
			}
		}
		stateChangedLastStep = false;
	}
	
	/**
	 * It deletes every game state.
	 */
	public void clear(){
		this.gameStates.clear();
	}

	/**
	 * Saves game states before the application is paused
	 * 
	 * */
	public void onSaveInstance(Bundle outState) {
		outState.putIntegerArrayList("Game.order" , (ArrayList<Integer>) order);
		
		outState.putInt("Game.next", next);
		outState.putBoolean("Game.endGame", endGame);
		outState.putBoolean("Game.stateChangedLastStep", stateChangedLastStep);
		
		outState.putInt("Game.clearCanvas", clearCanvas);
		
		outState.putBoolean("Game.disabled", disabled);
		
		for (int i = 0; i < gameStates.size(); i++){
			gameStates.get(i).onSaveInstance(outState, i);
		}
	}
	
	/**
	 * Loads game states before the application is restored
	 * 
	 * */
	public void onRestoreInstance(Bundle savedInstanceState) {
		try{
			next = savedInstanceState.getInt("Game.next", 0);
			endGame= savedInstanceState.getBoolean("Game.endGame", false);
			stateChangedLastStep = savedInstanceState.getBoolean("Game.stateChangedLastStep", false);
			
			clearCanvas = savedInstanceState.getInt("Game.clearCanvas", 0);
			
			disabled = savedInstanceState.getBoolean("Game.disabled", false);
			
			if(next < order.size()){
				int n = order.get(next);
				currentState = gameStates.get(n);
			}
			
			for (int i = 0; i < gameStates.size(); i++){
				gameStates.get(i).onRestoreInstance(savedInstanceState, i);
			}
		}catch(Exception e){
			ErrorReporter.getInstance().handleSilentException(new Exception("Fallo restaurar estado: \n" + e.getMessage() + "\n" +
									" " + e.getStackTrace() +  " " + gameStates.toString()));
		}
	}

	/**
	 * Release all graphic resource before the is stopped
	 * 
	 * */
	public void delete() {
		for (GameState gs: gameStates){
			gs.delete();
		}
	}
}

	