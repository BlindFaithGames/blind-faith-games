package org.example.zarodnik;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.example.R;
import org.example.tinyEngineClasses.Music;

import android.content.Context;

public class Simon {
	
	public static final int UP_LEFT = 0;
	public static final int UP_RIGHT = 1;
	public static final int DOWN_LEFT = 2;
	public static final int DOWN_RIGHT = 3;

	private Context mContext;
	
	private ArrayList<Integer> sequence;
	
	private int next;
	private int chances;
	
	public Simon(Context context){
		mContext = context;
		sequence = new ArrayList<Integer>();
		next = 0;
		chances = 3;
	}
	
	public void playSequence(){
		Random numberGenerator = new Random();
		sequence.add(numberGenerator.nextInt(4));
		playSounds(sequence);
	}
	
	private void playSounds(ArrayList<Integer> sequence) {
		Iterator<Integer> it = sequence.iterator();
		int n;
		while(it.hasNext()){
			n = it.next();
			switch(n){
			// TODO find sounds
				case UP_LEFT:
					//Music.getInstanceMusic().play(mContext, R.raw.up_left, false);
					break;
				case UP_RIGHT:
					//Music.getInstanceMusic().play(mContext, R.raw.up_right, false);
					break;
				case DOWN_LEFT:
					//Music.getInstanceMusic().play(mContext, R.raw.down_left, false);
					break;
				case DOWN_RIGHT:
					//Music.getInstanceMusic().play(mContext, R.raw.down_right, false);
					break;
				default:
					break;
			}
		}
		
	}

	public boolean checkAnswer(int ans){
		boolean found = false;
		
		if(next < sequence.size())
			found = sequence.get(next) == ans;

		if(found)
				chances--;
			
		next++;
		
		return next == sequence.size();
	}

	public boolean isGameOver() {
		return chances == 0;
	}
}
