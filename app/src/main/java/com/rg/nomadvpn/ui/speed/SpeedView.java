package com.rg.nomadvpn.ui.speed;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class SpeedView extends View {

    int x = 0;
    float radius = 0;
    float cx = 0;
    float cy = 0;
    int value = 0;
    int valueUpload = 0;
    private Canvas canvas;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int colorUpload;

    public SpeedView(Context context) {
        super(context);
        this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.colorUpload = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        this.canvas = canvas;
        // canvas.scale(getWidth(), getHeight());
        canvas.translate(getPivotX(), getPivotY() / 2);

        // paint.setColor(0x40000000);
        // paint.setStyle(Paint.Style.FILL);
        // canvas.drawCircle(0, 0, 1, paint);

        float width = getWidth();
        float height = getHeight();
        cx = 0;
        cy = 0;

        if (width > height) {
            radius = height / 4;
        } else {
            radius = width / 4;
        }

        /*
        paint.setColor(Color.parseColor("#352457"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20.0f);
        canvas.drawCircle(cx, cy, radius, paint);
        */

        // Draw first circle
        paint.setColor(Color.parseColor("#352457"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20.0f);
        RectF ovalFirst = new RectF();
        ovalFirst.set(cx - radius , cy - radius, cx + radius, cy + radius);
        canvas.drawArc(ovalFirst, 140, 260, false, paint);

        // Draw second circle
        paint.setColor(Color.parseColor("#221638"));
        float radiusSecond = radius - 20.0f;
        RectF ovalSecond = new RectF();
        ovalSecond.set(cx - radiusSecond , cy - radiusSecond, cx + radiusSecond, cy + radiusSecond);
        canvas.drawArc(ovalSecond, 140, 260, false, paint);

        // Draw third
        paint.setColor(Color.parseColor("#352457"));
        float radiusThird = radius - 40.0f;
        RectF ovalThird = new RectF();
        ovalThird.set(cx - radiusThird , cy - radiusThird, cx + radiusThird, cy + radiusThird);
        canvas.drawArc(ovalThird, 140, 260, false, paint);

        // paint.setColor(Color.parseColor("#004f53"));
        // paint.setStyle(Paint.Style.FILL);
        // canvas.drawCircle(cx - radius, cy, radius - 250.0f, paint);

        RectF oval = new RectF();
        oval.set(cx - radius , cy - radius, cx + radius, cy + radius);
        paint.setColor(Color.parseColor("#004f53"));
        paint.setStrokeWidth(20);
        canvas.drawArc(oval, 140, this.value, false, paint);

        RectF ovalUpload = new RectF();
        ovalUpload.set(cx - radiusThird , cy - radiusThird, cx + radiusThird, cy + radiusThird);
        paint.setColor(colorUpload);
        paint.setStrokeWidth(20);
        canvas.drawArc(ovalUpload, 140, this.valueUpload, false, paint);

        int x = 0;
        int y = 0;

        double radiusText = radius + 45;
        double angleText = (this.value + 130) % 360;
        double angleTextDraw = (this.value + 228) % 360;
        double angle = Math.toRadians(angleText);
        float coordX = (float) (radiusText * Math.cos(angle));
        float coordY = (float) (radiusText * Math.sin(angle));

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setTextSize(30);
        paint.setStrokeWidth(1);
        canvas.rotate((float) angleTextDraw, coordX, coordY);
        canvas.drawText(String.valueOf(angleText), coordX,  coordY, paint);

        Log.d(MainActivity.LOGTAG, "Coord --------------------------------------->");
        Log.d(MainActivity.LOGTAG,"CoordX: " + coordX);
        Log.d(MainActivity.LOGTAG,"CoordY: " + coordY);

        canvas.restore();
        // invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                valueAnimatorStartDownload();
                valueAnimatorStartUpload();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }


    public void setValue(int value) {
        this.value = value;
        invalidate();
    }

    public void setValue2(int valueUpload) {
        this.valueUpload = valueUpload;
        invalidate();
    }

    public void valueAnimatorStartDownload() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "value", 0, 260);
        objectAnimator.setDuration(10000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();


        /*
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0.0f, 360.0f);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RectF oval = new RectF();
                oval.set(cx - radius , cy - radius, cx + radius, cy + radius);
                paint.setColor(Color.parseColor("#004f53"));
                paint.setStrokeWidth(20);

                float value = (float) animation.getAnimatedValue();
                canvas.drawArc(oval, 0, value, false, paint);
                canvas.restore();
            }
        });
        valueAnimator.start();

         */
    }

    public void valueAnimatorStartUpload() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "value2", 0, 260);
        objectAnimator.setDuration(10000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
