package org.example.zarodnik;

import java.util.List;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.CustomBitmap;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SoundManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Vibrator;

/**
 * It represents the player
 * 
 * */

public class Dot extends Entity{
	
//	private static final int previous_shot_feedback_sound = R.raw.previous_shoot_feedback_sound;

	private static final double EPSILON = 10;
	
	private static final int originX = ZarodnikGame.SCREEN_WIDTH/2;
	private static final int originY = ZarodnikGame.SCREEN_HEIGHT - ZarodnikGame.SCREEN_HEIGHT/3;
	private boolean launched;
	
	private double destX;
	private double destY;
	private double speed;
	private Point v;
	
	private ScoreBoard scoreBoard;
	
	private float dotCenterX;
	private float dotCenterY;

	/**
	 * It creates the entity scoreboard to refresh its content and uses the vibrator service.
	 * 
	 * */
	public Dot(int x, int y, int record, Bitmap img, Game game, List<Mask> mask,
			Context context, int frameCount, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, true, 1, soundName, soundOffset);

		this.stopAnim();
		
		this.game = (ZarodnikGame) game;
		
		dotCenterX = this.x + this.getImgWidth()/2;
		dotCenterY = this.y + this.getImgHeight()/2;
		
		speed = 0.1;
		v = new Point(0,0);

		Bitmap scoreBoardImg = BitmapFactory.decodeResource(this.game.getView().getResources(), R.drawable.scoreboard);
		scoreBoardImg = CustomBitmap.getResizedBitmap(scoreBoardImg, scoreBoardImg.getWidth()*2, scoreBoardImg.getHeight()*2);
		scoreBoard = new ScoreBoard(0,500,record,scoreBoardImg, game, null, false, 0, null, null);
		this.game.addEntity(scoreBoard);
	}
	
	/**
	 * Use a parametric equation to generate the points of the dot's trajectory
	 * */
	@Override
	public void onUpdate() {
		// TODO si evento scroll, actualizar pos		
		onScrollManagement();
		
		onDownManagement();
		
		SoundManager.getSoundManager(this.game.getContext()).setListenerPosition(x, y, 0f);
		
		super.onUpdate();
	}
	
	
	private void onDownManagement() {
		double auxX,auxY;
		EventType e  = Input.getInput().removeEvent("onDown");
		
		if(e != null){
			
			v.x = (int) (e.getMotionEventE1().getX() - this.x);
			v.y = (int) (e.getMotionEventE1().getY() - this.y);
			
			destX = e.getMotionEventE1().getX();
			
			destY = e.getMotionEventE1().getY();
			
		}
		
		if(v != null){
     		auxX = (this.x + v.x * speed);
     		auxY = (this.y + v.y * speed);
     		
			if(inStage(auxX,auxY) && !inDestination(auxX,auxY)){
				this.x = (int) auxX;
				this.y = (int) auxY;
			}
		} 
	}

	private boolean inDestination(double auxX, double auxY) {
		if(x < (destX + EPSILON) && x >= (destX - EPSILON) && y < (destY + EPSILON) && y >= (destY - EPSILON))
			return true;
		else
			return false;
	}

	private boolean inStage(double d, double e) {
		if(d < ZarodnikGame.SCREEN_WIDTH && d >= 0 && e < ZarodnikGame.SCREEN_HEIGHT && e >= 0)
			return true;
		else
			return false;
	}

	/**
	 * If onFling event occurs initializes the shot 
	 * In case of onScroll event manage the vibration or plays stereo sound and the animation. Also it plays a sound effect 
	 * if the event isn't on the shot area.
	 * 
	 * */
	private void onScrollManagement() {
		EventType e  = Input.getInput().removeEvent("onScroll");

	}

	/**
	 * It calls father's onDraw and 
	 * 
	 * @param canvas surface which will be drawn
	 * 
	 * */
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
	}

	@Override
	public void onCollision(Entity e) {
		// Predator and prey collides
		if (e instanceof Predator){
			// TODO Muerte, guardar puntuación
		}
		else if (e instanceof Prey){
			// TODO eliminar presa
			e.remove();
			
			scoreBoard.incrementCounter();
		}
	}
	
	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}
	
}
