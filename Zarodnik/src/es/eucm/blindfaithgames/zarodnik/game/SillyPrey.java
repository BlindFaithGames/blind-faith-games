package es.eucm.blindfaithgames.zarodnik.game;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import es.eucm.blindfaithgames.engine.general.Entity;
import es.eucm.blindfaithgames.engine.general.GameState;
import es.eucm.blindfaithgames.engine.general.Mask;
import es.eucm.blindfaithgames.engine.graphics.SpriteMap;
import es.eucm.blindfaithgames.engine.sound.Music;

public class SillyPrey extends Creature {

	private int die_sound;
	
	public SillyPrey(int x, int y, Bitmap img, GameState game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, boolean collidable) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, collidable, 1);
	}
	
	@Override
	public void onCollision(Entity e) {
		super.onCollision(e);
		if (e instanceof Player){
			this.playAnim("die", 1, false);
		}
	}

	@Override
	public void onDie() {
		Music.getInstanceMusic().play(this.gameState.getContext(), die_sound, false);
	}
}
