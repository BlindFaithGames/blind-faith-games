package org.example.golf;

import java.util.List;
import java.util.Random;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Target extends Entity{

	public Target(int x, int y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, false, 0);
		// TODO Auto-generated constructor stub
	}
	
	public Point changePosition(){
		Random positions = new Random();
		int ancho = this.game.getView().getWidth() - this.getImgWidth();
		this.x = positions.nextInt(ancho);
		return new Point(this.x,this.y);
	}
	
	
	
	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}
}
