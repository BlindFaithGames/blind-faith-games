package org.example.zarodnik;

import java.util.List;
import java.util.Random;

import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;
import org.example.zarodnik.ZarodnikGameplay.Sense;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

public abstract class Creature extends Entity{
	
	protected ZarodnikGameplay game;
	
	protected double speed;
	
	private Sense direction; 
	
	private Random randomNumber;
	private int steps;
	
	protected boolean die;
	

	public Creature(int x, int y, Bitmap img, GameState game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, boolean collidable, int speed) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, collidable);

		if(game instanceof ZarodnikGameplay)
			this.game = (ZarodnikGameplay) game;

		this.speed = speed;
		
		randomNumber = new Random();
		
		direction = selectSense();
		
		steps = randomNumber.nextInt(5) + 1;
		
		this.playAnim("right", RuntimeConfig.FRAMES_PER_STEP, true);
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
		if(!die){
			if(steps == 0){
				changeMovementSense();
			}
			else{
				steps = moveCreature(direction, speed, steps);
			}
		}
		super.onUpdate();
	}
	
	public abstract void onDie();

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

	private int moveCreature(Sense direction, double speed, int steps) {
		if(checkIfOutsideScreen(direction,speed)){
			steps = 0;
		}else{
			if(this.game != null){
				if(this.game.getPlayer().isInMovement()){
					switch(direction){
						case UP:
							this.y -= speed;
							this.playAnim("up", RuntimeConfig.FRAMES_PER_STEP, true);
							break;
						case DOWN:
							this.y += speed;
							this.playAnim("down", RuntimeConfig.FRAMES_PER_STEP, true);
							break;
						case LEFT:
							this.x -= speed;
							this.playAnim("left", RuntimeConfig.FRAMES_PER_STEP, true);
							break;
						case RIGHT:
							this.x += speed;
							this.playAnim("right", RuntimeConfig.FRAMES_PER_STEP, true);
							break;
						default:
							break;
					}	
					steps--;
				}
			}else{
				switch(direction){
				case UP:
					this.y -= speed;
					this.playAnim("up", RuntimeConfig.FRAMES_PER_STEP, true);
					break;
				case DOWN:
					this.y += speed;
					this.playAnim("down", RuntimeConfig.FRAMES_PER_STEP, true);
					break;
				case LEFT:
					this.x -= speed;
					this.playAnim("left", RuntimeConfig.FRAMES_PER_STEP, true);
					break;
				case RIGHT:
					this.x += speed;
					this.playAnim("right", RuntimeConfig.FRAMES_PER_STEP, true);
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
		if(this.game != null){
			boolean result = (Math.abs(this.game.getPlayer().getX() - (2*this.x + this.getImgWidth())/2) < this.getImgWidth()*4)
					&& (Math.abs(this.game.getPlayer().getY() - (2*this.y + this.getImgHeight())/2) < this.getImgHeight()*4);
			return result;
		}
			return false;
	}
	

	public void setDie(boolean die) {
		this.die = die;
	}
	
}
