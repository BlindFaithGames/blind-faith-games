package org.example.Activities;

import org.example.GolfGame.GolfGame;
import org.example.GolfGame.GolfGameView;

import android.app.Activity;
import android.os.Bundle;

public class GolfGameActivity extends Activity {
	
	private GolfGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GolfGameView());
    }
}
