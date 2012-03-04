package com.zarodnik.game;

import java.util.List;

import com.accgames.tinyEngineClasses.GameState;
import com.accgames.tinyEngineClasses.Input;
import com.accgames.tinyEngineClasses.Mask;
import com.accgames.tinyEngineClasses.SpriteMap;
import com.accgames.tinyEngineClasses.Input.EventType;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Boss extends Creature{

	
	private boolean playing;
	private Simon simon;
	
	public Boss(int x, int y, Bitmap img, GameState game, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset,
			boolean collidable, int speed) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, collidable,
				speed);
		
		playing = false;
		
		simon = new Simon(this.gameState.getContext());
		
		//this.playAnim("stay", RuntimeConfig.FRAMES_PER_STEP, true);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		int ans;
		float touchX, touchY;
		EventType e = Input.getInput().removeEvent("onDown");
		if(e != null && !playing){
				touchX = e.getMotionEventE1().getX();
				touchY = e.getMotionEventE1().getY();
				if(touchX > GameState.SCREEN_WIDTH/2){
					if(touchY > GameState.SCREEN_HEIGHT/2){
						ans = Simon.UP_RIGHT;
					}
					else{
						ans = Simon.DOWN_RIGHT;
					}
				}
				else{
					if(touchY > GameState.SCREEN_HEIGHT/2){
						ans = Simon.UP_LEFT;
					}
					else{
						ans = Simon.DOWN_LEFT;
					}
				}
				playing = simon.checkAnswer(ans);
		}
		
		if(playing){
			simon.playSequence();
			playing = false;
		}
		
		if(simon.isGameOver()){
			finishGame();
		}
	}

	private void finishGame() {
		this.game.getPlayer().onDie();
	}

	@Override
	public void onDie() {}
}
