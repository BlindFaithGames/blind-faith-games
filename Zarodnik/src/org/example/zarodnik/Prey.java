package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.Music;

import android.R;
import android.graphics.Bitmap;
import android.graphics.Point;

public class Prey extends Entity{

	private int die_sound;
	
	
	public Prey(int x, int y, Bitmap img, Game game, List<Mask> mask, int frameCount, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, false, frameCount, soundName, soundOffset);
		
		//die_sound = R.raw.whatever;
	}

	@Override
	public void onCollision(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimer(int timer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onRemove() {
		//Music.getInstanceMusic().play(this.game.getContext(), die_sound, false);
		// change img and play anim
	}

}
