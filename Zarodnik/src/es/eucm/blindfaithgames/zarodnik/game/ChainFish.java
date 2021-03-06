package es.eucm.blindfaithgames.zarodnik.game;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import es.eucm.blindfaithgames.engine.general.Entity;
import es.eucm.blindfaithgames.engine.general.GameState;
import es.eucm.blindfaithgames.engine.general.Mask;
import es.eucm.blindfaithgames.engine.graphics.SpriteMap;
import es.eucm.blindfaithgames.engine.others.RuntimeConfig;
import es.eucm.blindfaithgames.engine.sound.Music;
import es.eucm.blindfaithgames.zarodnik.R;

public class ChainFish extends Item {

	private Player player;
	
	private static int instancesNo;
	
	public ChainFish(int x, int y, Bitmap img, GameState gameState,
			List<Mask> mask, SpriteMap animations, String soundName,
			Point soundOffset, boolean collide) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		instancesNo++;
	}

	@Override
	public void onCollision(Entity e) {
		if(e instanceof Player){
			this.setTimer(0, RuntimeConfig.FRAMES_PER_STEP);
			this.setTimer(1, RuntimeConfig.FRAMES_PER_STEP*20);
			
			this.setCollidable(false);
			this.state = State.EATEN;
			
			player = (Player) e;
			
			player.setInvulnerable(true);
			
			Music.getInstanceMusic().play(this.gameState.getContext(), R.raw.barn_beat, true);
		}
	}

	@Override
	public void onTimer(int timer) {
		if(timer == 0){
			this.setVisible(false);
		}
		else if(timer == 1){
			this.remove();
			
			player.setInvulnerable(false);
			
			Music.getInstanceMusic().stop(R.raw.barn_beat);
		}
	}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {
		Music.getInstanceMusic().stop(R.raw.barn_beat);
	}

	
	@Override
	public boolean isFirstInstance() {
		return instancesNo == 2;
	}
}
