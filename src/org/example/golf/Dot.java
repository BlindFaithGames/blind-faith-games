package org.example.golf;

import java.util.List;

import org.example.R;
import org.example.activities.GolfGameActivity;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.Music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;


public class Dot extends Entity{

	private static final int originX = 200;
	private static final int originY = 500;
	private boolean launched;
	
	private float incr;
	private float param;
	Point v = null;
	float initialX = 0;
	float initialY = 0;
	private Point targetPos; // target Position
	
	private ScoreBoard scoreBoard; 
	
	private Vibrator mVibrator;
	
	public Dot(int x, int y, Bitmap img, Game game, List<Mask> mask, Point targetPos) {
		super(x, y, img, game, mask, true, 7);
		launched = false;
		param = 0;
		this.stopAnim();
		
		this.targetPos = targetPos;
		
		this.mVibrator = (Vibrator) this.game.getContext().getSystemService(Context.VIBRATOR_SERVICE);
		
		Bitmap scoreBoardImg = BitmapFactory.decodeResource(this.game.getView().getResources(), R.drawable.scoreboard);
		scoreBoard = new ScoreBoard(0,500,scoreBoardImg, game, null, false, 0);
		this.game.addEntity(scoreBoard);
	}
	
	@Override
	public void onUpdate() {
		
		super.onUpdate();
		if (GolfGameActivity.UP_MODE){
			upModeManagement();
		}
		else{
			EventType e  = Input.getInput().getEvent("onFling");
			if (!launched &&  e != null){
				// Si hay desplazamiento en y (acción tirachinas)
				if (e.getDvy() > 0){
					// Entonces disparamos 
					v = e.getDistance();
					
					if (v.y < 0 && v.y < 200){
						launched = true;
						this.playAnim();
						param = 0.03f;
						incr = 0.1f;
						initialX = this.x;
						initialY = this.y;
					}
				}
			}
			
			e  = Input.getInput().getEvent("onScroll");
			if(!launched && e != null){
				this.playAnim();
				// vibration depends of gradient
				float gradientTarget = (- this.y)/(targetPos.x- this.x);
				float gradientMovement = (this.y - e.getE2().getRawY())/(this.x - e.getE2().getRawX());
				manageVibration(gradientMovement,gradientTarget);
			}
			else{ 
				this.stopAnim();
			}	
		}

		if(launched){
			this.playAnim();
			// parametric equation defined by initial event and final event associated to onFling event
			float auxX = initialX + param * v.x; 
			float auxY = initialY + param * v.y;

			param = param + incr;
			
			this.x = (int) auxX;
			this.y = (int) auxY;
			
			if(this.x <= -this.getImgWidth() || this.x > game.getView().getWidth() || this.y <= -this.getImgWidth()){
				// moves ball to origin position
				collides();
				scoreBoard.resetCounter();
			}
		}
	}

	private void upModeManagement() {
		EventType ed  = Input.getInput().getEvent("onDown");
		if (!launched &&  ed != null){
			MotionEvent e1 = ed.getE();
			EventType eu = Input.getInput().getEvent("onUp");
			
			if (eu != null){
				MotionEvent e2 = eu.getE();
				// Si hay desplazamiento en y (acción tirachinas)
				if (e2.getRawY() - e1.getRawY() > 0){
					// Entonces disparamos 
					v = new Point((int)(e1.getRawX()- e2.getRawX()),(int)(e1.getRawY() - e2.getRawY()));;
					
					if (v.y < 0 && v.y < 200){
						Log.d("GolfGameActivity", "Tiro");
						launched = true;
						this.playAnim();
						param = 0.03f;
						incr = 0.1f;
						initialX = this.x;
						initialY = this.y;
					}
				}
			}
		}
	}

	@Override
	public void onCollision(Entity e) {
		// Hole and ball collides
		if (e instanceof Target){
			
			Music.play(this.game.getContext(), R.raw.bing, false);
			// if we win it creates a new Target
			Target t = (Target) e;
			targetPos = t.changePosition();
			
			// increments scoreboard
			scoreBoard.incrementCounter();
			
			// moves ball to origin position
			collides();
		}
	}

	/**
	 * 	Moves ball to origin position
	 * 
	 * */
	private void collides(){
		this.setX(originX);
		this.setY(originY);
		launched = false;
		this.stopAnim();
	}
	
	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	private void manageVibration(float gradientMovement, float gradientTarget) {
		Log.w("",gradientMovement + " " + gradientTarget +  " " + Math.abs((gradientMovement - gradientTarget)));
		if(Math.abs(gradientMovement - gradientTarget) < 1){
			Music.play(this.game.getContext(), R.raw.bip, false);
			mVibrator.vibrate(100);
		}
	}
	
}