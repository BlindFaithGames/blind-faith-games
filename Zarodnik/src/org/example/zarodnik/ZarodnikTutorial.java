package org.example.zarodnik;

import java.util.ArrayList;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskCircle;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.SpriteMap;
import org.example.tinyEngineClasses.TTS;
import org.example.tinyEngineClasses.VolumeManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.view.View;

public class ZarodnikTutorial extends  GameState{

	private SpriteMap predatorAnimation;
	
	private SpriteMap preyAnimation;
	
	private int nTouches;
	
	public ZarodnikTutorial(View v, TTS textToSpeech, Context context) {
		super(v, context, textToSpeech);
	
		nTouches = 0;
		
		initializeStage();
	}

	@Override
	public void onInit() {
		super.onInit();
		this.getTextToSpeech().setQueueMode(TextToSpeech.QUEUE_FLUSH);
		this.getTextToSpeech().speak(this.getContext().getString(R.string.tutorial01_intro));
	}
	
	private void initializeStage() {
		createPredator();
		createPrey();
		createPlayer();
		createText();
	}

	private void createPlayer() {
		int  playerX, playerY;
		int frameW, frameH;
		Bitmap playerBitmap = null;
		ArrayList<Integer> aux;
		ArrayList<Mask> playerMasks;
		SmartPrey player;
		
		playerBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.playersheetm);
		
		/*-------- Animations --------------------------------------*/
		SpriteMap animations = new SpriteMap(8, 3, playerBitmap, 0);
		aux = new ArrayList<Integer>();
		aux.add(0);
		aux.add(1);
		aux.add(2);
		aux.add(1);
		aux.add(0);
		animations.addAnim("left", aux, RuntimeConfig.FRAMES_PER_STEP, true);

		aux = new ArrayList<Integer>();
		aux.add(18);
		aux.add(19);
		aux.add(20);
		aux.add(19);
		aux.add(18);
		animations.addAnim("right", aux, RuntimeConfig.FRAMES_PER_STEP, true);
		
		aux = new ArrayList<Integer>();
		aux.add(6);
		aux.add(7);
		animations.addAnim("up", aux, RuntimeConfig.FRAMES_PER_STEP, true);
		
		aux = new ArrayList<Integer>();
		aux.add(9);
		aux.add(10);
		aux.add(11);
		aux.add(10);
		aux.add(9);
		animations.addAnim("down", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		
		/*--------------------------------------------------*/
		
		frameW = playerBitmap.getWidth() / 3;
		frameH = playerBitmap.getHeight() / 8;
		
		playerMasks = new ArrayList<Mask>();
		playerMasks.add(new MaskCircle(frameW/2,frameH/2,frameW/3));

		playerX = SCREEN_WIDTH / 2;
		playerY = SCREEN_HEIGHT - SCREEN_HEIGHT / 3;
		
		player = new SmartPrey(playerX, playerY, playerBitmap, this, playerMasks, animations, null, null, false, 0);
		
		this.addEntity(player);
	}

	private void createText() {
		float fontSize;
		Typeface font;
		Paint brush;
		Text preyText, predatorText;
		int stepsPerWord;
		String preySpeech, predatorSpeech;
		fontSize =  (this.getContext().getResources().getDimension(R.dimen.font_size_intro)/GameState.scale);
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setARGB(255, 255, 255, 204);
		if(font != null)
			brush.setTypeface(font);
		stepsPerWord = RuntimeConfig.TEXT_SPEED;
		
		preySpeech = this.getContext().getString(R.string.tutorial01_prey);
		preyText = new Text(SCREEN_WIDTH/3, 0, null, this,null, null, null,
				null, false, brush, stepsPerWord, preySpeech);
		this.addEntity(preyText);
		
		predatorSpeech = this.getContext().getString(R.string.tutorial01_predator);
		predatorText = new Text(SCREEN_WIDTH/3, SCREEN_HEIGHT/2, null, this,null, null, null,
				null, false, brush, stepsPerWord, predatorSpeech);
		this.addEntity(predatorText);
	}

	private void createPredator() {
		Bitmap predatorBitmap = null;
		ArrayList<Integer> aux;
	
		predatorBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.predatorsheetx);
		
		predatorAnimation = new SpriteMap(1, 8, predatorBitmap, 0);
		aux = new ArrayList<Integer>();
		aux.add(6);
		predatorAnimation.addAnim("up", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(4);
		aux.add(5);
		predatorAnimation.addAnim("down", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(2);
		aux.add(3);
		predatorAnimation.addAnim("left", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(0);
		aux.add(1);
		predatorAnimation.addAnim("right", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(7);
		predatorAnimation.addAnim("die", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		
		predatorAnimation.playAnim("right", RuntimeConfig.FRAMES_PER_STEP, true);
	}

	
	private void createPrey() {
		Bitmap preyBitmap = null;
		ArrayList<Integer> aux;

		preyBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.preysheetx);
		
		/*-------- Animations --------------------------------------*/
		preyAnimation = new SpriteMap(1, 9, preyBitmap, 0);
		aux = new ArrayList<Integer>();
		aux.add(6);
		aux.add(7);
		preyAnimation.addAnim("up", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(4);
		aux.add(5);
		preyAnimation.addAnim("down", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(2);
		aux.add(3);
		preyAnimation.addAnim("left", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(0);
		aux.add(1);
		preyAnimation.addAnim("right", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(8);
		preyAnimation.addAnim("die", aux, RuntimeConfig.FRAMES_PER_STEP, false);

		preyAnimation.playAnim("right", RuntimeConfig.FRAMES_PER_STEP, true);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		int  preyX, preyY;		
		int predatorX, predatorY;
		
		canvas.drawColor(Color.BLACK);
		
		Paint p = new Paint();
		p.setStrokeWidth(10*GameState.scale);
		p.setColor(Color.WHITE);
		canvas.drawLine(0, SCREEN_HEIGHT/2, SCREEN_WIDTH, SCREEN_HEIGHT/2, p);
		
		preyX = 0;
		preyY = 0;
		preyAnimation.onDraw(preyX, preyY, canvas);
		predatorX = 0;
		predatorY = SCREEN_HEIGHT/2;
		predatorAnimation.onDraw(predatorX, predatorY, canvas);

		super.onDraw(canvas);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("onVolUp");
		if (e != null){
			VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_RAISE);
		}else{
			e = Input.getInput().removeEvent("onVolDown");
			if (e != null)
				VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_LOWER);
		}
		
		e = Input.getInput().removeEvent("onDoubleTap");
		if(e != null){
			nTouches++;
			e.getMotionEventE1();
			if(e.getMotionEventE1().getY() < SCREEN_HEIGHT/2){
				explainPrey();
			}else
				explainPredator();
		}
		
		if(nTouches > 10){
			nTouches = 0;
			this.getTextToSpeech().speak(this.getContext().getString(R.string.tutorial01_warning));
		}
		
		e = Input.getInput().removeEvent("onLongPress");
		if(e != null){
			this.stop();
		}

		preyAnimation.onUpdate();
	
		predatorAnimation.onUpdate();
	}

	private void explainPredator() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.predator, false);
		this.getTextToSpeech().speak(this.getContext().getString(R.string.tutorial01_predator));
	}

	private void explainPrey() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.prey, false);
		this.getTextToSpeech().speak(this.getContext().getString(R.string.tutorial01_prey));
	}
	
}
