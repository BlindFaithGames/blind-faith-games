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
import org.example.tinyEngineClasses.SoundConfig;

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
//	private static final int alternative_previous_shot_feedback_sound  = R.raw.water_bubbles;
//	private static final int success_feedback_sound = R.raw.win_sound;
//	private static final int hit_feedback_sound = R.raw.hit_ball;
//	private static final int doppler_sound = R.raw.sound_shot;
//	private static final int alternative_doppler_sound = R.raw.storm;
	private static final int originX = ZarodnikGame.SCREEN_WIDTH/2;
	private static final int originY = ZarodnikGame.SCREEN_HEIGHT - ZarodnikGame.SCREEN_HEIGHT/3;
	private boolean launched;
	
	private float incr;
	private float param;
	private Point v = null;
	private float initialX = 0;
	private float initialY = 0;
	private Point targetPos; // target Position
	
	private ScoreBoard scoreBoard;
	
	private Vibrator mVibrator;
	private float leftVol,rightVol;
	
	private float dotCenterX;
	private float dotCenterY;
	
	
	// Debug
	private float scrollX;
	private float scrollY;
	
	private SoundConfig soundConfig;
	
	private EventType shotEvent; // Event that generate a shot

	/**
	 * It creates the entity scoreboard to refresh its content and uses the vibrator service.
	 * 
	 * */
	public Dot(int x, int y, int record, Bitmap img, Game game, List<Mask> mask, Context context) {
		super(x, y, img, game, mask, true, 5);
		soundConfig =  new SoundConfig(context, game);
		launched = false;
		param = 0;
		this.stopAnim();
		
		this.game = (ZarodnikGame) game;
		
		dotCenterX = this.x + this.getImgWidth()/2;
		dotCenterY = this.y + this.getImgHeight()/2;
		
		this.mVibrator = (Vibrator) this.game.getContext().getSystemService(Context.VIBRATOR_SERVICE);

		leftVol = 100;
		rightVol = leftVol;
		
		Bitmap scoreBoardImg = BitmapFactory.decodeResource(this.game.getView().getResources(), R.drawable.scoreboard);
		scoreBoardImg = CustomBitmap.getResizedBitmap(scoreBoardImg, scoreBoardImg.getWidth()*2, scoreBoardImg.getHeight()*2);
		scoreBoard = new ScoreBoard(0,500,record,scoreBoardImg, game, null, false, 0);
		this.game.addEntity(scoreBoard);
		
//		Music.getInstanceMusic().play(this.game.getContext(),previous_shot_feedback_sound,true);
//		Music.getInstanceMusic().setVolume(0, 0,previous_shot_feedback_sound);
//		if(!headPhonesMode){
//			Music.getInstanceMusic().play(this.game.getContext(),alternative_previous_shot_feedback_sound,true);
//			Music.getInstanceMusic().setVolume(0, 0,alternative_previous_shot_feedback_sound);
//		}
	}
	
	/**
	 * Use a parametric equation to generate the points of the dot's trajectory
	 * */
	@Override
	public void onUpdate() {
		// TODO si evento scroll, actualizar pos		
		onScrollManagement();
	
		if (launched){
			this.playAnim();

			float auxX = initialX + param * v.x; 
			float auxY = initialY + param * v.y;

			param = param + incr;
			
			this.x = (int) auxX;
			this.y = (int) auxY;
		}
		super.onUpdate();
	}
	
	
	/**
	 * If onFling event occurs initializes the shot 
	 * In case of onScroll event manage the vibration or plays stereo sound and the animation. Also it plays a sound effect 
	 * if the event isn't on the shot area.
	 * 
	 * */
	private void onScrollManagement() {
		EventType e  = Input.getInput().removeEvent("onScroll");
		if (!launched &&  e != null){
			if (e.getDvy() > 0){
				v = new Point((int)(dotCenterX - e.getMotionEventE2().getX()),
						      (int)(dotCenterY - e.getMotionEventE2().getY()));
				launched = true;
				this.playAnim();
				shotEvent = e;
				param = 0.5f;
				incr = 0.05f;
				initialX = this.x;
				initialY = this.y;
//				Music.getInstanceMusic().play(this.game.getContext(), hit_feedback_sound,false);
//				Music.getInstanceMusic().stop(this.game.getContext(), previous_shot_feedback_sound);
//				Music.getInstanceMusic().play(this.game.getContext(), doppler_sound, true);
				
			}
		}
	}


	/**
	 * It calls father's onDraw and draws trajectory lines if debug mode is enabled.
	 * 
	 * @param canvas surface which will be drawn
	 * 
	 * */
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		if (RuntimeConfig.IS_DEBUG_MODE){
			Paint brush = new Paint();
			brush.setColor(Color.BLACK);
			brush.setStrokeWidth(3);
			canvas.drawLine(dotCenterX, dotCenterY, targetPos.x, 0, brush);
			canvas.drawLine(scrollX, scrollY, dotCenterX, dotCenterY, brush);
		}
	}


	@Override
	public void onCollision(Entity e) {
		// Predator and prey collides
		if (e instanceof Predator){
			// TODO Muerte, guardar puntuación
		}
		else if (e instanceof Prey){
			// TODO eliminar presa
			scoreBoard.incrementCounter();
		}
	}

	/**
	 * 	Moves ball to origin position
	 * 
	 * */
	private void resetBall(){
		// TODO devolver a la posición inicial
		
//		Music.getInstanceMusic().play(this.game.getContext(),previous_shot_feedback_sound,true);
//		Music.getInstanceMusic().setVolume(0, 0, previous_shot_feedback_sound);
//		Music.getInstanceMusic().stop(this.game.getContext(), doppler_sound);
//		if(!headPhonesMode){
//			Music.getInstanceMusic().play(this.game.getContext(),alternative_previous_shot_feedback_sound,true);
//			Music.getInstanceMusic().setVolume(0, 0, alternative_previous_shot_feedback_sound);
//			Music.getInstanceMusic().stop(this.game.getContext(), alternative_doppler_sound);
//			Music.getInstanceMusic().stop(this.game.getContext(), alternative_doppler_sound);
//		}
//
//		this.setX(originX);
//		this.setY(originY);
//		launched = false;
//		this.stopAnim();
	}
	
	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}
	
}
