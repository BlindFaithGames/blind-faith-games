package org.example.activities;

import org.example.golf.GolfGame;
import org.example.golf.GolfGameView;

import android.app.Activity;
import android.os.Bundle;

public class GolfGameActivity extends Activity {
	
	private GolfGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GolfGameView(null, game));
    }
}
