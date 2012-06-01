package es.eucm.blindfaithgames.tss.game;

import java.io.InputStream;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
import es.eucm.blindfaithgames.tss.input.ScenesReader;

public class Gameplay extends GameState {
	
	private enum StateGameplay { SET_DIALOG, SET_INTRO, SHOW_SELECT_NPC, SHOW_DIALOG, SHOW_TEXT_INTRO,
								SET_SELECT_NPC, SET_SELECT_SCENE, SHOW_SELECT_SCENE};
	
	private Text text;
	private Paint brush;
	private int focus;
	private boolean focusChange;
	
	private SceneManager scm;
	
	private StateGameplay state;
	private boolean transitionDialog;
	private int nScenes;
	private int nNPCs;
	
	private final static String typeInteraction = "onDoubleTap";
	
	private static final int textoffSetX = 0;
	private static final int textoffSetY = 40;
	
	private int stepsPerWord = 1;

	public Gameplay(View v, TTS textToSpeech, Context c, Game game) {
		super(v,c,textToSpeech, game);

		float fontSize;
		Typeface font;
		fontSize =  (this.getContext().getResources().getDimension(R.dimen.font_size_intro)/GameState.scale);
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.GAME_FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setColor(this.getContext().getResources().getColor(R.color.green0));
		if(font != null)
			brush.setTypeface(font);
		
		text = new Text(textoffSetX, textoffSetY, null, this, null, null, 
				null, null, false, brush, stepsPerWord, "");
		this.addEntity(text);
		
		InputStream is = this.context.getResources().openRawResource(R.raw.game_script);
		ScenesReader scenesReader = new ScenesReader();
		scm = scenesReader.loadAdventure(is);
		
		state = StateGameplay.SET_INTRO;
		
		focus = -1;
		
		this.getTTS().setQueueMode(TTS.QUEUE_FLUSH);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		super.onDraw(canvas);
		
		if(scm != null){
			canvas.drawText("ID: " + String.valueOf(scm.getIDCurrentScene()), 25, 25, brush);
		}
		
		if(state == StateGameplay.SHOW_SELECT_NPC)
			drawButtons(canvas,nNPCs);
		else
			if(state == StateGameplay.SHOW_SELECT_SCENE)
				drawButtons(canvas,nScenes);
	}
	
	private void drawButtons(Canvas canvas, int nOptions) {
		if(nOptions > 0){
			int inc = GameState.SCREEN_HEIGHT / nOptions;
			int bot = inc;
			Paint localBrush = new Paint(brush);
			localBrush.setColor(this.v.getResources().getColor(R.color.red2));
			localBrush.setStrokeWidth(1);
			canvas.drawText(this.getView().getResources().getString(R.string.optionButton) + " " + 0, GameState.SCREEN_WIDTH/2, inc/2, localBrush);
			for(int i = 0; i < nOptions; i++){
				canvas.drawText(this.getView().getResources().getString(R.string.optionButton) + " " +  (i + 1), GameState.SCREEN_WIDTH/2, bot + inc/2, localBrush);
				canvas.drawLine(0, bot, GameState.SCREEN_WIDTH, bot, localBrush);
				bot += inc;
			}
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

		manageTTS();
		
		manageSceneManager();
	}
	
	private void manageTTS() {
		EventType e1 = Input.getInput().removeEvent("onDrag");
		EventType e2 = Input.getInput().removeEvent("onDown");
		if(e1 != null){
			readButtons(e1, true);
		}
		else if(e2 != null) {
			readButtons(e2, false);
		}
	}
	
	private void readButtons(EventType e, boolean focusRestriction) {
		int optionSelected = -1;
		String msg = null;
		if(state == StateGameplay.SHOW_SELECT_NPC){
			optionSelected = screenPosToSelectedOption(e, nNPCs);
			if((optionSelected < scm.getNPCS().size() && optionSelected  >= 0)){
				msg = scm.getNPCS().get(optionSelected).getName();
			}
		}else{
			if(state == StateGameplay.SHOW_SELECT_SCENE){
				optionSelected = screenPosToSelectedOption(e, nScenes);
				Integer scene;
				if(optionSelected  < scm.getNextScenes().size() && optionSelected  >= 0) {
					scene = scm.getNextScenes().get(optionSelected);
					msg = scm.getScene(scene).getDescription();
				}
			}
		}
		focusChange = optionSelected != focus;
		if(msg != null && (focusChange || !focusRestriction)){
			this.getTTS().speak(msg.replace(" //", "."));
			focus = optionSelected;
		}
	}

	private void manageSceneManager(){
		EventType e;
		switch(state){
		case SET_INTRO:
			if(scm.setIntro(text))
				state = StateGameplay.SHOW_TEXT_INTRO;
			else
				state = StateGameplay.SET_SELECT_NPC;
			this.getTTS().speak(text.getText().replace(" //", "."));
			break;
		case SHOW_TEXT_INTRO:
			e = Input.getInput().removeEvent(typeInteraction);
			if(e != null)
				state = StateGameplay.SET_SELECT_NPC;
			break;
		case SET_SELECT_NPC:
			nNPCs = scm.showNPCOptions(text);
			if(nNPCs == 0)
				state = StateGameplay.SET_SELECT_SCENE;
			else{
				this.getTTS().speak(text.getText().replace(" //", "."));
				state = StateGameplay.SHOW_SELECT_NPC;
			}
			break;
		case SHOW_SELECT_NPC:
			e = Input.getInput().removeEvent(typeInteraction);
			if(e != null){
				int selectedNPC = screenPosToSelectedOption(e, nNPCs);
				transitionDialog = scm.changeNPC(selectedNPC);
				state = StateGameplay.SET_DIALOG;
				text.setText("");
				this.getTTS().speak(scm.getCurrentDialog().replace(" //", "."));
			}
			break;
		case SET_DIALOG:
			if(!text.isWriting()){
				if(!scm.updateDialog(text))
					state = StateGameplay.SHOW_DIALOG;
			}
			break;
		case SHOW_DIALOG:
			e = Input.getInput().removeEvent(typeInteraction);
			if(e != null){
				if(!transitionDialog)
					state = StateGameplay.SET_SELECT_NPC;
				else
					state = StateGameplay.SET_SELECT_SCENE;
			}
			break;
		case SET_SELECT_SCENE:
			//delete finished scenes
			scm.deleteScenes();
			nScenes = scm.showSceneOptions(text);
			if(nScenes == 0)
				this.stop();
			this.getTTS().speak(text.getText().replace(" //", "."));
			state = StateGameplay.SHOW_SELECT_SCENE;
			break;
		case SHOW_SELECT_SCENE:
			e = Input.getInput().removeEvent(typeInteraction);
			if(e != null){
				int selectedScene = screenPosToSelectedOption(e, nScenes);
				scm.changeScene(selectedScene);
				state = StateGameplay.SET_INTRO;
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
			if(!found){
				counter++;
				top += inc;
				bot += inc;
			}
		}
		if (found)
			return counter;
		else
			return -1;
	}
}
