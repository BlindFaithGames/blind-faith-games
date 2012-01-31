package org.example.zarodnik;

import java.io.IOException;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.BitmapScaler;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.TTS;
import org.example.tinyEngineClasses.VolumeManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.View;

public class ZarodnikGameOver extends GameState {
	
	private int fontSize;
	private Typeface font;
	private Paint brush;
	
	private String message;
	private int nextChar;
	private int steps;
	
	private Bitmap arrow;
	
	private static int textoffSetX;
	private static int textoffSetY;
	
	private int stepsPerLetter = RuntimeConfig.TEXT_SPEED;
	
	public ZarodnikGameOver(View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);
		
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		textToSpeech.speak(this.context.getString(R.string.game_over_text));
		
		message = this.getContext().getString(R.string.game_over_text);
		nextChar = 0;
		
		fontSize = (int) (RuntimeConfig.FONT_SIZE_INTRO * GameState.scale);	
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setARGB(255, 255, 255, 204);
		if(font != null)
			brush.setTypeface(font);
		
		BitmapScaler scaler;
		try {
			scaler = new BitmapScaler(this.getContext().getResources(), R.drawable.arrow, 30);
			arrow = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		textoffSetX = SCREEN_WIDTH  / 4;
		textoffSetY = SCREEN_HEIGHT / 2;
	}
	
	
	@Override
	public void onInit() {
		super.onInit();
		getTextToSpeech().speak(this.context.getString(R.string.game_over_tts));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		introTextEffect(canvas);
		if(nextChar-1 == message.length()){
			canvas.drawBitmap(arrow, SCREEN_WIDTH/2 - arrow.getWidth(), (SCREEN_HEIGHT - SCREEN_HEIGHT/5)  - arrow.getHeight(), null);
		}
	}
	
	private void introTextEffect(Canvas canvas) {
		int row,col;
		String aux;
		int lettersPerCol;
		row = 1; col = 1;
		lettersPerCol = SCREEN_WIDTH / fontSize;
		for(int i = 0; i < nextChar-1; i++){
			aux = message.substring(i, i+1);
			if(col == lettersPerCol-2 || aux.equalsIgnoreCase("\n")){
				row++;
				col = 1;
			}
			if(!aux.equalsIgnoreCase("\n")){
				canvas.drawText(aux, textoffSetX + col*fontSize, textoffSetY + row*fontSize, brush);
				col++;
			}
		}
		
		if (steps == stepsPerLetter){
			steps = 0;
			if(nextChar <= message.length())
				nextChar++;
		}
	}

	public void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("onVolUp");
		if (e != null){
			VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_RAISE);
		}else{
			e = Input.getInput().removeEvent("onVolDown");
			if (e != null)
				VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_LOWER);
		}

		e = Input.getInput().removeEvent("onDown");
		if(e != null && nextChar-1 == message.length()){
			this.stop();
		}
		else{
			if(e != null){
				stepsPerLetter = 1;
				steps = 0;
			}
		}
		steps++;
	}
}
