package com.exp.game;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

//home page
public class HomeActivity extends Activity implements OnClickListener {

	private SharedPreferences mBaseSettings;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);

		Button startButton = (Button) findViewById(R.id.start_game);
		startButton.setOnClickListener(this);

		Button scoreBoardButton = (Button) findViewById(R.id.score_board);
		scoreBoardButton.setOnClickListener(this);

		Button exitButton = (Button) findViewById(R.id.exit);
		exitButton.setOnClickListener(this);

		mBaseSettings = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = null;
		switch (v.getId()) {
		case R.id.start_game:
			//go to game site
			i = new Intent(this, GameActivity.class);
			break;
		case R.id.score_board:
			//go to record site
			i = new Intent(this, ScoreActivity.class);
			break;

		case R.id.exit:
			finish(); //Exit
			return;
		}
		if (i != null) {
			startActivity(i);
		}
	}
}
