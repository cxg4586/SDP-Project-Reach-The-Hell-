package com.exp.game.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * On touchListener
 */
public abstract class GameButtonClickListener implements View.OnTouchListener {
    private boolean isContinue;
    private Thread mThread;
    //Create one Handler
    private volatile MyHandler mHandler;

    private int what;
    public final static int interval = 20;
    private View view;

    public GameButtonClickListener() {
        //call in main
        if (mThread == null)
            mHandler = new MyHandler();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.view = v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isContinue = true;
                mThread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (isContinue) {
                            mHandler.sendEmptyMessage(what);
                            Log.v("@msg-what", what + "");
                            try {
                                Thread.sleep(interval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                mThread.start();
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacksAndMessages(null);
                isContinue = false;
                mThread = null;
                up();
                break;
            case MotionEvent.ACTION_CANCEL:
                mHandler.removeCallbacksAndMessages(null);
                isContinue = false;
                mThread = null;
                up();
                break;
        }

        return true;
    }

    public abstract void up();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleClickEvent(view);
        }
    }

    public abstract void handleClickEvent(View view);


}
