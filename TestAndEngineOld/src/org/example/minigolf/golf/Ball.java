package org.example.minigolf.golf;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Ball extends AnimatedSprite {
	private static final String TAG = "Ball";
	private static final int PLAYER_CHARGE_ANIMATIONS = 5;

	public Ball(float pX, float pY, TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
		// TODO Auto-generated constructor stub
	}

}
