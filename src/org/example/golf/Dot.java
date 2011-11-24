package org.example.golf;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;

public class Dot extends Entity{

	public Dot(int x, int y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, true, 6);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		this.y = y - 10;
	}
	
	@Override
	public void onCollision(Entity e) {
		// La pelota se mete en el hoyo
		if (e instanceof Target){
			this.setX(150);
			this.setY(300);
		}
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
