package com.exp.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exp.game.util.GameButtonClickListener;
import com.exp.game.util.SPUtils;
import com.exp.game.view.SurfaceViewLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

//game interface
public class GameActivity extends AppCompatActivity implements SurfaceViewLayout.ActionListener, DialogInterface.OnDismissListener {

    private SurfaceViewLayout mGameLayout; //GameView
    private View left; //buttonleft
    private View right; //buttonright
    private SoundPool soundPool; //PlayBGM
    private int load; //loadIdofBGM
    private AlertDialog dialog; //GameOverdialog
    private TextView title; //dialogtitle
    private boolean saved; //savedornot
    private EditText usernameEditText; //InputPlayerName

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SPUtils.init(this);
        mGameLayout = (SurfaceViewLayout) findViewById(R.id.game);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        left.setOnTouchListener(new GameButtonClickListener() {
            @Override
            public void up() {
                //frontface
                mGameLayout.persongNormal();
            }

            @Override
            public void handleClickEvent(View view) {
                //moveleft
                mGameLayout.moveLeft();
            }
        });


        right.setOnTouchListener(new GameButtonClickListener() {
            @Override
            public void up() {
                //frontface
                mGameLayout.persongNormal();
            }

            @Override
            public void handleClickEvent(View view) {
                //move to right
                mGameLayout.moveRight();
            }
        });
        mGameLayout.setActionListener(this); //set up for start and listener
        startBgm(); //PlayBGM
        //dialog initial
        LayoutInflater factory = LayoutInflater.from(this);
        View dialogView = factory.inflate(R.layout.result_put,
                null);
        dialogView.setFocusableInTouchMode(true);
        dialogView.requestFocus();
        usernameEditText = (EditText) dialogView
                .findViewById(R.id.namefield);
        title = (TextView) dialogView.findViewById(R.id.title);
        dialog = new AlertDialog.Builder(this)
                .setView(dialogView).create();
        dialog.setCanceledOnTouchOutside(false);
        dialogView.findViewById(R.id.retry).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mGameLayout.restartGame();
                    }
                });

        dialogView.findViewById(R.id.post_scores).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(saved){
                            Toast.makeText(GameActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Save score
                        String userName = null;
                        if (usernameEditText.getText() != null) {
                            userName = usernameEditText.getText()
                                    .toString().replace("\n", " ")
                                    .trim();
                            App.getDaoInstant().getDBScoreDao().insert(
                                    new DBScore(System.currentTimeMillis(),mGameLayout.mTotalScore,
                                            userName,getDate()));
                            Toast.makeText(GameActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            saved = true;
                        }
                        if (userName.length() > 0
                                && userName.length() < 20) {
                        } else {
                            Toast.makeText(GameActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dialogView.findViewById(R.id.goback).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //go back to home page
                        GameActivity.this.finish();
                    }
                });
        dialog.setOnDismissListener(this);
    }

    private String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    //playing BGM
    private void startBgm() {
        load = soundPool.load(this, R.raw.background, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            float streamVolumeCurrent = mgr
                    .getStreamVolume(AudioManager.STREAM_MUSIC);

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(load, streamVolumeCurrent, streamVolumeCurrent, 1, -1, 1f);
            }
        });
    }

    //stop playing BGM
    private void stopBgm() {
        soundPool.stop(load);
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.pause(load); //Pause BGM
        mGameLayout.setPause(true); //Pause
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundPool.resume(load); //BGM go on
        mGameLayout.setPause(false); //set game go on
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameLayout.stop(); //stop the game
        stopBgm(); //stop the BGM
        soundPool.release();
    }

    @Override //start listen
    public void start() {
        startBgm(); //start bgm
    }

    @Override //listen end
    public void over() {
        stopBgm();
        if(!dialog.isShowing()){ //pop out dialog
            title.setText("Game Over（Score："+mGameLayout.mTotalScore+"）");
            usernameEditText.setText("");
            dialog.show();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        saved = false; //reset
    }
}
