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

public class Capsule extends Item {

	private boolean increment;

	private static int instancesNo;
	
	public Capsule(int x, int y, Bitmap img, GameState gameState,
			List<Mask> mask, SpriteMap animations, String soundName,
			Point soundOffset, boolean collide) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		Random numberGenerator = new Random();
		increment = numberGenerator.nextInt(2) == 0;
		instancesNo++;
	}

	@Override
	public void onCollision(Entity e) {
		if(e instanceof Player){
			Player player = (Player) e;
			if(increment)
				player.resize(Player.PIXEL_PLAYER_RESIZE);
			else
				player.resize(-Player.PIXEL_PLAYER_RESIZE);
			
			this.state = State.EATEN;
			
			this.setTimer(0, RuntimeConfig.FRAMES_PER_STEP);
			this.setCollidable(false);
		}
	}

	@Override
	public void onTimer(int timer) {
		if(timer == 0){
			this.remove();
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
