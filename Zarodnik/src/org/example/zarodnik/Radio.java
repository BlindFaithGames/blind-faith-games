package org.example.zarodnik;

import java.util.List;
import java.util.Random;

import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Radio extends Item{

	private static final int MAX_CLUES = 10;
	private Entity prey;
	private Entity player;
	
	private int clueNumber;

	private static int instancesNo;
	
	public Radio(int x, int y, Bitmap img, GameState gameState,
			List<Mask> mask, SpriteMap animations, String soundName,
			Point soundOffset, boolean collide, Entity prey) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		this.prey = prey;
		Random numberGenerator = new Random();
		clueNumber = numberGenerator.nextInt(MAX_CLUES);
		
		instancesNo++;
	}

	@Override
	public void onCollision(Entity e) {
		if(e instanceof Player){
			this.setTimer(0, RuntimeConfig.FRAMES_PER_STEP);
			this.setTimer(1, RuntimeConfig.FRAMES_PER_STEP*5);
			this.setCollidable(false);
			player = e;
		}
	}

	@Override
	public void onTimer(int timer) {
		if(timer == 0){
			this.setVisible(false);
		}else{
			if(timer == 1){
				if(clueNumber > 0){
					this.setTimer(1, RuntimeConfig.FRAMES_PER_STEP*5);
					speakClue();
					clueNumber--;
				}
			}
		}
		
	}

	private void speakClue() {
		if(prey != null && !prey.isRemovable()){
			if (Math.abs(player.getX() - prey.getX()) > Math.abs(player.getY() - prey.getY())){
				if (prey.getX() < player.getX())
					this.gameState.getTextToSpeech().speak("left");
				else
					this.gameState.getTextToSpeech().speak("right");
			}
			else{
				if (prey.getY() < player.getY())
					this.gameState.getTextToSpeech().speak("up");
				else
					this.gameState.getTextToSpeech().speak("down");
			}
		}
	}

	@Override
	public void onInit() {}


	@Override
	public void onRemove() {}

	@Override
	public boolean isFirstInstance() {
		return instancesNo == 2;
	}
}
