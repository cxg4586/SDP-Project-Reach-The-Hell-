package com.exp.game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.exp.game.R;
import com.exp.game.util.RandomUtil;

import java.util.ArrayList;

public class SurfaceViewLayout extends View {

    public interface ActionListener{
        void start();
        void over();
    }

    private ActionListener actionListener;

    //Gamelayout
    private int mLayoutWidth;
    private int mLayoutHeight;
    private Barrier mBarrier;
    private Person mPerson;
    private Score mScore;
    private Paint mPaint;
    private int radius = 65;
    private Thread mThread;

    private MyHandler myHandler;
    private int mBarrierMoveSpeed = 5;
    private boolean isAutoFall;
    private boolean isRunning;
    private int mPersonMoveSpeed = 20;

    private ArrayList<Barrier> mBarriers;
    private ArrayList<Integer> mBarrierXs;
    private ArrayList<Integer> mBarrierYs;
    private int mBarrierStartY = 500;
    private int mBarrierInterval = 500;
    private int mBarrierHeight = 60;
    
    private int mTouchIndex = -1;

    private float mFallTime = 0;

    private float mPreSocre = 0;

    public static final float G = 9.8f;

    public int mTotalScore;

    private int mTextSize = 17;

  
    private RectF mRestartRectf;
    private RectF mQuiteRectf;
    
    private int mButtonWidth = 300;
    private int mButtonHeight = 120;
    private int Padding = 20;

   
    public static final int BLOOD_ALL = 100;
   
    private static final int BLOOD_TOP_SUB = (int) (BLOOD_ALL * 0.8f);
   
    private static final int BLOOD_FOOT_BOARD_SUB = 10;
   
    private static final int BLOOD_FOOT_BOARD_ADD = 3;

    public static final int TOP_BAR_JUDGE = 100;

  
    private Drawable mHpBarTotalImage;
    private Drawable mHpBarRemainImage;

    private ScreenAttribute mScreenAttribute;

    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public SurfaceViewLayout(Context context) {
        super(context);
        init();
    }

    public SurfaceViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
      
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(10);
      
        isAutoFall = true;
        myHandler = new MyHandler();
        mBarriers = new ArrayList<>();
        
        mBarrierXs = new ArrayList<>();
        
