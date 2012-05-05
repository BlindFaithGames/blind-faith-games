package es.eucm.blindfaithgames.golfgame.game;

import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;


import es.eucm.blindfaithgames.engine.general.Entity;
import es.eucm.blindfaithgames.engine.general.GameState;
import es.eucm.blindfaithgames.engine.general.Mask;
import es.eucm.blindfaithgames.engine.input.Input;
import es.eucm.blindfaithgames.engine.input.Input.EventType;
import es.eucm.blindfaithgames.engine.sound.Music;
import es.eucm.blindfaithgames.golfgame.R;
import es.eucm.blindfaithgames.golfgame.activities.SettingsActivity;
import es.eucm.blindfaithgames.golfgame.activities.TutorialActivity;
import es.eucm.blindfaithgames.golfgame.game.GolfGameplay.steps;

public class Target extends Entity {

	private static final int clue_feedback_sound = R.raw.clue_feed_back_sound;

	public Target(int x, int y, Bitmap img, GameState game, List<Mask> mask) {
		super(x, y, img, game, mask, null, null, null, true);
	}

	public Point changePosition() {
		Random positions = new Random();
		int ancho = this.gameState.getView().getWidth() - this.getImgWidth();
		this.x = positions.nextInt(ancho);
		return new Point(this.x + 2 * this.getImgWidth() / 5, this.y + 4
				* this.getImgWidth() / 5);
	}

	@Override
	public void onUpdate() {
		EventType e = Input.getInput().getEvent("onDrag");

		if (e != null && (SettingsActivity.getNotifyTarget(gameState.getContext()) || this.gameState.getContext() instanceof TutorialActivity)) {
			float x = e.getMotionEventE1().getX();
			float y = e.getMotionEventE1().getY();
			if ((x >= this.x && x < this.x + this.getImgWidth())
					&& (y >= this.y && y < this.y + this.getImgHeight())) {
				
				if(!Music.getInstanceMusic().isPlaying(clue_feedback_sound))
					Music.getInstanceMusic().play(this.gameState.getContext(),clue_feedback_sound, false);
				
				if (((GolfGameplay) this.gameState).isTutorialMode()) {
					if (((GolfGameplay) this.gameState).getStep() == steps.STEP4) {
						((GolfGameplay) this.gameState).nextState();
					}
				}
			}
		}

		super.onUpdate();
	}

	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {}
}
