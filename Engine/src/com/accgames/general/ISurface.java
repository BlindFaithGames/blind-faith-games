package com.accgames.general;

import android.graphics.Canvas;

/**
 * Basic interface to emulate a classical game loop.
 * 
 * @author Javier �lvarez & Gloria Pozuelo.
 * 
 * */

public interface ISurface {
	void onInitalize();
	void onDraw(Canvas canvas);
	void onUpdate();
}
