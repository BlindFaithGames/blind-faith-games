package com.zarodnik.game;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.view.View;

import com.accgames.general.Game;
import com.accgames.general.GameState;
import com.accgames.general.Mask;
import com.accgames.general.MaskCircle;
import com.accgames.graphics.SpriteMap;
import com.accgames.input.Input;
import com.accgames.input.Input.EventType;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Music;
import com.accgames.sound.TTS;
import com.accgames.sound.VolumeManager;
import com.zarodnik.R;

public class ZarodnikTutorial extends  GameState{
	
	public enum TutorialID {TUTORIAL0, TUTORIAL1, TUTORIAL2, TUTORIAL3, TUTORIAL4, TUTORIAL5, TUTORIAL6, TUTORIAL7, TUTORIAL8 };
	
	private TutorialID tutorialN;

	private SpriteMap predatorAnimation;
	
	private SpriteMap preyAnimation;
	
	private int nTouches;
	
	public ZarodnikTutorial(View v, TTS textToSpeech, Context context, TutorialID tutorialN, Game game) {
		super(v, context, textToSpeech, game);
	
		nTouches = 0;
		
		this.tutorialN = tutorialN;
		initializeStage(tutorialN);
	}

	public void setTutorialN(TutorialID tutorialN) {
		this.tutorialN = tutorialN;
		initializeStage(tutorialN);
	}
	
	@Override
	public void onInit() {
		super.onInit();
		this.getTTS().setQueueMode(TextToSpeech.QUEUE_FLUSH);
		switch(tutorialN){
		case TUTORIAL0:
			this.getTTS().speak(this.getContext().getString(R.string.tutorial00_intro) 
					+ " " + this.context.getString(R.string.long_tap));
			break;
		case TUTORIAL1:
			explainRadio();
			break;
		case TUTORIAL2:
			explainChainfish();
			break;
		case TUTORIAL3:
			explainCapsule();
			break;
		case TUTORIAL4:
			this.getTTS().speak(this.getContext().getString(R.string.tutorial04_instructions) 
					+ " " + this.context.getString(R.string.long_tap));
			break;
		case TUTORIAL5:
			this.getTTS().speak(this.getContext().getString(R.string.tutorial05_goal) 
					+ " " + this.context.getString(R.string.long_tap));
			break;
		case TUTORIAL6:
			this.getTTS().speak(this.getContext().getString(R.string.tutorial06_intro) 
					+ " " + this.context.getString(R.string.long_tap));
			break;
		case TUTORIAL7:
			this.getTTS().speak(this.getContext().getString(R.string.tutorial07_sound) 
					+ " " + this.context.getString(R.string.long_tap));
			break;
		case TUTORIAL8:
			this.getTTS().speak(this.getContext().getString(R.string.tutorial08_scoreboard) 
					+ " " + this.context.getString(R.string.long_tap));
			break;
		}
	}
	
	private void initializeStage(TutorialID tutorialN) {
		switch(tutorialN){
			case TUTORIAL0:
				createPredator();
				createPrey();
				createText(tutorialN);
				break;
			case TUTORIAL1:
				createItem(Item.RADIO);
				createText(tutorialN);
				break;
			case TUTORIAL2:
				createItem(Item.CHAINFISH);
				createText(tutorialN);
				break;
			case TUTORIAL3:
				createItem(Item.CAPSULE);
				createText(tutorialN);
				break;
			case TUTORIAL4:
				createText(tutorialN);
				break;
			case TUTORIAL5:
				createText(tutorialN);
				break;
			case TUTORIAL6:
				createText(tutorialN);
				break;
			case TUTORIAL7:
				createText(tutorialN);
				break;
			case TUTORIAL8:
				createText(tutorialN);
				break;
		}
		createPlayer();
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

	private void createText(TutorialID tutorialN) {
		float fontSize;
		Typeface font;
		Paint brush;
		Text preyText, predatorText, radioText, chainFishText, capsuleText, instructionsText;
		int stepsPerWord;
		String preySpeech, predatorSpeech, chainFishSpeech, radioSpeech, capsuleSpeech, instructions;
		fontSize =  (this.getContext().getResources().getDimension(R.dimen.font_size_tuto)/GameState.scale);
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setColor(this.getContext().getResources().getColor(R.color.blue1));
		if(font != null)
			brush.setTypeface(font);
		stepsPerWord = RuntimeConfig.TEXT_SPEED;
		
		switch(tutorialN){
			case TUTORIAL0:
				preySpeech = this.getContext().getString(R.string.tutorial00_prey);
				preyText = new Text(SCREEN_WIDTH/3, 0, null, this,null, null, null,
						null, false, brush, stepsPerWord, preySpeech, fontSize);
				this.addEntity(preyText);
				
				predatorSpeech = this.getContext().getString(R.string.tutorial00_predator);
				predatorText = new Text(SCREEN_WIDTH/3, SCREEN_HEIGHT/2, null, this,null, null, null,
						null, false, brush, stepsPerWord, predatorSpeech, fontSize);
				this.addEntity(predatorText);
				break;
			case TUTORIAL1:
				radioSpeech = this.getContext().getString(R.string.tutorial01_radio);
				radioText = new Text(0, SCREEN_HEIGHT/3, null, this,null, null, null,
						null, false, brush, stepsPerWord, radioSpeech, fontSize);
				this.addEntity(radioText);
				break;
			case TUTORIAL2:
				chainFishSpeech = this.getContext().getString(R.string.tutorial02_chainfish);
				chainFishText = new Text(0, SCREEN_HEIGHT/3, null, this,null, null, null,
						null, false, brush, stepsPerWord, chainFishSpeech, fontSize);
				this.addEntity(chainFishText);
				break;
			case TUTORIAL3:
				capsuleSpeech = this.getContext().getString(R.string.tutorial03_capsule);
				capsuleText = new Text(0, SCREEN_HEIGHT/3, null, this,null, null, null,
						null, false, brush, stepsPerWord, capsuleSpeech, fontSize);
				this.addEntity(capsuleText);
				break;
			case TUTORIAL4:
				instructions = this.getContext().getString(R.string.tutorial04_instructions);
				instructionsText = new Text(0, 0, null, this,null, null, null,
						null, false, brush, stepsPerWord, instructions, fontSize);
				this.addEntity(instructionsText);
				break;
			case TUTORIAL5:
				instructions = this.getContext().getString(R.string.tutorial05_goal);
				instructionsText = new Text(0, 0, null, this,null, null, null,
						null, false, brush, stepsPerWord, instructions, fontSize);
				this.addEntity(instructionsText);
				break;
			case TUTORIAL6:
				instructions = this.getContext().getString(R.string.tutorial06_intro);
				instructionsText = new Text(0, 0, null, this,null, null, null,
						null, false, brush, stepsPerWord, instructions, fontSize);
				this.addEntity(instructionsText);
				break;
			case TUTORIAL7:
				instructions = this.getContext().getString(R.string.tutorial07_sound);
				instructionsText = new Text(0, 0, null, this,null, null, null,
						null, false, brush, stepsPerWord, instructions, fontSize);
				this.addEntity(instructionsText);
				break;
			case TUTORIAL8:
				instructions = this.getContext().getString(R.string.tutorial08_scoreboard);
				instructionsText = new Text(0, 0, null, this,null, null, null,
						null, false, brush, stepsPerWord, instructions, fontSize);
				this.addEntity(instructionsText);
				break;
		}
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
	
	
	private void createItem(int type) {
		Item i = null; 
		int  x, y;
		int frameW;
		Bitmap itemBitmap = null;

		switch(type){
			case(Item.RADIO):
				itemBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.radio);
				break;
			case(Item.CHAINFISH):
				itemBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.chainfish);
				break;
			case(Item.CAPSULE):
				itemBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.capsule);
				break;
			default:
				break;
		}
		
