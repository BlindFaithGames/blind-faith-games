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
import com.accgames.sound.TTS;
import com.accgames.sound.VolumeManager;
import com.juego4.R;

public class Gameplay extends GameState {
	
	private enum StateGameplay { SELECT_NPC, SELECT_SCENE, SHOW_DIALOG, SHOW_INTRO};
	
	private Text text;
	
	private SceneManager scm;
	
	private StateGameplay state;
	
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
				null, null, false, brush, stepsPerWord, "");
		this.addEntity(text);
		
		// TODO: scm = new SceneManager(sceneBuffer);
		state = StateGameplay.SHOW_INTRO;
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

		switch(state){
			case SHOW_INTRO:
				scm.showIntro(text);
				state = StateGameplay.SELECT_NPC;
				break;
			case SELECT_NPC:
				e = Input.getInput().removeEvent("onLongPress");
				int nOptions = scm.showNPCOptions(text);
				if(e != null){
					int selectedNPC = screenPosToSelectedOption(e,nOptions);
					scm.changeNPC(selectedNPC);
					state = StateGameplay.SHOW_DIALOG;
				}
				break;
			case SHOW_DIALOG:
				if(!scm.updateDialog(text)){
					state = StateGameplay.SELECT_SCENE;
				}
				break;
			case SELECT_SCENE:
					e = Input.getInput().removeEvent("onLongPress");
					int nScenes = scm.showSceneOptions(text);
					if(e != null){
						int selectedScene = screenPosToSelectedOption(e,nScenes);
						scm.changeScene(selectedScene);
						state = StateGameplay.SELECT_NPC;
					}
					break;

		}
	}
	
	private int screenPosToSelectedOption(EventType e, int nOptions){
		int inc = GameState.SCREEN_HEIGHT / nOptions;
		int top = 0;
		int bot = inc;
		int counter = 0;
		boolean found = false;
		while(!found && counter < nOptions){
			if(e.getMotionEventE1().getY() > top && e.getMotionEventE1().getY() < bot){
				found = true;
			}
			counter++;
			top += inc;
			bot += inc;
		}
		if (found)
			return counter--;
		else
			return -1;
	}
}
