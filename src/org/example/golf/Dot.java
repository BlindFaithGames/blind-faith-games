package org.example.golf;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Dot extends Entity{

	public Dot(float x, float y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, false, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		
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

}
