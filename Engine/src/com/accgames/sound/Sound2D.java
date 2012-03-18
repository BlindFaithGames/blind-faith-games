package com.accgames.sound;

import org.pielot.openal.Source;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Sound2D {

	private Point p;
	
	private Source s;

	public Sound2D(Point p, Source s) {
		super();
		this.p = p;
		this.s = s;
	}

	public Point getP() {
		return p;
	}

	public void setP(Point p) {
		this.p = p;
	}

	public Source getS() {
		return s;
	}

	public void onDraw(Canvas canvas, int x, int y) {
		Paint brush = new Paint();
		if(s.getTranscription() != null)
			canvas.drawText(s.getTranscription(), x + p.x, y, brush);
	}
}
