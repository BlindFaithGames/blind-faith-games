package org.example.golf;

import java.util.List;
import java.util.Random;

import org.example.R;
import org.example.activities.SettingsActivity;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.Music;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;

public class Target extends Entity{

	public Target(int x, int y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, false, 0);
	}
	
	public Point changePosition(){
		Random positions = new Random();
		int ancho = this.game.getView().getWidth() - this.getImgWidth();
		this.x = positions.nextInt(ancho);
		return new Point(this.x,this.y);
	}
	
	@Override
	public void onUpdate(){
		if(SettingsActivity.getNotifyTarget(this.game.getContext())){
			EventType e = Input.getInput().removeEvent("onDown");
			
			if(e != null){
				float x = e.getE().getX();
				float y = e.getE().getY(); 
				if((x >= this.x && x < this.x + this.getImgWidth()) && (y >= this.y && y < this.y + this.getImgHeight())){
					Music.play(this.game.getContext(), R.raw.bip, false);
				} 
			}
		}
		super.onUpdate();
	}
	
	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}
}