		frameW = itemBitmap.getWidth();
		
		x = SCREEN_WIDTH/2;
		y = SCREEN_HEIGHT/5;
		
		switch(type){
			case(Item.RADIO):
				i = new Radio(x, y, itemBitmap, this, null, null, null, new Point(frameW/2,frameW/2), true, null);
				break;
			case(Item.CHAINFISH):
				i = new ChainFish(x, y, itemBitmap, this, null, null, null, new Point(frameW/2,frameW/2), true);
				break;
			case(Item.CAPSULE):
				i = new Capsule(x, y, itemBitmap, this, null, null, null, new Point(frameW/2,frameW/2), true);
				break;
			default:
				break;
		}

		this.addEntity(i);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		int  preyX, preyY;		
		int predatorX, predatorY;
		
		canvas.drawColor(Color.BLACK);
		switch(tutorialN){
			case TUTORIAL0:
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
				break;
		}

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
		
		if(nTouches > 10){
			nTouches = 0;
			this.getTTS().speak(this.getContext().getString(R.string.tutorial_warning));
		}
		
		e = Input.getInput().removeEvent("onLongPress");
		if(e != null){
			this.stop();
		}

		switch(tutorialN){
			case TUTORIAL0:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					nTouches++;
					e.getMotionEventE1();
					if(e.getMotionEventE1().getY() < SCREEN_HEIGHT/2){
						explainPrey();
					}else
						explainPredator();
				}
				preyAnimation.onUpdate();
				predatorAnimation.onUpdate();
			break;	
			case TUTORIAL1:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					nTouches++;
					explainRadio();
				}
				break;
			case TUTORIAL2:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					nTouches++;
					explainChainfish();
				}
				break;
			case TUTORIAL3:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					nTouches++;
					explainCapsule();
				}
				break;
			case TUTORIAL4:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					nTouches++;
					explainHero();
				}
				break;
		}

	}
	
	private void explainHero() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.bubble, false);
	}

	private void explainChainfish() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.chain, false);
		this.getTTS().speak(this.getContext().getString(R.string.tutorial02_chainfish) 
				+ " " + this.context.getString(R.string.long_tap));
	}
	
	private void explainCapsule() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.pill, false);
		this.getTTS().speak(this.getContext().getString(R.string.tutorial03_capsule) 
				+ " " + this.context.getString(R.string.long_tap));
	}
	
	private void explainRadio() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.radio, false);
		this.getTTS().speak(this.getContext().getString(R.string.tutorial01_radio) 
				+ " " + this.context.getString(R.string.long_tap));
	}

	private void explainPredator() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.predator, false);
		this.getTTS().speak(this.getContext().getString(R.string.tutorial00_predator));
	}

	private void explainPrey() {
		Music.getInstanceMusic().playWithBlock(this.getContext(), R.raw.prey, false);
		this.getTTS().speak(this.getContext().getString(R.string.tutorial00_prey));
	}
	
}
