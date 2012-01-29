package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Item extends Entity{

	public Item(int x, int y, Bitmap img, Game game, List<Mask> mask, boolean animated, int frameCount, String soundName,
			Point soundOffset) {
		super(x, y, img, game, mask, animated, frameCount, soundName, soundOffset);
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
		// TODO Auto-generated method stub
		
	}
	
}
