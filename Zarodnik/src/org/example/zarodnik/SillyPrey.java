package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Point;

public class SillyPrey extends Creature {

	//private int die_sound;
	
	public SillyPrey(int x, int y, Bitmap img, GameState game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, boolean collidable) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, collidable, 1);
		
	}
	
	@Override
	public void onCollision(Entity e) {
		super.onCollision(e);
		if (e instanceof Player){
			this.playAnim("die", 1, false);
		}
	}
}
