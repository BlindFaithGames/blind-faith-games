package org.example.zarodnik;

import java.util.ArrayList;
import java.util.List;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.CustomBitmap;
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

	/**
	 * It creates the entity scoreboard to refresh its content and uses the vibrator service.
	 * 
	 * */
	public Player(int x, int y, int record, Bitmap img, GameState game, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, animations, soundName, soundOffset);

		this.game = (ZarodnikGameplay) game;
		
		initMovementParameters();
		inMovement = false;
		
		if(animations != null)
			animations.playAnim("andar", 15, true);
		
		scoreBoard = new ScoreBoard(ZarodnikGameplay.SCREEN_WIDTH - 200, 30, record, null, game, null, null, null, null);
		this.game.addEntity(scoreBoard);
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
		onScrollManagement();
		
		onDownManagement();
		
		SoundManager.getSoundManager(this.game.getContext()).setListenerPosition(x, y, 0f);
		
		super.onUpdate();
	}
	
	
	private void onDownManagement() {
		double auxX,auxY;
		EventType e  = Input.getInput().removeEvent("onDown");
		
		if(e != null){
			initMovementParameters();
			
			vx = e.getMotionEventE1().getX() - dotCenterX;
			vy = e.getMotionEventE1().getY() - dotCenterY;
			
			destX = e.getMotionEventE1().getX();
			
			destY = e.getMotionEventE1().getY();
			
			inMovement = true;
		}
		
		if(inMovement){
     		auxX = (initialX + vx * speed);
     		auxY = (initialY + vy * speed);
     		
     		speed += incr;
     		
			if(inStage(auxX,auxY) && !inDestination(dotCenterX,dotCenterY)){
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
	 * If onFling event occurs initializes the shot 
	 * In case of onScroll event manage the vibration or plays stereo sound and the animation. Also it plays a sound effect 
	 * if the event isn't on the shot area.
	 * 
	 * */
	private void onScrollManagement() {
	//	EventType e  = Input.getInput().removeEvent("onScroll");
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
			this.remove();
		}
		else if (e instanceof SmartPrey || e instanceof SillyPrey){
			e.remove();
			
			this.resize();
			
			scoreBoard.incrementCounter();
		}
	}
	

	private void resize() {
		Bitmap img;
		List<Mask> maskList;
		
		img = this.getImg();
		img = CustomBitmap.getResizedBitmap(img, img.getHeight()*2, img.getWidth()*2);
		this.setImg(img);
		
		maskList = new ArrayList<Mask>();
		maskList.add(new MaskCircle(img.getWidth()/2,img.getWidth()/2,img.getWidth()/2));
		this.setMask(maskList);
	}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {
		Music.getInstanceMusic().playWithBlock(this.game.getContext(), die_sound, false);
		this.game.getContext().finish();
	}	
}
