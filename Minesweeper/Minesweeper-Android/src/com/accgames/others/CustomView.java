package com.accgames.others;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class CustomView extends View {

	public CustomView(Context context) {
		super(context);
		requestFocus();
		setFocusableInTouchMode(true);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		super.draw(canvas);
	}
}
