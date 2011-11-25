package org.example.golf;

import java.util.List;
import android.graphics.Point;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.view.MotionEvent;

public class Dot extends Entity{

	private boolean launched;
	private int speed;
	
	private float incr;
	private float param;
	Point v = null;
	float initialX = 0;
	float initialY = 0;
	
	
	public Dot(int x, int y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, true, 7);
		launched = false;
		speed = 10;
		param = 0;
		this.stopAnim();
	}
	
	@Override
	public void onUpdate() {
		
		super.onUpdate();
		
		EventType e  = Input.getInput().getEvent("onFling");
		if (!launched &&  e != null){
			MotionEvent e1 = e.getE();
			MotionEvent e2 = e.getE2();
			// Si hay desplazamiento en y negativo (acción tirachinas)
			if (e1.getRawY() - e2.getRawY() < 0){
				// Entonces disparamos en el ángulo que forma el desplazamiento en x
				// BANG!	
				v = new Point((int)(e1.getRawX() - e2.getRawX()),(int)(e1.getRawY() - e2.getRawY()));
				
				if(v.y < 0 & v.y < 200){
					launched = true;
					this.playAnim();
					param = 0.05f;
					incr = 0.5f;
					initialX = this.x;
					initialY = this.y;
				}
			}
		}
		
		e  = Input.getInput().getEvent("onScroll");
		if(!launched && e != null){
			this.playAnim();
		}
		else 
			this.stopAnim();
		
		if(launched){
			float auxX = initialX + param * v.x; 
			float auxY = initialY + param * v.y;

			param = param + incr;
			
			this.x = (int) auxX;
			this.y = (int) auxY;
			
			if(this.x <= 0 || this.x > 500)// width
				collides();
		}
	}

	@Override
	public void onCollision(Entity e) {
		// La pelota se mete en el hoyo
		if (e instanceof Target){
			collides();
		}
	}

	private void collides(){
		this.setX(200);
		this.setY(500);
		launched = false;
		this.stopAnim();
	}
	
	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

}