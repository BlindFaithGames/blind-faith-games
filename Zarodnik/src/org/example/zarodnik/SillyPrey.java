package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Point;

public class SillyPrey extends Prey {

	private int die_sound;
	
	public SillyPrey(int x, int y, Bitmap img, Game game, List<Mask> mask, int frameCount, String soundName, Point soundOffset) {
		super(x, y, img, game, mask, frameCount, soundName, soundOffset, 1);
		
		//die_sound = R.raw.whatever;
	}
}
