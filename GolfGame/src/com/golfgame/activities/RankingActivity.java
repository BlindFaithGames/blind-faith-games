package com.golfgame.activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accgames.input.Input;
import com.accgames.others.AnalyticsManager;
import com.accgames.sound.TTS;
import com.golfgame.R;
import com.golfgame.game.GolfGameAnalytics;

public class RankingActivity extends Activity implements  OnFocusChangeListener, OnClickListener{
	
	private TTS textToSpeech;
	
	public static final String DATE_FORMAT = "HH:mm:ss dd-MM-yyyy";

	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.previous_games);
        
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		
		String newEntry = getIntent().getStringExtra(MainActivity.KEY_RESULTS);

		String[] data = loadPreviousInfo(newEntry);

		if(data == null){
			data = new String[1];
			data[0] = sdf.format(c.getTime()) + "      " + newEntry;
		}
		else{
			data[data.length-1] = sdf.format(c.getTime()) + "      " + newEntry;
		}
			
		createTable(getString(R.string.ranking_title), 2, data.length, 1, "#0055AA", data);

		saveNewData(data);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
		textToSpeech.setInitialSpeech(getString(R.string.ranking_speech));
		
		AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.RANKING_ACTIVITY);
		
		AnalyticsManager.getAnalyticsManager(this).registerAction(GolfGameAnalytics.MISCELLANEOUS, GolfGameAnalytics.GAME_RESULT_STAGE, 
				newEntry, 0);
	}

	private void saveNewData(String[] data) {
		FileOutputStream fos;
		try { 
			fos = this.openFileOutput(MainActivity.FILENAMESTAGEMODE, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data.length);
			for(int i = 0; i < data.length; i++){
				oos.writeObject(data[i]); 
			}
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] loadPreviousInfo(String s) {
		FileInputStream fis;
		String[] data = null;
		int size = 0;
		try { 
			fis = this.openFileInput(MainActivity.FILENAMESTAGEMODE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object f = ois.readObject();
			size = (Integer) f;
			data = new String[size+1];
			for(int i = 0; i < size; i++){
				f = ois.readObject();
				data[i] = (String)f;
			}
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	

    public TableLayout createTable(String headerText, int borderWidth, int rowNumber, int colNumber, String borderColor, String[] data){
    	TableLayout table =  (TableLayout) findViewById(R.id.table_ranking);
	   
    	TextView header = (TextView) findViewById(R.id.header_ranking);
	    header.setText(headerText);
	    header.setGravity(Gravity.CENTER);
	    
    	if(rowNumber>0 && colNumber>0){
	    	TableRow row = new TableRow(this);
	    	
	    	int cellNumber = rowNumber * colNumber;
	    	
	    	int width=(getWindowManager().getDefaultDisplay().getWidth()/colNumber)-(borderWidth+(borderWidth/(colNumber)));
	    	width--;
	    	
	    	int columnCounter=0;
	        int rowCounter=0;
	    	
	        for (int i = 0; i < cellNumber; i++) {
	        	if(columnCounter==colNumber){
	        		table.addView(row);
	        		row = new TableRow(this);
	        		columnCounter=0;
	        		rowCounter++;
	        	}

	        	RelativeLayout border = new RelativeLayout(this);
	        	border.setPadding(borderWidth,borderWidth,0,0);
	        	if(columnCounter==colNumber){
	        		border.setPadding(borderWidth, borderWidth, borderWidth, 0);
	        	}
	        	// If it's the last row
	        	if(rowCounter==rowNumber-1){
	        		//Draws up left down
	        		border.setPadding(borderWidth,borderWidth,0,borderWidth);
	        		// If it's the last column
	        		if(columnCounter==(colNumber-1)){
	        			//draws all the borders
	        			border.setPadding(borderWidth,borderWidth,borderWidth,borderWidth);
	        		}
	        	}
	        	// Border Color
	        	border.setBackgroundColor(Color.parseColor(borderColor));
	        	
	        	// we create the buttons
	        	Button b = new Button(this);
	 
	        	b.setText(data[i]);
	        	b.setTextColor(Color.WHITE);
	        	b.setWidth(width);
	        	b.setGravity(Gravity.CENTER);
	        	b.setPadding(4, 4, 4, 4);
	        	b.setOnFocusChangeListener(this);
	        	b.setOnClickListener(this);
	        	b.setBackgroundColor(Color.BLACK);
   	
	        	border.addView(b);
	        	
	        	row.addView(border);
	        	columnCounter++;
			}

        	table.addView(row);

    	}else{
            TextView error= new TextView(this);
            error.setText("Valores de columnas o filas deben ser mayor de 0");
            table.addView(error);
		}
    	return table;
    }
	

	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		 super.onDestroy();
	     textToSpeech.stop();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		textToSpeech.speak(getString(R.string.ranking_speech));
		textToSpeech.setQueueMode(TTS.QUEUE_ADD);
		textToSpeech.speak(((Button) v).getText().toString());
		textToSpeech.setQueueMode(TTS.QUEUE_FLUSH);
		if(hasFocus)
			v.setBackgroundColor(Color.parseColor("#64ff8000"));
		else
			v.setBackgroundColor(Color.BLACK);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Integer key = Input.getKeyboard().getKeyByAction(KeyConfActivity.ACTION_REPEAT);
		if(key != null){
			if (keyCode == key) {
				textToSpeech.repeatSpeak();
				return true;
			} 
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		textToSpeech.speak(getString(R.string.ranking_speech) + ((Button) v).getText().toString());
	}
}
