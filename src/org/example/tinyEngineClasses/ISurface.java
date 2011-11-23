package org.example.tinyEngineClasses;

import android.graphics.Canvas;

public interface ISurface {
	void onInitalize();
	void onDraw(Canvas canvas);
	void onUpdate(long gameTime);
}
