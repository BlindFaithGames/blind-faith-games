package org.example.zarodnik;

import java.util.List;

import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

public abstract class Item extends Entity {
	
	protected ZarodnikGameplay gameState;
	
	public static final int ITEM_NUMBER = 3;
	
	public static final int RADIO = 0;
	public static final int SEAWEED = 1;
	public static final int CAPSULE = 2;
	
	protected enum State{ EATEN, NORMAL };
	
	protected State state;

	public Item(int x, int y, Bitmap img, GameState gameState, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset,
			boolean collide) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		if(gameState instanceof ZarodnikGameplay)
			this.gameState = (ZarodnikGameplay) gameState;
		
		state = State.NORMAL;
	}

	public abstract boolean isFirstInstance();
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(RuntimeConfig.IS_DEBUG_MODE){
			Paint brush = new Paint();
			brush.setColor(Color.RED);
			brush.setStyle(Style.STROKE);
			int auxX = (int) (this.x - this.getImgWidth()*3.5);
			int auxY = (int) (this.y - this.getImgHeight()*3.5);
			canvas.drawRect(auxX, auxY, auxX + 8*this.getImgWidth(), auxY + 8*this.getImgHeight(), brush);
		}
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		if(checkAround() && state != State.EATEN){
			this.playAllSources();
		}
		else{
			this.stopAllSources();
		}
		
		switch(state){
			case EATEN:
				break;
			case NORMAL:
				break;
		}
	}
	
	private boolean checkAround() {
		if(this.gameState != null){
			boolean result = (Math.abs(this.gameState.getPlayer().getX() - (2*this.x + this.getImgWidth())/2) < this.getImgWidth()*4)
					&& (Math.abs(this.gameState.getPlayer().getY() - (2*this.y + this.getImgHeight())/2) < this.getImgHeight()*4);
			return result;
		}
			return false;
	}
}