        mBarrierYs = new ArrayList<>();
        
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics());
        
        isRunning = true;
        mHpBarTotalImage = getResources().getDrawable(R.drawable.hp_bar_total);
        mHpBarRemainImage = getResources().getDrawable(R.drawable.hp_bar_remain);
        startGame();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        mLayoutWidth = getMeasuredWidth();
        mLayoutHeight = getMeasuredHeight();
        mScreenAttribute = new ScreenAttribute(0, 20, mLayoutWidth, mLayoutHeight);
        
        mBarrier = new Barrier(getContext(), mLayoutWidth, mPaint);
        mBarrier.setHeight(mBarrierHeight);
      
        mPerson = new Person(getContext(), mPaint, radius);
        mPerson.mPersonY = 300;
        mPerson.mPersonX = mLayoutWidth / 2;
        
        mScore = new Score(mPaint);
        mScore.x = mLayoutWidth / 2 - mScore.panelWidth / 2;

        
        int rX = mLayoutWidth / 2 - 20 - mButtonWidth;
        int rY = mLayoutHeight * 3 / 5;
        mRestartRectf = new RectF(rX, rY, rX + mButtonWidth, rY + mButtonHeight);
     
        int qX = mLayoutWidth / 2 + 20;
        int qY = mLayoutHeight * 3 / 5;
        mQuiteRectf = new RectF(qX, qY, qX + mButtonWidth, qY + mButtonHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mTotalScore - mPreSocre) > SPEED_ADD_SCORE) {
           
            mPreSocre = mTotalScore;
            mBarrierMoveSpeed += SPEED_ADD;
            postInvalidate();
            return;
        }
     
        if (topAndSubBlood()) {
            mPerson.blood -= BLOOD_TOP_SUB;
            mPerson.mPersonY += mBarrierHeight * 2;
            isAutoFall = true;
            postInvalidate();
            return;
        }
        
        generateTop(canvas);
       
        generateBarrier(canvas);
        
        if (isAutoFall)
            checkTouch();
        
        generatePerson(canvas);
       
        isRunning = !checkIsGameOver();
        
        if (!isRunning) {
            if(actionListener != null){
                actionListener.over();
            }
        }

        mHpBarTotalImage.setBounds(
                (this.mScreenAttribute.maxX / 9),
                this.mScreenAttribute.maxY - 65,
                (this.mScreenAttribute.maxX * 8 / 9),
                this.mScreenAttribute.maxY - 40);
        mHpBarTotalImage.draw(canvas);
        mHpBarRemainImage
                .setBounds(
                        (this.mScreenAttribute.maxX / 9),
                        this.mScreenAttribute.maxY - 65,
                        (int) (this.mScreenAttribute.maxX * 8 / 9 * (float) (mPerson.blood * 1.f / BLOOD_ALL)),
                        this.mScreenAttribute.maxY - 40);
        mHpBarRemainImage.draw(canvas);
     
        generateScore(canvas);
    }

    private void generateTop(Canvas canvas) {
        BitmapDrawable bitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.top_bar);
        canvas.drawBitmap(bitmap.getBitmap(), 0, 0, mPaint);
    }

    /**
     * 
     *
     * 
     */
    private void drawPanel(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#8e333333"));
        canvas.drawRoundRect(new RectF(mRestartRectf.left - Padding * 2, mLayoutHeight * 2 / 5 - Padding, mQuiteRectf.right + Padding * 2, mQuiteRectf.bottom + Padding), Padding, Padding, mPaint);
    }

    /**
     *
     */
    private void notifyGameOver(Canvas canvas) {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize * 1.5f);
        mPaint.setColor(Color.parseColor("#cc0000"));
        mPaint.setFakeBoldText(false);
        canvas.drawText("Game over", mLayoutWidth / 2, mLayoutHeight / 2, mPaint);
    }

    
    private void drawButton(Canvas canvas, RectF rectF, String text, int strokeColor, int textColor) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(strokeColor);
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(textColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        int y = (int) (rectF.top + textHeight / 2 + (rectF.bottom - rectF.top) / 2 - fontMetrics.bottom);
        canvas.drawText(text, rectF.left + mButtonWidth / 2, y, mPaint);

    }

    /**
     * 
     *
     * 
     */
    private void generateScore(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#666666"));
        // mScore.drawPanel(canvas);
        mPaint.setColor(Color.WHITE);
        mPaint.setFakeBoldText(true);
        mPaint.setTextSize(mTextSize);
        mScore.drawScore(canvas, mTotalScore + "");
    }

    /**
     * 
     * 
     * 
     */
    private void generateBarrier(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.DKGRAY);
      
        mBarrierYs.clear();
        
        for (int i = 0; ; ) {
            
            if (i < mBarrierXs.size()) {
                mBarrier.mPositionX = mBarrierXs.get(i);
            } else {
                mBarrier.mPositionX = RandomUtil.getRangeX(mLayoutWidth);
                mBarrierXs.add(mBarrier.mPositionX);
            }
           
            mBarrier.mPositionY = mBarrierStartY + mBarrierInterval * i;
            mBarrierYs.add(mBarrier.mPositionY);
            Barrier b = null;
            if (i < mBarriers.size()) {
                b = mBarriers.get(i);
                b.mPositionX = mBarrier.mPositionX;
                b.mPositionY = mBarrier.mPositionY;
            } else {
                b = new Barrier(getContext(), mLayoutWidth, mPaint);
                b.setHeight(mBarrierHeight);
                b.mPositionX = mBarrier.mPositionX;
                b.mPositionY = mBarrier.mPositionY;
                b.setType(RandomUtil.getRandomType());
                mBarriers.add(b);
            }
            
            if (mBarrier.mPositionY > mLayoutHeight) {
                break;
            }
            b.drawBarrier(getResources(), canvas);
            i++;
        }
    }

    public static final int SPEED_ADD = 1;
    public static final int SPEED_ADD_SCORE = 20;

    private void generatePerson(Canvas canvas) {
        
        if (isAutoFall) {
          
            mFallTime += 20;
            
            mPerson.mPersonY += mFallTime / 1000 * G;
            mPerson.draw(canvas);
        } else {
            
            Log.v("@time", mFallTime / 1000 + "");
            
            mFallTime = 0;
            
            if (mTouchIndex >= 0) {
               
                mPerson.mPersonY = mBarrierYs.get(mTouchIndex) - 2 * radius;
                mPerson.draw(canvas);
            }
        }
    }

  
    private Barrier preSubBarrier;

    /**
     *
     */
    private void checkTouch() {
        for (int i = 0; i < Math.min(mBarrierXs.size(), mBarrierYs.size()); i++) {
            
            if (isTouchBarrier(mBarrierXs.get(i), mBarrierYs.get(i))) {
                mTouchIndex = i;
                isAutoFall = false;
                Barrier barrier = mBarriers.get(i);
                if (barrier != preSubBarrier && barrier.getType() == 1) {
                    mPerson.blood -= BLOOD_FOOT_BOARD_SUB;
                }
                if (barrier != preSubBarrier && barrier.getType() == 0) {
                    mPerson.blood += BLOOD_FOOT_BOARD_ADD;
                    if (mPerson.blood > BLOOD_ALL) {
                        mPerson.blood = BLOOD_ALL;
                    }
                }
                preSubBarrier = barrier;
            }
        }
    }

    private boolean checkIsGameOver() {
        return mPerson.mPersonY < TOP_BAR_JUDGE && mPerson.blood <= BLOOD_TOP_SUB
                || mPerson.mPersonY > mLayoutHeight - 2 * radius || mPerson.blood <= 0;
    }

    private boolean topAndSubBlood() {
        return mPerson.mPersonY < TOP_BAR_JUDGE && mPerson.blood > BLOOD_TOP_SUB;
    }

    /**
     *
     *
     * 
     */
    private boolean isTouchBarrier(int x, int y) {
        boolean res = false;
        int pY = mPerson.mPersonY + 2 * radius;
        
        if (Math.abs(pY - y) <= Math.abs(mBarrierMoveSpeed + Person.SPEED + mFallTime / 1000 * G)) {
            if (mPerson.mPersonX + 2 * radius >= x && mPerson.mPersonX <= x + mBarrier.getWidth()) {
                res = true;
            }
        }
        return res;
    }

    boolean pause = false;

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void startGame() {
        if(actionListener != null){
            actionListener.start();
        }
        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (isRunning) {
                    if(pause){
                        continue;
                    }
                    
                    mBarrierStartY -= mBarrierMoveSpeed;
                   
                    if (mBarrierStartY <= -mBarrierInterval - mBarrierHeight) {
                        mBarrierStartY = -mBarrierHeight;
                        
                        if (mBarrierXs.size() > 0) {
                            mBarrierXs.remove(0);
                            mBarriers.remove(0);
                        }
                       
                        mTotalScore++;
                        
                        mTouchIndex--;
                    }
                   
                    myHandler.sendEmptyMessage(0x1);
                    try {
                      
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                invalidate();
            }

        }
    }

   
    public void persongNormal() {
        mPerson.type = 0;
    }

   
    public void moveLeft() {
        if (!isRunning) {
            return;
        }
        mPerson.type = -1;
        int x = mPerson.mPersonX;
        int dir = x - mPersonMoveSpeed;
        if (dir < 0)
            dir = 0;
        mPerson.mPersonX = dir;
       
        checkIsOutSide(dir);
    }

    /**
     *
     */
    public void moveRight() {
        if (!isRunning) {
            return;
        }
        mPerson.type = 1;
        int x = mPerson.mPersonX;
        int dir = x + mPersonMoveSpeed;
        if (dir > mLayoutWidth - radius * 2)
            dir = mLayoutWidth - radius * 2;
        mPerson.mPersonX = dir;
        checkIsOutSide(dir);
    }

    private void checkIsOutSide(int x) {
        if (preSubBarrier != null) {
           
            if (isTouchBarrier(preSubBarrier.mPositionX, preSubBarrier.mPositionY)) {
                isAutoFall = false;
            } else {
                isAutoFall = true;
            }
        }
    }

    public void stop() {
        isRunning = false;
    }

    /**
     * 
     */
    public void restartGame() {
        mBarrierXs.clear();
        mBarrierYs.clear();
        mBarrierStartY = 500;
        mPerson.type = 0;
        mPerson.mPersonY = 300;
        mPerson.mPersonX = mLayoutWidth / 2;
        mPerson.blood = BLOOD_ALL;
        mPerson.resetBitmapWalk();
        mTotalScore = 0;
        isAutoFall = true;
        mFallTime = 0;
        isRunning = true;
        preSubBarrier = null;
        mBarrierMoveSpeed = 5;
        mPreSocre = 0;
        startGame();
    }
}
