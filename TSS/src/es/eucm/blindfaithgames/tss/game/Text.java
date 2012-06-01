package es.eucm.blindfaithgames.tss.game;

import java.util.List;
import java.util.StringTokenizer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import es.eucm.blindfaithgames.engine.general.Entity;
import es.eucm.blindfaithgames.engine.general.GameState;
import es.eucm.blindfaithgames.engine.general.Mask;
import es.eucm.blindfaithgames.engine.graphics.SpriteMap;
import es.eucm.blindfaithgames.engine.others.RuntimeConfig;
import es.eucm.blindfaithgames.tss.R;

public class Text extends Entity {

	public static final String SEPARATOR = " // ";
	private float fontSize;
	private Typeface font;
	private Paint brush;
	
	private String text;
	private int nextWord;
	private int steps;
	private int nTokens;
	
	private int stepsPerWord = RuntimeConfig.TEXT_SPEED;
	private float WHITE_SPACE_SIZE;
	
	public Text(int x, int y, Bitmap img, GameState gameState, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset,
			boolean collide, Paint brush, int stepsPerWord, String text) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		this.text = text;
		nextWord = 0;
		steps = 0;
		
		this.stepsPerWord = stepsPerWord;
		
		WHITE_SPACE_SIZE =  (this.gameState.getContext().getResources().getDimension(R.dimen.white_space_size)/GameState.scale);
		fontSize =  (this.gameState.getContext().getResources().getDimension(R.dimen.font_size_intro)/GameState.scale);
		font = Typeface.createFromAsset(this.gameState.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		if(brush != null){
			this.brush = brush;
		}else{
			font = Typeface.createFromAsset(this.gameState.getContext().getAssets(),RuntimeConfig.FONT_PATH);
			this.brush = new Paint();
			this.brush.setTextSize(fontSize);
			this.brush.setARGB(255, 255, 255, 204);
			if(font != null)
				this.brush.setTypeface(font);
		}
		
        StringTokenizer stk = new StringTokenizer(text);
        nTokens =  stk.countTokens();
	}

	public int getnTokens() {
		return nTokens;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		introTextEffect(canvas);
		super.onDraw(canvas);
	}
	
	private void introTextEffect(Canvas canvas) {
		int i; int row;
		String aux;
        StringTokenizer stk = new StringTokenizer(text);
        int nTokens =  stk.countTokens();
        i = 0;
        row = 1;
        int lineSize = 0;
        while(i < nextWord && stk.hasMoreTokens()){
                aux  = stk.nextToken();
                if(this.x +  (lineSize) + brush.measureText(aux) > GameState.SCREEN_WIDTH || aux.equals("//")){
                        row++;
                        lineSize = 0;
                        if(this.y + (row+1)*fontSize > GameState.SCREEN_HEIGHT){
                        	row = 1;
                        	clearText();
                        }
                }
                if(!aux.equals("//")){
                	canvas.drawText(aux, this.x + lineSize, this.y + row*fontSize, brush);
                	lineSize += brush.measureText(aux) + WHITE_SPACE_SIZE;
                }
                i++;
        }
		if (steps == stepsPerWord){
			steps = 0;
			if(nextWord <= nTokens)
				nextWord++;
		}
	}
	
	private void clearText() {
		StringTokenizer stk = new StringTokenizer(text, SEPARATOR);
		String aux;
		while(stk.hasMoreTokens()){
			aux = stk.nextToken();
			if(!stk.hasMoreTokens()){
				text = aux;
				nextWord = 0;
				steps = 0;
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		steps++;
	}
	
	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {}

	public boolean isFinished() {
		return nTokens == nextWord-1;
	}

	public void setStepsPerWord(int stepsPerWord) {
		this.stepsPerWord = stepsPerWord;
		steps = 0;
	}

	public void setText(String text) {
		this.text = text;
		nextWord = 0;
		steps = 0;
	}

	public boolean isWriting() {
		StringTokenizer stk = new StringTokenizer(text);
		return nextWord <= stk.countTokens();
	}

	public void concatText(String speech) {
		this.text += SEPARATOR + speech;
	}

}
