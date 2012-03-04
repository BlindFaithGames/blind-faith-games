package com.accgames.tinyEngineClasses;

import org.pielot.openal.Source;

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
}
