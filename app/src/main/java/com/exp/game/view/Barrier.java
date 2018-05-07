package com.exp.game.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;

import com.exp.game.R;

/**
 * 
 */
public class Barrier {
    public int mPositionY;
    public int mPositionX;
    private int mWidth;
    private int mHeight;
    private int mScreenWidth;
    private boolean hidden = false;

    private Paint mPaint;

    private int type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Barrier barrier = (Barrier) o;

        return mPositionX == barrier.mPositionX;

    }

    @Override
    public int hashCode() {
        return mPositionX;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hidden = true;
            if(back !=null){
                back.callback(Barrier.this);
            }
        }
    };


    public Barrier(Context context, int screenWidth, Paint paint) {
        this.mScreenWidth = screenWidth;
        this.mPaint = paint;
        this.mWidth = this.mScreenWidth/3;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 
     * 
     */
    public void drawBarrier(Resources resources, Canvas canvas) {
        if(hidden){
            System.out.println("Barrier.drawBarrier");
            if(back != null){
                back.callback(this);
            }
            return;
        }
        canvas.save();
        int resId = R.drawable.footboard_normal;
        if(type == 1){
            resId = R.drawable.footboard_spiked;
        }
        BitmapDrawable bitmap = (BitmapDrawable) resources.getDrawable(resId);
        RectF rectF = new RectF(mPositionX, mPositionY, mWidth + mPositionX, mPositionY + mHeight);
        canvas.drawBitmap(bitmap.getBitmap(), null, rectF,mPaint);
        canvas.restore();
    }

    public void startTimerBack(){
        handler.sendEmptyMessageDelayed(0,1000L);
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getWidth(){
        return this.mWidth;
    }


    interface Back{
        void callback(Barrier b);
    }

    Back back;

    public void setBack(Back back) {
        this.back = back;
    }
}
