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
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.MaskBox;
import org.example.tinyEngineClasses.MaskCircle;
import org.example.tinyEngineClasses.Sound2D;
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
	
	private Dot player;
	
	private boolean flag = false;
	
	public ZarodnikGame(View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);
		
		int record;
		
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		
		textToSpeech.setInitialSpeech(this.context.getString(R.string.game_initial_TTStext));
		record = loadRecord();
		
		createEntities(record);
		
		// Set background image
		//Bitmap field = BitmapFactory.decodeResource(v.getResources(), R.drawable.field);
		//field = CustomBitmap.getResizedBitmap(field, SCREEN_HEIGHT, SCREEN_WIDTH);
		//Paint brush = new Paint();
		//brush.setAlpha(175);
		//editBackground(brush);
		//setBackground(field);
		
		fontSize = RuntimeConfig.FONT_SIZE;
		
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		
		brush = new Paint();
		brush.setTextSize(fontSize*2);
		brush.setARGB(255, 51, 51, 51);
		if(font != null)
			brush.setTypeface(font);
		
		flag = true;
	}
	
	public Dot getPlayer(){
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
		Entity e; 
		List<Sound2D> sources; 
		Source s;
		int predatorN, predatorX, predatorY, preyX, preyY;
		int width, height;
		Random numberGenerator;
		Bitmap predatorBitmap, preyBitmap, playerBitmap;
		ArrayList<Mask> predatorMasks, preyMasks, playerMasks;
		
		numberGenerator = new Random();
		predatorN = numberGenerator.nextInt(maxPredatorNumber) + 1;
		predatorBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.predator);

		width = SCREEN_WIDTH - predatorBitmap.getWidth()*2;
		height = SCREEN_HEIGHT - predatorBitmap.getHeight()*2;
		while(predatorN != 0){
			predatorX = numberGenerator.nextInt(width);
			predatorY = numberGenerator.nextInt(height);
		
			predatorMasks = new ArrayList<Mask>();
			predatorMasks.add(new MaskBox(0,0,predatorBitmap.getWidth(),predatorBitmap.getHeight()));	
			
			e = new Predator(predatorX, predatorY, predatorBitmap, this, predatorMasks, 0, 
					predator_sound, new Point(predatorBitmap.getWidth()/2,predatorBitmap.getWidth()/2));
			
			this.addEntity(e);
			
			sources = e.getSources();
			if(!sources.isEmpty()){
				s = sources.get(0).getS();
				s.setGain(5);
				s.setPitch(0.5f);
			}
			predatorN--;
		}
		
		// Prey
		preyBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.prey);
		preyMasks = new ArrayList<Mask>();
		preyMasks.add(new MaskBox(0,0,preyBitmap.getWidth(),preyBitmap.getHeight()));	
		numberGenerator = new Random();
		preyX = numberGenerator.nextInt(width) + 30;
		preyY = numberGenerator.nextInt(height) + 30;
		e = new SmartPrey(preyX, preyY, preyBitmap, this, preyMasks, 0, 
				prey_sound, new Point(preyBitmap.getWidth()/2,preyBitmap.getWidth()/2),prey_sound_die);
		this.addEntity(e);
		
		// Player
		playerBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.player);
		playerMasks = new ArrayList<Mask>();
		playerMasks.add(new MaskCircle(playerBitmap.getWidth()/2,playerBitmap.getWidth()/2,playerBitmap.getWidth()/2));     
		player = new Dot(SCREEN_WIDTH / 2, SCREEN_HEIGHT - SCREEN_HEIGHT
				/ 3, record, playerBitmap, this, playerMasks, context, 0, null, null);
		this.addEntity(player);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
	    Bitmap grass = BitmapFactory.decodeResource(v.getResources(),R.drawable.field);
        int width = (v.getWidth() / grass.getWidth()) + 1;
        int height = (v.getHeight() / grass.getHeight()) + 1;
        for(int i = 0; i < width; i++){
          for(int j = 0; j < height; j++){
                       canvas.drawBitmap(grass, grass.getWidth()*i, grass.getHeight()*j, null);
          }
        }
        
        if(flag ){
        	brush.setARGB(255, 0, 0, 51);
        	canvas.drawText(this.getContext().getString(R.string.initial_message), 1*Game.SCREEN_WIDTH/3, Game.SCREEN_HEIGHT/2, brush);
        	flag = false;
        }
        
        if(!this.isRunning()){
        	brush.setARGB(255, 0, 0, 51);
        	canvas.drawText(this.getContext().getString(R.string.ending_lose_message), 1*Game.SCREEN_WIDTH/3, Game.SCREEN_HEIGHT/2, brush);
        }
        
		super.onDraw(canvas);
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
