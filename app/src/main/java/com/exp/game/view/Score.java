package com.exp.game.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * //绘制得分面板
 */
public class Score {

    private Paint mPaint;
    public int panelWidth = 300;
    public int panelHeight = 120;
    public int x = 0;
    public int y = 160;

    public Score(Paint paint) {
        this.mPaint = paint;
    }

    //绘制面板背景
    public void drawPanel(Canvas canvas) {
        canvas.save();
        canvas.drawRoundRect(new RectF(x, y, x + panelWidth, y + panelHeight), 0, 0, mPaint);
        Shader mShader = new LinearGradient(x, y + panelHeight, x, y + panelHeight + 8, new int[]{Color.parseColor("#9e666666"), Color.parseColor("#6e666666"), Color.parseColor("#1edddddd")}, null, Shader.TileMode.REPEAT);
        mPaint.setShader(mShader);
        canvas.drawRect(new RectF(x, y + panelHeight, x + panelWidth, y + panelHeight + 8), mPaint);
        mPaint.setShader(null);
        canvas.restore();
    }
    //绘制分数
    public void drawScore(Canvas canvas, String text) {
        canvas.save();
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算文字
        float textBaseY = panelHeight / 2 + fontHeight / 2 - fontMetrics.bottom;
        canvas.drawText(text, 130, y + textBaseY, mPaint);
        canvas.restore();
    }
}
