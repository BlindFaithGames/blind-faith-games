package com.golfgame.game;

import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.accgames.general.Entity;
import com.accgames.general.GameState;
import com.accgames.general.Mask;
import com.accgames.input.Input;
import com.accgames.input.Input.EventType;
import com.accgames.sound.Music;
import com.golfgame.R;
import com.golfgame.game.GolfGameplay.steps;

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

		if (e != null) {
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
