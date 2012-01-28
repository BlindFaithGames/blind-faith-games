package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Predator extends Entity{

	public Predator(int x, int y, Bitmap img, Game game, List<Mask> mask,int frameCount, String soundName, Point soundOffset) {
		// TODO poner la máscara bien
		super(x, y, img, game, mask, false, frameCount, null, null);
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
