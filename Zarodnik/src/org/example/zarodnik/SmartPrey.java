package org.example.zarodnik;

import java.util.List;
import java.util.Random;

import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Point;

public class SmartPrey extends Creature {

	private int die_sound;
	
	private Random numberGenerator;
	
	public SmartPrey(int x, int y, Bitmap img, Game game, List<Mask> mask, int frameCount, String soundName, Point soundOffset) {

		super(x, y, img, game, mask, frameCount, soundName, soundOffset, 1);
	
		//die_sound = R.raw.whatever;
		numberGenerator = new Random();
	}
	
    @Override
    protected void onUpdate() {
    	double dx, dy;
    	int n = numberGenerator.nextInt(5);
    	if(!checkAround())
    		super.onUpdate();
    	else{
    		super.onUpdate();
			dx = this.game.getPlayer().getX() - this.x;
			dy = this.game.getPlayer().getY() - this.y;
			if(dx < 0 && dy < 0 && this.x + speed < Game.SCREEN_WIDTH - getImgWidth() 
								&& this.y + speed < Game.SCREEN_HEIGHT - getImgHeight()){
				this.x += speed;
				this.y += speed;
			}
			else{
				if(dx < 0 && dy > 0 && this.x + speed < Game.SCREEN_WIDTH - getImgWidth() 
									&& this.y - speed > getImgHeight()){
					this.x += speed;
					this.y -= speed;
				}
				else{
					if(dx > 0 && dy < 0 && this.x - speed > getImgWidth() 
										&& this.y + speed < Game.SCREEN_HEIGHT - getImgHeight()){
    					this.x -= speed;
    					this.y += speed;
					}
					else{
						if(this.x - speed > getImgWidth() && this.y - speed > getImgHeight()){
	    					this.x -= speed;
	    					this.y -= speed;
						}
					}
				}
			}
			
		}
    }

	private boolean checkAround() {
		boolean result = (Math.abs(this.game.getPlayer().getX() - this.x) < 50)
				&& (Math.abs(this.game.getPlayer().getY() - this.y) < 50);
		return result;
	}
}
