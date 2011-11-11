package org.example.minesweeper;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

enum Actions{ZOOM, EXPLORATION, INSTRUCTIONS, COORD};

public class KeyConf extends Activity implements OnKeyListener,OnItemSelectedListener{
	
	private HashMap<Actions, Integer> map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyconf);	
		map = new HashMap();
		Spinner spinnerZoom = (Spinner) findViewById(R.id.spinnerZoom);
		spinnerZoom.setOnItemSelectedListener(this);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.keys, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinnerZoom.setAdapter(adapter);
		
		Spinner spinnerExploration = (Spinner) findViewById(R.id.spinnerExploration);	
		spinnerExploration.setOnItemSelectedListener(this);
		spinnerExploration.setAdapter(adapter);
		
		Spinner spinnerInstructions = (Spinner) findViewById(R.id.spinnerInstructions);
		spinnerInstructions.setOnItemSelectedListener(this);
		spinnerInstructions.setAdapter(adapter);
		
		Spinner spinnerCoordinates = (Spinner) findViewById(R.id.spinnerCoordinates);
		spinnerCoordinates.setOnItemSelectedListener(this);
		spinnerCoordinates.setAdapter(adapter);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		String s = "";
		switch (parent.getId()){
		case R.id.spinnerZoom: s = "Zoom";
			map.put(Actions.ZOOM, KeyEvent.KEYCODE_VOLUME_UP);
			break;
		case R.id.spinnerExploration: s = "Exploration";
			break;
		case R.id.spinnerInstructions: s = "Instructions";
			break;
		case R.id.spinnerCoordinates: s = "Coordinates";
			break;
		}
	      Toast.makeText(parent.getContext(), "The changed option is " + s +
	              parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
