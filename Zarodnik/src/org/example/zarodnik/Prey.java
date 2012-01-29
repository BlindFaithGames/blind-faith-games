package org.example.zarodnik;

import java.util.List;

import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Point;

public abstract class Prey extends Creature {
	public Prey(int x, int y, Bitmap img, Game game, List<Mask> mask, int frameCount, String soundName, Point soundOffset, int speed) {
		super(x, y, img, game, mask, frameCount, soundName, soundOffset, speed);
	}
}
