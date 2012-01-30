package org.example.zarodnik;

import java.util.List;
import java.util.Random;

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

public abstract class Creature extends Entity{
	
	private enum Sense { UP, DOWN, LEFT, RIGHT };
	
	protected ZarodnikGameplay game;
	
	protected double speed;
	
	private Sense direction; 
	
	private Random randomNumber;
	private int steps;
	
	public Creature(int x, int y, Bitmap img, GameState game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, int speed) {
		super(x, y, img, game, mask, animations, soundName, soundOffset);
		
		this.game = (ZarodnikGameplay) game;
		
		this.speed = speed;
		
		randomNumber = new Random();
		
		direction = selectSense();
		
		steps = randomNumber.nextInt(5) + 1;
	}

	private Sense selectSense() {
		Sense result = Sense.UP;
		switch(randomNumber.nextInt(4)){
			case(0):
				result = Sense.UP;
				break;
			case(1):
				result = Sense.DOWN;
				break;
			case(2):
				result = Sense.LEFT;
				break;
			case(3):
				result = Sense.RIGHT;
				break;
			default:
				break;
		}
		return result;
	}

	@Override
	protected void onUpdate() {
		if(checkAround()){
			this.playAllSources();
		}
		else{
			this.stopAllSources();
		}
		
		if(steps == 0){
			changeMovementSense();
		}
		else{
			steps = moveCreature(direction, speed, steps);
		}
		
		super.onUpdate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		switch(direction){
			case UP:
				this.playAnim("up", 15, true);
				break;
			case DOWN:
				this.playAnim("down", 15, true);
				break;
			case LEFT:
				this.playAnim("left", 15, true);
				break;
			case RIGHT:
				this.playAnim("right", 15, true);
				break;
			default:
				break;
	}
		
		if(RuntimeConfig.IS_DEBUG_MODE){
			Paint brush = new Paint();
			brush.setColor(Color.RED);
			brush.setStyle(Style.STROKE);
			int auxX = (int) (this.x - this.getImgWidth()*3.5);
			int auxY = (int) (this.y - this.getImgHeight()*3.5);
			canvas.drawRect(auxX, auxY, auxX + 8*this.getImgWidth(), auxY + 8*this.getImgHeight(), brush);
		}
	}

	private int moveCreature(Sense direction, double speed, int steps) {
		if(checkIfOutsideScreen(direction,speed)){
			steps = 0;
		}else{
			if(this.game.getPlayer().isInMovement()){
				switch(direction){
					case UP:
						this.y -= speed;
						break;
					case DOWN:
						this.y += speed;
						break;
					case LEFT:
						this.x -= speed;
						break;
					case RIGHT:
						this.x += speed;
						break;
					default:
						break;
				}
				steps--;
			}
		}
		return steps;
	}

	private boolean checkIfOutsideScreen(Sense direction, double speed) {
		boolean result = false;
		switch(direction){
			case UP:
				result  = this.y - speed < this.getImgHeight();
				break;
			case DOWN:
				result  = this.y - speed > GameState.SCREEN_HEIGHT - this.getImgHeight();
				break;
			case LEFT:
				result = this.x - speed < this.getImgWidth();
				break;
			case RIGHT:
				result = this.x - speed > GameState.SCREEN_WIDTH - this.getImgWidth();
				break;
			default:
				break;
		}
		return result;
	}

	@Override
	public void onCollision(Entity e) {
		if(e instanceof SmartPrey || e instanceof SillyPrey || e instanceof Predator){
			changeMovementSense();
		}
	}

	private void changeMovementSense() {
		steps = randomNumber.nextInt(50) + 30;
		direction = selectSense();
	}
	
	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}
	
	@Override
	public void onRemove() {}
	
	private boolean checkAround() {
		boolean result = (Math.abs(this.game.getPlayer().getX() - (2*this.x + this.getImgWidth())/2) < this.getImgWidth()*4)
				&& (Math.abs(this.game.getPlayer().getY() - (2*this.y + this.getImgHeight())/2) < this.getImgHeight()*4);
		return result;
	}
	
}
