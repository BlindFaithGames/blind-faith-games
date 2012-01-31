package org.example.zarodnik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.BitmapScaler;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskCircle;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.SoundManager;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * It represents the player
 * 
 * */

public class Player extends Entity{
	
	private static final int die_sound = R.raw.pacman_dies;

	private static final float EPSILON = 8;
	
	private float destX;
	private float destY;
	private float speed;
	private float vx, vy;
	
	private float dotCenterX, initialX;
	private float dotCenterY, initialY;
	
	private ScoreBoard scoreBoard;
	
	private boolean inMovement;
	private float incr;
	private enum Sense { UP, DOWN, LEFT, RIGHT };
	private Sense direction;
	
	// The player size express in dps
	private static float SIZE_DP;
	

	/**
	 * It creates the entity scoreboard to refresh its content and uses the vibrator service.
	 * 
	 * */
	public Player(int x, int y, int record, Bitmap img, GameState game, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, true);

		this.gameState = (ZarodnikGameplay) game;
		
		initMovementParameters();
		inMovement = false;
		
		if(animations != null)
			animations.playAnim("up", RuntimeConfig.FRAMES_PER_STEP, true);
		
		if (GameState.SCREEN_WIDTH > 800)
			SIZE_DP = 400;
		else
			SIZE_DP = 800;
		
