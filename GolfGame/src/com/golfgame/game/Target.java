package com.golfgame.game;

import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.accgames.tinyengine.Entity;
import com.accgames.tinyengine.Game;
import com.accgames.tinyengine.Input;
import com.accgames.tinyengine.Input.EventType;
import com.accgames.tinyengine.Mask;
import com.accgames.tinyengine.Music;
import com.golfgame.R;
import com.golfgame.game.GolfGame.steps;

public class Target extends Entity {

	private static final int clue_feedback_sound = R.raw.clue_feed_back_sound;

	public Target(int x, int y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, false, 0);
	}

	public Point changePosition() {
		Random positions = new Random();
		int ancho = this.game.getView().getWidth() - this.getImgWidth();
		this.x = positions.nextInt(ancho);
		return new Point(this.x + 2 * this.getImgWidth() / 5, this.y + 4
				* this.getImgWidth() / 5);
	}

	@Override
	public void onUpdate() {
		EventType e = Input.getInput().removeEvent("onDownTarget");

		if (e != null) {
			float x = e.getMotionEventE1().getX();
			float y = e.getMotionEventE1().getY();
			if ((x >= this.x && x < this.x + this.getImgWidth())
					&& (y >= this.y && y < this.y + this.getImgHeight())) {
				Music.getInstanceMusic().play(this.game.getContext(),
						clue_feedback_sound, false);
				if (((GolfGame) this.game).isTutorialMode()) {
					if (((GolfGame) this.game).getStep() == steps.STEP4) {
						((GolfGame) this.game).nextState();
					}
				}
			}
		}

		super.onUpdate();
	}

	@Override
	public void onCollision(Entity e) {
	}

	@Override
	public void onTimer(int timer) {
	}

	@Override
	public void onInit() {
	}
}
