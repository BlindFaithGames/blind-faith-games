package org.example.golf;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;

public class Target extends Entity{

	public Target(int x, int y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, false, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}
}