		scoreBoard = new ScoreBoard(ZarodnikGameplay.SCREEN_WIDTH - 200, 30, record, null, game, null, null, null, null);
		this.gameState.addEntity(scoreBoard);
	}
	
	public void initMovementParameters(){
		speed = 0.01f;
		incr = 0.03f;
		vx = 0;
		vy = 0;
		initialX = this.x;
		initialY = this.y;
		dotCenterX = this.x + this.getImgWidth()/2;
		dotCenterY = this.y + this.getImgHeight()/2;
	}
	
	public boolean isInMovement() {
		return inMovement;
	}

	public void setInMovement(boolean inMovement) {
		this.inMovement = inMovement;
	}
	
	/**
	 * Use a parametric equation to generate the points of the dot's trajectory
	 * */
	@Override
	public void onUpdate() {	
		onMoveManagement();

		SoundManager.getSoundManager(this.gameState.getContext()).setListenerPosition(x, y, 0f);
		
		super.onUpdate();
	}
	
	private void onMoveManagement() {
		double auxX,auxY;
		EventType e  = Input.getInput().removeEvent("onDrag"); 
		
		if (e != null){
			initMovementParameters();
			
			vx = e.getMotionEventE1().getX() - dotCenterX;
			vy = e.getMotionEventE1().getY() - dotCenterY;
			
			destX = e.getMotionEventE1().getX();
			
			destY = e.getMotionEventE1().getY();
			
			inMovement = true;
		}
		
		if (inMovement){
     		auxX = (initialX + vx * speed);
     		auxY = (initialY + vy * speed);
     		
			// We calculate the player direction
     		calculateDirection();
     		
     		speed += incr;
     		
			if (inStage(auxX,auxY) && !inDestination(dotCenterX,dotCenterY)){
				this.x = (int) auxX;
				this.y = (int) auxY;
				dotCenterX = this.x + this.getImgWidth()/2;
				dotCenterY = this.y + this.getImgHeight()/2;
				inMovement = true;
			}
			else{
				inMovement = false;
			}
		} 
	}
	
	private void calculateDirection() {
		if (Math.abs(initialX - destX) > Math.abs(initialY - destY)){
			if (destX < initialX){
				direction = Sense.LEFT;
				this.playAnim("left", RuntimeConfig.FRAMES_PER_STEP, true);
			}
			else{
				direction = Sense.RIGHT;
				this.playAnim("right", RuntimeConfig.FRAMES_PER_STEP, true);
			}
		}
		else{
			if (destY < initialY){
				direction = Sense.UP;
				this.playAnim("up", RuntimeConfig.FRAMES_PER_STEP, true);
			}
			else{
				direction = Sense.DOWN;
				this.playAnim("down", RuntimeConfig.FRAMES_PER_STEP, true);
			}
		}
	}

	private boolean inDestination(double auxX, double auxY) {
		if(auxX < (destX + EPSILON) && auxX >= (destX - EPSILON) && auxY < (destY + EPSILON) && auxY >= (destY - EPSILON))
			return true;
		else
			return false;
	}

	private boolean inStage(double d, double e) {
		if(d < ZarodnikGameplay.SCREEN_WIDTH && d >= 0 && e < ZarodnikGameplay.SCREEN_HEIGHT && e >= 0)
			return true;
		else
			return false;
	}

	/**
	 * It calls father's onDraw and 
	 * 
	 * @param canvas surface which will be drawn
	 * 
	 * */
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(RuntimeConfig.IS_DEBUG_MODE){
			canvas.drawRect(destX-EPSILON, destY-EPSILON, destX+EPSILON, destY+EPSILON, new Paint());
			canvas.drawLine(dotCenterX, dotCenterY,destX, destY, new Paint());
		}
	}

	@Override
	public void onCollision(Entity e) {
		// Predator and prey collides
		if (e instanceof Predator){
			inMovement = false;
			destX = x;
			destY = y;
			this.playAnim("die", RuntimeConfig.FRAMES_PER_STEP, false);
			this.remove();
		}
		else if (e instanceof SmartPrey || e instanceof SillyPrey){
			inMovement = false;
			destX = x;
			destY = y;
			this.resize();
			
			switch (direction) {
				case UP: this.playAnim("eatU", RuntimeConfig.FRAMES_PER_STEP, false);
					break;
				case DOWN: this.playAnim("eatD", RuntimeConfig.FRAMES_PER_STEP, false);
					break;
				case RIGHT: this.playAnim("eatR", RuntimeConfig.FRAMES_PER_STEP, false);
					break;
				case LEFT: this.playAnim("eatL", RuntimeConfig.FRAMES_PER_STEP, false);
					break;
			}
			
			e.remove();
			
			e.playAnim("die", 0, false);
			
			scoreBoard.incrementCounter();
		}
	}
	

	private void resize() {
		Bitmap img;
		List<Mask> maskList;
		int imgW, imgH, frameW, frameH;
		ArrayList<Integer> aux;
		
		img = this.getImg();
		imgW = img.getWidth();
		imgH = img.getHeight();
	
		BitmapScaler scaler;
		
		// We increments 150 pixels
		SIZE_DP += 150;
		
		// Convert the dps to pixels, based on density scale
		int size = (int) (SIZE_DP * GameState.scale);
		
		try {
			scaler = new BitmapScaler(this.gameState.getContext().getResources(), R.drawable.playersheetx, size);/*(int) (imgW*1.3));*/
			img = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		this.setImg(img);
		
		/*-------- Animations --------------------------------------*/
		SpriteMap animations = new SpriteMap(8, 3, img, 0);
		aux = new ArrayList<Integer>();
		aux.add(0);
		aux.add(1);
		aux.add(2);
		aux.add(1);
		aux.add(0);
		animations.addAnim("left", aux, RuntimeConfig.FRAMES_PER_STEP, true);
		
		aux = new ArrayList<Integer>();
		aux.add(0);
		aux.add(3);
		aux.add(4);
		aux.add(5);
		aux.add(4);
		aux.add(3);
		aux.add(0);
		animations.addAnim("eatL", aux, RuntimeConfig.FRAMES_PER_STEP, false);

		aux = new ArrayList<Integer>();
		aux.add(18);
		aux.add(19);
		aux.add(20);
		aux.add(19);
		aux.add(18);
		animations.addAnim("rigth", aux, RuntimeConfig.FRAMES_PER_STEP, true);
		
		aux = new ArrayList<Integer>();
		aux.add(18);
		aux.add(21);
		aux.add(22);
		aux.add(23);
		aux.add(22);
		aux.add(21);
		aux.add(18);
		animations.addAnim("eatR", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		
		aux = new ArrayList<Integer>();
		aux.add(6);
		aux.add(7);
		animations.addAnim("up", aux, RuntimeConfig.FRAMES_PER_STEP, true);
		
		aux = new ArrayList<Integer>();
		aux.add(6);
		aux.add(8);
		aux.add(7);
		aux.add(6);
		animations.addAnim("eatU", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		
		aux = new ArrayList<Integer>();
		aux.add(9);
		aux.add(10);
		aux.add(11);
		aux.add(10);
		aux.add(9);
		animations.addAnim("down", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		
		aux = new ArrayList<Integer>();
		aux.add(9);
		aux.add(12);
		aux.add(13);
		aux.add(14);
		aux.add(13);
		aux.add(12);
		aux.add(9);
		animations.addAnim("eatD", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		
		aux = new ArrayList<Integer>();
		aux.add(15);
		aux.add(16);
		aux.add(17);
		animations.addAnim("die", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		
		/*--------------------------------------------------*/
		
		this.setSpriteMap(animations);
		
		imgW = img.getWidth();
		imgH = img.getHeight();
		frameW = imgW / 9;
		frameH = imgH / 1;
		maskList = new ArrayList<Mask>();
		maskList.add(new MaskCircle(frameW/2,frameH/2,frameW/3));
		this.setMask(maskList);
	}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {
		Music.getInstanceMusic().playWithBlock(this.gameState.getContext(), die_sound, false);
		this.gameState.stop();
	}	
}