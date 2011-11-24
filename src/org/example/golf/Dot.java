package org.example.golf;

import java.util.List;

import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.Game;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.Point;
import org.example.tinyEngineClasses.Mask;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Dot extends Entity{

	public Dot(float x, float y, Bitmap img, Game game, List<Mask> mask) {
		super(x, y, img, game, mask, false, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	}

	@Override
	public void onUpdate() {
		// Si hay eventos tipo scroll, actualizar
		// onDown(0) - onShowPress(1) - onScroll(2)
		if (Input.getInput().getType(2).equals("onScroll")){
			Input.getInput().removeEvent(); Input.getInput().removeEvent();
			Point p = Input.getInput().getDistance();
			// Consumimos eventos de scroll
			Input.getInput().removeNextScroll();
			// Es posible que el último mov. en lugar de scroll sea fling --> no se tiene en cuenta y se borra.
			if (Input.getInput().getTypeNextEvent().equals("onFling")) Input.getInput().removeEvent();
			// Si hay desplazamiento en y negativo (acción tirachinas)
			if (p.getY() < 0){
				// Entonces disparamos en el ángulo que forma el desplazamiento en x
				// BANG!				
			}
		}
	}

	@Override
	public void onCollision(Entity e) {
		// La pelota se mete en el hoyo
		if (e instanceof Target){
			this.setX(e.getX());
			this.setY(e.getY());
		}
	}

	@Override
	public void onTimer(int timer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		
	}

}
