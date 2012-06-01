package es.eucm.blindfaithgames.tss.game;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.View;
import es.eucm.blindfaithgames.engine.general.Game;
import es.eucm.blindfaithgames.engine.general.GameState;
import es.eucm.blindfaithgames.engine.input.Input;
import es.eucm.blindfaithgames.engine.input.Input.EventType;
import es.eucm.blindfaithgames.engine.others.RuntimeConfig;
import es.eucm.blindfaithgames.engine.sound.TTS;
import es.eucm.blindfaithgames.engine.sound.VolumeManager;
import es.eucm.blindfaithgames.tss.R;

public class GameOver extends GameState {
	
	private Text text;
	
	private int stepsPerWord = RuntimeConfig.TEXT_SPEED;

	
	public GameOver(View v, TTS textToSpeech, Context c, Game game) {
		super(v,c,textToSpeech, game);
		
		int textoffSetX;
		int textoffSetY;
		float fontSize;
		Typeface font;
		Paint brush;
		
		fontSize =  (this.getContext().getResources().getDimension(R.dimen.font_size_game_over)/GameState.scale);
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.GAME_FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setColor(this.getContext().getResources().getColor(R.color.green0));
		if(font != null)
			brush.setTypeface(font);
	
		textoffSetX = SCREEN_WIDTH  / 4;
		textoffSetY = SCREEN_HEIGHT / 2;
		text = new Text(textoffSetX, textoffSetY, null, this, null, null, 
				null, null, false, brush, stepsPerWord, this.getContext().getString(R.string.game_over_text));
		this.addEntity(text);
		
	}
	
	@Override
	public void onInit() {
		super.onInit();
		getTTS().speak(this.context.getString(R.string.game_over_tts));
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
			this.stop();
		}
		else{
			if(e != null){
				text.setStepsPerWord(1);
			}
		}
	}
}
