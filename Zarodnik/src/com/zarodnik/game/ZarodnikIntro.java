package com.zarodnik.game;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.View;

import com.accgames.general.Game;
import com.accgames.general.GameState;
import com.accgames.graphics.BitmapScaler;
import com.accgames.input.Input;
import com.accgames.input.Input.EventType;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Music;
import com.accgames.sound.TTS;
import com.accgames.sound.VolumeManager;
import com.zarodnik.R;

public class ZarodnikIntro extends GameState {
	

	private Text text;
	
	private Bitmap arrow;
	
	private static final int textoffSetX = 0;
	private static final int textoffSetY = 40;
	
	private int stepsPerWord = RuntimeConfig.TEXT_SPEED;

	
	public ZarodnikIntro(View v, TTS textToSpeech, Context c, Game game) {
		super(v,c,textToSpeech, game);

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
		Music.getInstanceMusic().play(this.getContext(), R.raw.frost_walz, true);
		
		getTTS().speak(this.context.getString(R.string.intro_game_tts));
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
			Music.getInstanceMusic().stop(R.raw.frost_walz);
			this.stop();
		}
		else{
			if(e != null){
				text.setStepsPerWord(1);
			}
		}
	}
}
