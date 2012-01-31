package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Predator extends Creature{

	public Predator(int x, int y, Bitmap img, GameState game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, boolean collidable) {
		super(x, y, img, game, mask, animations,soundName, soundOffset, collidable, 1);
	}

}