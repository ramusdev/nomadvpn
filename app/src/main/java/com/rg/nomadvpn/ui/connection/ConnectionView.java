package com.rg.nomadvpn.ui.connection;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rg.nomadvpn.R;

import java.util.logging.ConsoleHandler;

public class ConnectionView extends View {
    float cx = 0;
    float cy = 0;
    int valueDownload = 20;
    int valueUpload = 20;
    float radius = 350f;
    private Canvas canvas;
    private Paint paint = new Paint();
    private float valueSpeed = 0.0f;
    private View viewMain;
    private TextView textView;
    private Handler handler = new Handler();
    // private OnAnimationEnd onAnimationEnd;

    public ConnectionView(Context context) {
        super(context);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public interface OnAnimationEnd {
        void onAnimationEndAction();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        this.canvas = canvas;
        radius = 350f;
        // canvas.scale(getWidth(), getHeight());
        canvas.translate(getPivotX(), getHeight() / 2);

        double valueDouble = (double) Math.round(this.valueSpeed * 10) / 10;
        String valueString = String.valueOf(valueDouble);
        float factor = 965.714f;
        int valueDegree = (int) Math.sqrt(this.valueSpeed * factor);

        textView.setText(valueString);

        // Draw first circle
        paint.setColor(Color.parseColor("#303F50"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setStrokeWidth(80.0f);
        RectF ovalFirst = new RectF();
        ovalFirst.set(cx - radius , cy - radius, cx + radius, cy + radius);
        canvas.drawArc(ovalFirst, 140, 260, false, paint);

        // Draw second circle
        paint.setColor(Color.parseColor("#B3303F50"));
        paint.setStrokeWidth(50.0f);
        paint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL));
        float radiusSecond = radius - 40.0f;
        RectF ovalSecond = new RectF();
        ovalSecond.set(cx - radiusSecond , cy - radiusSecond, cx + radiusSecond, cy + radiusSecond);
        canvas.drawArc(ovalSecond, 140, 260, false, paint);
        paint.setMaskFilter(null);

        // Draw helper circle
        // paint.setColor(Color.parseColor("#ffffff"));
        // paint.setStrokeWidth(50.0f);
        // float radiusHelper = radius - 75.0f;
        // RectF ovalHelper = new RectF();
        // ovalHelper.set(cx - radiusHelper , cy - radiusHelper, cx + radiusHelper, cy + radiusHelper);
        // canvas.drawArc(ovalHelper, 140, 260, false, paint);

        // Draw speed download
        paint.setColor(Color.parseColor("#0fd6b5"));
        paint.setStrokeWidth(80);
        RectF ovalSpeedOne = new RectF();
        ovalSpeedOne.set(cx - radius , cy - radius, cx + radius, cy + radius);
        canvas.drawArc(ovalSpeedOne, 140, valueDegree, false, paint);

        // Draw speed second download
        paint.setColor(Color.parseColor("#4D0fd6b5"));
        paint.setStrokeWidth(50.0f);
        paint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL));
        float radiusSpeedTwo = radius - 40.0f;
        RectF ovalSpeedTwo = new RectF();
        ovalSpeedTwo.set(cx - radiusSpeedTwo , cy - radiusSpeedTwo, cx + radiusSpeedTwo, cy + radiusSpeedTwo);
        canvas.drawArc(ovalSpeedTwo, 140, valueDegree, false, paint);
        paint.setMaskFilter(null);

        double degreeText = 146;
        double rotationText = 238;
        paint.setColor(Color.parseColor("#ffffff"));

        if (valueDegree >= 20) {
            rotationText += valueDegree - 16;
            degreeText += valueDegree - 20;
        }

        if (valueDegree >= 14) {
            paint.setColor(Color.parseColor("#0a5145"));
        }

        double radiusText = radius - 12;
        double angle = Math.toRadians(degreeText);

        float coordX = (float) (radiusText * Math.cos(angle));
        float coordY = (float) (radiusText * Math.sin(angle));

        paint.setTextSize(34);
        paint.setStyle(Paint.Style.FILL);
        canvas.rotate((float) rotationText, coordX, coordY);
        canvas.drawText(valueString, coordX,  coordY, paint);

        canvas.restore();
        // invalidate();
    }

    public void setView(View viewMain) {
        this.viewMain = viewMain;
    }

    public void init() {
        textView = this.viewMain.findViewById(R.id.download_speed);
    }

    public void updateFrame(float valueSpeed) {
        this.valueSpeed = valueSpeed;
        invalidate();
    }

    public void downloadAnimation(String speedTo) {
        float speedToFloat = Float.parseFloat(speedTo);
        float speedCurrent = this.valueSpeed;

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(speedCurrent, speedToFloat);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                updateFrame(value);
            }
        });
        valueAnimator.start();
    }

    public void connectedAnimation() {
        float speedFrom = 0.0f;
        float speedTo = 70.0f;

        handler.post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animatorSpeedToMax = ObjectAnimator.ofFloat(speedFrom, speedTo);
                animatorSpeedToMax.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        updateFrame(value);
                    }
                });
                // animatorSpeedToMax.start();

                ValueAnimator animatorSpeedToMin = ObjectAnimator.ofFloat(speedTo, speedFrom);
                animatorSpeedToMin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        updateFrame(value);
                    }
                });

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(2000);
                animatorSet.play(animatorSpeedToMax);
                animatorSet.play(animatorSpeedToMin).after(animatorSpeedToMax);
                animatorSet.start();
            }
        });
    }

    public void clearAnimation() {
        float speedFrom = this.valueSpeed;
        float speedTo = 0.0f;

        handler.post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animatorSpeedToMin = ObjectAnimator.ofFloat(speedFrom, speedTo);
                animatorSpeedToMin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        updateFrame(value);
                    }
                });

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(2000);
                animatorSet.play(animatorSpeedToMin);
                animatorSet.start();
            }
        });
    }
}
