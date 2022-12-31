package com.rg.nomadvpn.ui.speed;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rg.nomadvpn.R;

public class SpeedView extends View {
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

    public SpeedView(Context context) {
        super(context);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
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

        float degreeInMb = 260 / 60;
        int valueDegree = (int) (this.valueSpeed * degreeInMb) + 18;

        textView.setText(valueString);


        // Draw first circle
        paint.setColor(Color.parseColor("#364049"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setStrokeWidth(80.0f);
        RectF ovalFirst = new RectF();
        ovalFirst.set(cx - radius , cy - radius, cx + radius, cy + radius);
        canvas.drawArc(ovalFirst, 140, 260, false, paint);

        // Draw second circle
        paint.setColor(Color.parseColor("#B3364049"));
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

        double radiusText = radius - 10;
        double angleText = (valueDegree + 127) % 360;
        double angle = Math.toRadians(angleText);
        double angleTextDraw = (valueDegree + 220) % 360;
        float coordX = (float) (radiusText * Math.cos(angle));
        float coordY = (float) (radiusText * Math.sin(angle));

        paint.setColor(Color.parseColor("#0a5145"));
        paint.setTextSize(34);
        paint.setStyle(Paint.Style.FILL);
        canvas.rotate((float) angleTextDraw, coordX, coordY);
        canvas.drawText(valueString, coordX,  coordY, paint);

        canvas.restore();
        // invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                valueAnimatorStartDownload();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    /*
    public void setValue(int valueDownload) {
        this.valueDownload = valueDownload;
        invalidate();
    }
    */

    /*
    public void setValue(float valueSpeed) {
        this.valueSpeed = valueSpeed;
        invalidate();
    }
    */

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
        // float degreeInMb = 260 / 60;
        // int degreeTo = (int) (speedTo * degreeInMb) + 20;

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


        /*
        int degreeCurrent = this.valueDownload;
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "value", degreeCurrent, degreeTo);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

        TextView speedView = view.findViewById(R.id.download_speed);
        String speedString = speedView.getText().toString();
        float speedFloat = Float.parseFloat(speedString);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(speedFloat, speedToFloat);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                double valueDouble = (double) Math.round(value * 10) / 10;
                speedView.setText(String.valueOf(valueDouble));
            }
        });
        valueAnimator.start();
        */




    }

    public void valueAnimatorStartDownload() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "value", 20, 260);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
