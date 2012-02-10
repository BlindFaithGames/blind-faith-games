package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;
import org.example.tinyEngineClasses.Input.EventType;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Boss extends Creature{

	public Boss(int x, int y, Bitmap img, GameState game, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset,
			boolean collidable, int speed) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, collidable,
				speed);
		
		//this->playAnim("whatever");
	
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("onDown");
		if(e != null){
			
		}
	}

	@Override
	public void onDie() {
		// TODO Auto-generated method stub
		
	}

}
