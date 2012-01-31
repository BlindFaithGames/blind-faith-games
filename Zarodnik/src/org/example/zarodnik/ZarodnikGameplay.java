package org.example.zarodnik;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.example.R;
import org.example.activities.MainActivity;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.BitmapScaler;
import org.example.tinyEngineClasses.CustomBitmap;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskCircle;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.Sound2D;
import org.example.tinyEngineClasses.SpriteMap;
import org.example.tinyEngineClasses.TTS;
import org.example.tinyEngineClasses.VolumeManager;
import org.pielot.openal.Source;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.View;

public class ZarodnikGameplay extends GameState {
	
	private static final int intro_sound = R.raw.pacman_intro;
	
	private static final int maxPredatorNumber = 3;
	private static final int prey_sound_die = R.raw.cat_angry;

	private static String prey_sound = "cat";
	private static String predator_sound = "snake";
	
	private int fontSize;
	private Typeface font;
	private Paint brush;
	
	private Player player;
	
	private boolean flag = false;
	
	public ZarodnikGameplay(View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);
		
		int record, sheetSize;
		
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		textToSpeech.setInitialSpeech("");

		record = loadRecord();
		
		sheetSize = 400;
		
		createEntities(record,sheetSize);
		
		// Set background image
		Bitmap field = BitmapFactory.decodeResource(v.getResources(), R.drawable.background);
		field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT, SCREEN_WIDTH);
		setBackground(field);
		
		fontSize = (int) (RuntimeConfig.FONT_SIZE * GameState.scale);
		
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setARGB(255, 51, 51, 51);
		if(font != null)
			brush.setTypeface(font);
		
		flag = true;
		
	}
	
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Loads from internal file system the previous record in Free Mode.
	 * 
	 * */
	private int loadRecord() {
		FileInputStream fis;
		int record = -1;
		try { 
			fis = this.getContext().openFileInput(MainActivity.FILENAMEFREEMODE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object f = ois.readObject();
			record = (Integer) f;
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (record == -1)
			return 0;
		else
			return record;
	}
	
	/**
	 * Instantiates the entities in the game.
	 * @param sheetSize 
	 * 
	 * */
	private void createEntities(int record, int sheetSize) {
		// Game entities: predators, preys and player
		
		// Player
		createPlayer(record, sheetSize);
		// Predators
		createPredator(sheetSize);
		// Prey
		createPrey(sheetSize);
	}
	
	
	private void createPlayer(int record, int sheetSize) {
		int  playerX, playerY;
		int frameW, frameH;
		Bitmap playerBitmap = null;
		ArrayList<Integer> aux;
		ArrayList<Mask> playerMasks;
		
		/*BitmapScaler scaler;
		
		try {
			scaler = new BitmapScaler(this.getContext().getResources(), R.drawable.playersheetx, sheetSize);
			playerBitmap = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}*/
		
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
		animations.addAnim("right", aux, RuntimeConfig.FRAMES_PER_STEP, true);
		
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
		
		frameW = playerBitmap.getWidth() / 3;
		frameH = playerBitmap.getHeight() / 8;
		
		playerMasks = new ArrayList<Mask>();
		playerMasks.add(new MaskCircle(frameW/2,frameH/2,frameW/3));

		playerX = SCREEN_WIDTH / 2;
		playerY = SCREEN_HEIGHT - SCREEN_HEIGHT / 3;
		
		player = new Player(playerX, playerY, record, playerBitmap, this, playerMasks, animations, null, null);
		
		this.addEntity(player);
	}
	
	private void createPredator(int sheetSize) {
		Entity e; 
		List<Sound2D> sources; 
		Source s;
		int frameW, frameH;
		int predatorN, predatorX, predatorY;
		int width, height;
		Random numberGenerator;
		Bitmap predatorBitmap = null;
		ArrayList<Integer> aux;
		ArrayList<Mask> predatorMasks;
		
		numberGenerator = new Random();
		predatorN = numberGenerator.nextInt(maxPredatorNumber) + 1;
		
		BitmapScaler scaler;
		try {
			scaler = new BitmapScaler(this.getContext().getResources(), R.drawable.predatorsheetx, sheetSize);
			predatorBitmap = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		
		frameW = predatorBitmap.getWidth() / 8;
		frameH = predatorBitmap.getHeight() / 1;
		width = SCREEN_WIDTH - frameW*2;
		height = SCREEN_HEIGHT - frameH*2;
		while (predatorN != 0){
			predatorX = numberGenerator.nextInt(width);
			predatorY = numberGenerator.nextInt(height);
		
			predatorMasks = new ArrayList<Mask>();
			predatorMasks.add(new MaskCircle(frameW/2,frameH/2,frameW/3));	
			
			SpriteMap animations = new SpriteMap(1, 8, predatorBitmap, 0);
			aux = new ArrayList<Integer>();
			aux.add(6);
			animations.addAnim("up", aux, RuntimeConfig.FRAMES_PER_STEP, false);
			aux = new ArrayList<Integer>();
			aux.add(4);
			aux.add(5);
			animations.addAnim("down", aux, RuntimeConfig.FRAMES_PER_STEP, false);
			aux = new ArrayList<Integer>();
			aux.add(2);
			aux.add(3);
			animations.addAnim("left", aux, RuntimeConfig.FRAMES_PER_STEP, false);
			aux = new ArrayList<Integer>();
			aux.add(0);
			aux.add(1);
			animations.addAnim("right", aux, RuntimeConfig.FRAMES_PER_STEP, false);
			aux = new ArrayList<Integer>();
			aux.add(7);
			animations.addAnim("die", aux, RuntimeConfig.FRAMES_PER_STEP, false);
			
			e = new Predator(predatorX, predatorY, null, this, predatorMasks, animations, 
					predator_sound, new Point(frameW/2,frameW/2), true);
			
			
			while (!this.positionFreeEntities(e)){
				predatorX = numberGenerator.nextInt(width);
				predatorY = numberGenerator.nextInt(height);
				e.setX(predatorX); e.setY(predatorY);
			}
			
			this.addEntity(e);
			
			sources = e.getSources();
			if(!sources.isEmpty()){
				s = sources.get(0).getS();
				s.setGain(5);
				s.setPitch(0.5f);
			}
			predatorN--;
		}
	}


	private void createPrey(int sheetSize) {
		int  preyX, preyY;
		int frameW, frameH;
		int width, height;
		Random numberGenerator;
		Bitmap preyBitmap = null;
		ArrayList<Integer> aux;
		ArrayList<Mask> preyMasks;
		Entity e;
		
		BitmapScaler scaler;
		try {
			scaler = new BitmapScaler(this.getContext().getResources(), R.drawable.preysheetx, sheetSize);
			preyBitmap = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		/*-------- Animations --------------------------------------*/
		SpriteMap animations = new SpriteMap(1, 9, preyBitmap, 0);
		aux = new ArrayList<Integer>();
		aux.add(6);
		aux.add(7);
		animations.addAnim("up", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(4);
		aux.add(5);
		animations.addAnim("down", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(2);
		aux.add(3);
		animations.addAnim("left", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(0);
		aux.add(1);
		animations.addAnim("right", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		aux = new ArrayList<Integer>();
		aux.add(8);
		animations.addAnim("die", aux, RuntimeConfig.FRAMES_PER_STEP, false);
		/*----------------------------------------------*/
		
		frameW = preyBitmap.getWidth() / 9;
		frameH = preyBitmap.getHeight() / 1;
		
		preyMasks = new ArrayList<Mask>();
		preyMasks.add(new MaskCircle(frameW/2,frameH/2,frameW/3));	
		
		numberGenerator = new Random();
		width = SCREEN_WIDTH - frameW*2;
		height = SCREEN_HEIGHT - frameH*2;
		preyX = numberGenerator.nextInt(width);
		preyY = numberGenerator.nextInt(height);
		
		e = new SmartPrey(preyX, preyY, null, this, preyMasks, animations,  
				prey_sound, new Point(frameW/2,frameW/2),true, prey_sound_die);
		
		while (!this.positionFreeEntities(e)){
			preyX = numberGenerator.nextInt(width);
			preyY = numberGenerator.nextInt(height);
			e.setX(preyX); e.setY(preyY);
		}
		
		this.addEntity(e);
	}
	
	@Override
	public void onInit() {
		super.onInit();
		Music.getInstanceMusic().playWithBlock(this.getContext(), intro_sound, false);
		this.getTextToSpeech().speak(this.getContext().getString(R.string.game_play_initial_TTStext));
		Input.getInput().clean();
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        if(flag){
    		brush.setARGB(255, 0, 0, 51);
    	    canvas.drawText(this.getContext().getString(R.string.initial_message), GameState.SCREEN_WIDTH/3, GameState.SCREEN_HEIGHT/2, brush);
        	flag = false;
        }
        
        if(player.isRemovable()){
        	brush.setARGB(255, 0, 0, 51);
        	canvas.drawText(this.getContext().getString(R.string.ending_lose_message), GameState.SCREEN_WIDTH/3, GameState.SCREEN_HEIGHT/2, brush);
        }
        
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("onVolUp");
		if (e != null){
			VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_RAISE);
		}else{
			e = Input.getInput().removeEvent("onVolDown");
			if (e != null)
				VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_LOWER);
		}
	}

}
