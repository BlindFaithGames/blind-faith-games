package es.eucm.blindfaithgames.zarodnik.game;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Point;
import es.eucm.blindfaithgames.engine.general.Entity;
import es.eucm.blindfaithgames.engine.general.GameState;
import es.eucm.blindfaithgames.engine.general.Mask;
import es.eucm.blindfaithgames.engine.graphics.SpriteMap;
import es.eucm.blindfaithgames.engine.others.RuntimeConfig;
import es.eucm.blindfaithgames.zarodnik.R;

public class Capsule extends Item {

	private boolean increment;

	private static int instancesNo;
	
	public Capsule(int x, int y, Bitmap img, GameState gameState,
			List<Mask> mask, SpriteMap animations, String soundName,
			Point soundOffset, boolean collide) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		increment = false;
		instancesNo++;
	}

	@Override
	public void onCollision(Entity e) {
		if(e instanceof Player){
			Player player = (Player) e;
			if(increment)
				player.resize(Player.PIXEL_PLAYER_RESIZE, true);
			else
				player.resize(-Player.PIXEL_PLAYER_RESIZE, true);
			
			this.state = State.EATEN;
			
			this.gameState.getTTS().speak(gameState.getContext().getString(R.string.size_dec));
			
			this.setTimer(0, RuntimeConfig.FRAMES_PER_STEP);
			this.setCollidable(false);
		}
	}

	@Override
	public void onTimer(int timer) {
		if(timer == 0){
			this.remove();
		}
	}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {}
	
	@Override
	public boolean isFirstInstance() {
		return instancesNo == 2;
	}
}
