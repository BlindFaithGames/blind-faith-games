package com.accgames.sound;

import org.pielot.openal.Source;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Associates a 3D sound source with a position 2D.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */
public class Sound2D {

	private Point p; // Position 
	 
	private Source s; // 3D sound source

	/**
	 * Unique constructor of the class.
	 * 
	 * @param p The position on the screen where the source will be played.
	 * @param s The source which will be played.
	 * 
	 * */
	public Sound2D(Point p, Source s) {
		super();
		this.p = p;
		this.s = s;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	public Point getP() {
		return p;
	}
	
	public Source getS() {
		return s;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	
	public void setP(Point p) {
		this.p = p;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	
	/**
	 * Draws a transcription of the sound on the given canvas.
	 * 
	 * @param canvas The canvas object that will be painted. 
	 * @param x the coordinate on the x axis of the entity linked to the source. 
	 * @param y the coordinate on the y axis of the entity linked to the source.
	 * */
	public void onDraw(Canvas canvas, int x, int y) {
		Paint brush = new Paint();
		if(s.getTranscription() != null)
			canvas.drawText(s.getTranscription(), x + p.x, y, brush);
	}
}
