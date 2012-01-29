package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Point;

public class SillyPrey extends Creature {

	//private int die_sound;
	
	public SillyPrey(int x, int y, Bitmap img, Game game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, 1);
		
		//die_sound = R.raw.whatever;
	}
}
