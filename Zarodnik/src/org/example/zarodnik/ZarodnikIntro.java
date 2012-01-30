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
	
	private int fontSize;
	private Typeface font;
	private Paint brush;
	
	private String introMessage;
	private int nextChar;
	private int steps;
	
	private Bitmap arrow;
	
	private static final int textoffSetX = 0;
	private static final int textoffSetY = 40;
	
	private int stepsPerLetter = RuntimeConfig.TEXT_SPEED;
	
	public ZarodnikIntro(View v, TTS textToSpeech, Context c) {
		super(v,c,textToSpeech);
		
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		textToSpeech.setInitialSpeech(this.context.getString(R.string.intro_game_text));
	
		introMessage = this.getContext().getString(R.string.intro_game_text);
		nextChar = 0;
		
		fontSize = RuntimeConfig.FONT_SIZE;	
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setARGB(255, 255, 255, 204);
		if(font != null)
			brush.setTypeface(font);
		
		Music.getInstanceMusic().play(this.getContext(), R.raw.prelude, true);
	
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
		getTextToSpeech().speak(this.context.getString(R.string.intro_game_tts));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		introTextEffect(canvas);
		if(nextChar-1 == introMessage.length()){
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
			aux = introMessage.substring(i, i+1);
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
			if(nextChar <= introMessage.length())
				nextChar++;
		}
	
		/*StringTokenizer stk = new StringTokenizer(introMessage);
		row = 1;
		int i = 0;
		int sizeText;
		col = 1;
		sizeText = 0;
		while(i < nextChar-1 && stk.hasMoreTokens()){
			aux  = stk.nextToken();
			if(textoffSetX +  col*(sizeText+aux.length())*5 > SCREEN_WIDTH){
				row++;
				col = 1;
				sizeText = 0;
			}
			sizeText += aux.length() + 1;
			canvas.drawText(aux, textoffSetX + col*sizeText*5, textoffSetY + row*fontSize, brush);

			col++;
		}*/
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
		if(e != null && nextChar-1 == introMessage.length()){
			Music.getInstanceMusic().stop(this.getContext(), R.raw.prelude);
			this.stop();
		}
		else{
			if(e != null){
				stepsPerLetter = 1;
				steps = 0;
			}
		}
		e = Input.getInput().removeEvent("onScroll");
		if(e != null){
			Music.getInstanceMusic().stop(this.getContext(), R.raw.prelude);
			this.stop();
		}
		
		steps++;
	}
}
