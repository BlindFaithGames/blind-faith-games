package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Point;

public class SmartPrey extends Creature {

	private int die_sound;
	
	public SmartPrey(int x, int y, Bitmap img, Game game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, int dieSound) {

		super(x, y, img, game, mask, animations, soundName, soundOffset, 1);
	
		die_sound = dieSound;
	}
	
    @Override
    protected void onUpdate() {
    	double dx, dy;
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
	
	@Override
	public void onRemove() {
		super.onRemove();
		
		Music.getInstanceMusic().play(this.game.getContext(), die_sound, false);
	}
}
