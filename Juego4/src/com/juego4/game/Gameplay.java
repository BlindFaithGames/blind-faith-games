package com.juego4.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.View;

import com.accgames.general.Game;
import com.accgames.general.GameState;
import com.accgames.input.Input;
import com.accgames.input.Input.EventType;
import com.accgames.others.RuntimeConfig;
import com.accgames.sound.Music;
import com.accgames.sound.TTS;
import com.accgames.sound.VolumeManager;
import com.juego4.R;

public class Gameplay extends GameState {
	

	private Text text;
	
	
	private static final int textoffSetX = 0;
	private static final int textoffSetY = 40;
	
	private int stepsPerWord = RuntimeConfig.TEXT_SPEED;

	
	public Gameplay(View v, TTS textToSpeech, Context c, Game game) {
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
	
	}
	
	@Override
	public void onInit() {
		super.onInit();
		getTTS().speak(this.context.getString(R.string.intro_game_tts));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

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
		

	}
}
