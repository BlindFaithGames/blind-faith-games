package org.example.zarodnik;

import java.util.List;
import java.util.Random;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Creature extends Entity{
	
	private int die_sound;
	
	private int direction; // or a point
	
	private double speed;
	
	private Random randomNumber;
	
	public Creature(int x, int y, Bitmap img, Game game, List<Mask> mask, int frameCount, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, false, frameCount, soundName, soundOffset);
		
		direction = 1;
		speed = 1;
		
		randomNumber = new Random();
		
		//die_sound = R.raw.whatever;
	}

	@Override
	protected void onUpdate() {
		
		super.onUpdate();
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
