package org.example.zarodnik;

import java.io.IOException;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.BitmapScaler;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Input;
import org.example.tinyEngineClasses.Input.EventType;
import org.example.tinyEngineClasses.Music;
import org.example.tinyEngineClasses.TTS;
import org.example.tinyEngineClasses.VolumeManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.View;

public class ZarodnikIntro extends GameState {
	

	private Text text;
	
	private Bitmap arrow;
	
	private static final int textoffSetX = 0;
	private static final int textoffSetY = 40;
	
	private int stepsPerWord = RuntimeConfig.TEXT_SPEED;

	
	public ZarodnikIntro(View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);

		float fontSize;
		Typeface font;
		Paint brush;
		fontSize =  (this.getContext().getResources().getDimension(R.dimen.font_size_intro)/GameState.scale);
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setARGB(255, 255, 255, 204);
		if(font != null)
			brush.setTypeface(font);
		
		text = new Text(textoffSetX, textoffSetY, null, this, null, null, 
				null, null, false, brush, stepsPerWord, this.getContext().getString(R.string.intro_game_text));
		this.addEntity(text);
		
		BitmapScaler scaler;
		try {
			scaler = new BitmapScaler(this.getContext().getResources(),R.drawable.arrow, 30);
			arrow = scaler.getScaled();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onInit() {
		super.onInit();
		Music.getInstanceMusic().play(this.getContext(), R.raw.prelude, true);
		
		getTextToSpeech().speak(this.context.getString(R.string.intro_game_tts));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(text.isFinished()){
			canvas.drawBitmap(arrow, SCREEN_WIDTH/2 - arrow.getWidth(), (SCREEN_HEIGHT - SCREEN_HEIGHT/5)  - arrow.getHeight(), null);
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
		
		e = Input.getInput().removeEvent("onLongPress");
		if(e != null && text.isFinished()){
			Music.getInstanceMusic().stop(this.getContext(), R.raw.prelude);
			this.stop();
		}
		else{
			if(e != null){
				text.setStepsPerWord(1);
			}
		}
	}
}
