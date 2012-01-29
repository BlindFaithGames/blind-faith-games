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
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskBox;
import org.example.tinyEngineClasses.MaskCircle;
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

public class ZarodnikGame extends Game {
	
	private static final int maxPredatorNumber = 3;
	private static final int prey_sound_die = R.raw.cat_angry;
	private static String prey_sound = "cat";
	private static String predator_sound = "snake";

	private int fontSize;
	private Typeface font;
	private Paint brush;
	
	private Player player;
	
	private boolean flag = false;
	
	public ZarodnikGame(View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);
		
		int record;
		
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		textToSpeech.setInitialSpeech(this.context.getString(R.string.game_initial_TTStext));
		record = loadRecord();
		
		createEntities(record);
		
		// Set background image
		Bitmap field = BitmapFactory.decodeResource(v.getResources(), R.drawable.background);
		field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT, SCREEN_WIDTH);
		setBackground(field);
		
		fontSize = RuntimeConfig.FONT_SIZE;
		
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		
		brush = new Paint();
		brush.setTextSize(fontSize*2);
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
	 * 
	 * */
	private void createEntities(int record) {
		// Game entities: predators, preys and player
		
		// Predators
		createPredator();
		// Prey
		createPrey();
		// Player
		createPlayer(record);
	}
	
	private void createPredator() {
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
			scaler = new BitmapScaler(this.getContext().getResources(), R.drawable.predatorsheetm, 500);
			predatorBitmap = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		
		frameW = predatorBitmap.getWidth() / 8;
		frameH = predatorBitmap.getHeight() / 1;
		width = SCREEN_WIDTH - frameW*2;
		height = SCREEN_HEIGHT - frameH*2;
		while(predatorN != 0){
			predatorX = numberGenerator.nextInt(width);
			predatorY = numberGenerator.nextInt(height);
		
			predatorMasks = new ArrayList<Mask>();
			predatorMasks.add(new MaskBox(0,0,frameW,frameH));	
			
			SpriteMap animations = new SpriteMap(1, 8, predatorBitmap, 0);
			aux = new ArrayList<Integer>();
			aux.add(6);
			animations.addAnim("up", aux, 15, false);
			aux = new ArrayList<Integer>();
			aux.add(4);
			aux.add(5);
			animations.addAnim("down", aux, 15, false);
			aux = new ArrayList<Integer>();
			aux.add(2);
			aux.add(3);
			animations.addAnim("left", aux, 15, false);
			aux = new ArrayList<Integer>();
			aux.add(0);
			aux.add(1);
			animations.addAnim("right", aux, 15, false);
			aux.add(7);
			animations.addAnim("die", aux, 15, false);
			
			e = new Predator(predatorX, predatorY, null, this, predatorMasks, animations, 
					predator_sound, new Point(frameW/2,frameW/2));
			
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

	private void createPrey() {
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
			scaler = new BitmapScaler(this.getContext().getResources(), R.drawable.preysheetm, 500);
			preyBitmap = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		SpriteMap animations = new SpriteMap(1, 9, preyBitmap, 0);
		aux = new ArrayList<Integer>();
		aux.add(6);
		aux.add(7);
		animations.addAnim("up", aux, 15, false);
		aux = new ArrayList<Integer>();
		aux.add(4);
		aux.add(5);
		animations.addAnim("down", aux, 15, false);
		aux = new ArrayList<Integer>();
		aux.add(2);
		aux.add(3);
		animations.addAnim("left", aux, 15, false);
		aux = new ArrayList<Integer>();
		aux.add(0);
		aux.add(1);
		animations.addAnim("right", aux, 15, false);
		aux.add(8);
		animations.addAnim("die", aux, 15, false);
		
		frameW = preyBitmap.getWidth() / 9;
		frameH = preyBitmap.getHeight() / 1;
		
		preyMasks = new ArrayList<Mask>();
		preyMasks.add(new MaskBox(0,0,frameW,frameH));	
		
		numberGenerator = new Random();
		width = SCREEN_WIDTH - frameW*2;
		height = SCREEN_HEIGHT - frameH*2;
		preyX = numberGenerator.nextInt(width) + 30;
		preyY = numberGenerator.nextInt(height) + 30;
		
		e = new SmartPrey(preyX, preyY, null, this, preyMasks, animations,  
				prey_sound, new Point(frameW/2,frameW/2),prey_sound_die);
		
		this.addEntity(e);
	}
	
	private void createPlayer(int record) {
		ArrayList<Integer> aux;
		Bitmap playerBitmap = null;
		ArrayList<Mask> playerMasks;
	
		BitmapScaler scaler;
		try {
			scaler = new BitmapScaler(this.getContext().getResources(), R.drawable.player, 50);
			playerBitmap = scaler.getScaled();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpriteMap animations = new SpriteMap(1, 1, playerBitmap, 0);
		aux = new ArrayList<Integer>();
		aux.add(0);
		animations.addAnim("andar", aux, 15, false);
		aux = new ArrayList<Integer>();
		
		int frameW = playerBitmap.getWidth();
		playerMasks = new ArrayList<Mask>();
		playerMasks.add(new MaskCircle(frameW/2,frameW/2,frameW/2)); 
		
		player = new Player(SCREEN_WIDTH / 2, SCREEN_HEIGHT - SCREEN_HEIGHT
				/ 3, record, playerBitmap, this, playerMasks, null, null, null);
		this.addEntity(player);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        if(flag ){
        	brush.setARGB(255, 0, 0, 51);
        	canvas.drawText(this.getContext().getString(R.string.initial_message), 1*Game.SCREEN_WIDTH/3, Game.SCREEN_HEIGHT/2, brush);
        	flag = false;
        }
        
        if(!this.isRunning()){
        	brush.setARGB(255, 0, 0, 51);
        	canvas.drawText(this.getContext().getString(R.string.ending_lose_message), 1*Game.SCREEN_WIDTH/3, Game.SCREEN_HEIGHT/2, brush);
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
