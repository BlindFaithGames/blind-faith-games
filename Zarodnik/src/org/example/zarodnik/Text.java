package org.example.zarodnik;

import java.util.List;
import java.util.StringTokenizer;

import org.example.R;
import org.example.others.RuntimeConfig;
import org.example.tinyEngineClasses.Entity;
import org.example.tinyEngineClasses.GameState;
import org.example.tinyEngineClasses.Mask;
import org.example.tinyEngineClasses.SpriteMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

public class Text extends Entity {

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
                if(this.x +  (lineSize) + brush.measureText(aux) > GameState.SCREEN_WIDTH){
                        row++;
                        lineSize = 0;
                }
                canvas.drawText(aux, this.x + lineSize, this.y + row*fontSize, brush);
                lineSize += brush.measureText(aux) + WHITE_SPACE_SIZE;
                i++;
        }
		if (steps == stepsPerWord){
			steps = 0;
			if(nextWord <= nTokens)
				nextWord++;
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

}
