package com.toulan.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.toulan91.R;

/**
 * 说明：圆圈倒计时
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/18 18:04
 * <p/>
 * 版本：verson 1.0
 */
public class CircleProgressView extends View {

    public final static int DEF_PROGRESS_STROKE_WIDTH = 8;
    private int backgroundColor = Color.parseColor("#E5E8E8");

    private float progress = 0;
    private float strokeWidth = DEF_PROGRESS_STROKE_WIDTH;
    private float backgroundStrokeWidth = DEF_PROGRESS_STROKE_WIDTH;

    private int startAngle = -90;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private float centerX,centerY;
    private int[] colors = new int[]{getResources().getColor(R.color.c_progress1),
            getResources().getColor(R.color.c_progress2)
    };

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        rectF = new RectF();
        // Init Background
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);

        // Init Foreground
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / 100;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        int shaderColor0 = colors[0];
        int shaderColor1 = colors[1];

        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, shaderColor0, shaderColor1);

        Matrix matrix = new Matrix();
        matrix.setRotate(startAngle,centerY,centerY);
        sweepGradient.setLocalMatrix(matrix);
        foregroundPaint.setShader(sweepGradient);
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (strokeWidth > backgroundStrokeWidth) ? strokeWidth : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
        invalidate();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }

    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, 1500);
    }

    public void setProgressWithAnimation(float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
