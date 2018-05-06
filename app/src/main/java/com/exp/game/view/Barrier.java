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
 * 辅助绘制障碍物的类
 */
public class Barrier {
    //绘制的位置纵坐标
    public int mPositionY;
    public int mPositionX;
    //障碍物的宽度
    private int mWidth;
    //障碍物的高度
    private int mHeight;
    //屏幕的宽度
    private int mScreenWidth;
    //已经消失
    private boolean hidden = false;

    private Paint mPaint;
    //当前的障碍物类型
    private int type; // 0 普通 1 刺

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
     * 绘制一个黑色矩形
     * @param canvas
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
