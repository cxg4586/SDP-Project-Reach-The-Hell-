package com.exp.game.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.exp.game.R;

/**
 * 
 */
public class Person {


    public static final int SPEED = 10;
    private int mHeaderRadius;
    private Paint mPaint;

    public int mPersonY;
    public int mPersonX;
    public int blood = SurfaceViewLayout.BLOOD_ALL;
    private Bitmap bitmapStand;
    private Bitmap bitmapLeft_1;
    private Bitmap bitmapLeft_2;
    private Bitmap bitmapLeft_3;
    private Bitmap bitmapLeft_4;
    private Bitmap bitmapRight_1;
    private Bitmap bitmapRight_2;
    private Bitmap bitmapRight_3;
    private Bitmap bitmapRight_4;
    private Bitmap[] bitmapRight = new Bitmap[]{null,null,null,null};
    private Bitmap[] bitmapLeft = new Bitmap[]{null,null,null,null};
    public int type = 0; // -1 left 0 stand 1 right
    public int[] walk = new int[]{0,0,0};


    public Person(Context context, Paint paint, int radius) {
        this.mPaint = paint;
        this.mHeaderRadius = radius;
        this.bitmapStand = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_standing);;
        this.bitmapLeft_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_left1);
        this.bitmapLeft_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_left2);
        this.bitmapLeft_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_left3);
        this.bitmapLeft_4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_left4);
        this.bitmapRight_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_right1);
        this.bitmapRight_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_right2);
        this.bitmapRight_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_right3);
        this.bitmapRight_4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.role_moving_right4);
        this.bitmapRight[0] = bitmapRight_1;
        this.bitmapRight[1] = bitmapRight_2;
        this.bitmapRight[2] = bitmapRight_3;
        this.bitmapRight[3] = bitmapRight_4;
        this.bitmapLeft[0] = bitmapLeft_1;
        this.bitmapLeft[1] = bitmapLeft_2;
        this.bitmapLeft[2] = bitmapLeft_3;
        this.bitmapLeft[3] = bitmapLeft_4;
    }

    /**
     * 
     */
    public void draw(Canvas canvas) {

        Bitmap bitmap = null;

        if(type == 0){
            bitmap = this.bitmapStand;
        }else if(type == -1){
            bitmap = this.bitmapLeft[walk[0]%4];
            walk[0] = walk[0] + 1;
        }else if(type == 1){
            bitmap = this.bitmapRight[walk[2]%4];
            walk[2] = walk[2] + 1;
        }

        RectF rectF = new RectF(mPersonX + mHeaderRadius * 0.1578f, mPersonY, mPersonX + 2 * mHeaderRadius- mHeaderRadius * 0.1578f,
                mPersonY + mHeaderRadius * 2);

        canvas.save();
        Path path = new Path();
        path.addCircle(mPersonX + mHeaderRadius, mPersonY + mHeaderRadius, mHeaderRadius, Path.Direction.CCW);

        canvas.clipPath(path);

        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, null,rectF, mPaint);

        canvas.restore();
    }

    public void resetBitmapWalk(){
        walk[0] = 0;
        walk[1] = 0;
        walk[2] = 0;
    }

}
