package com.rg.nomadvpn.ui.speed;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SpeedView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SpeedView(Context context) {
        super(context);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        // canvas.scale(getWidth(), getHeight());
        canvas.translate(getPivotX(), getPivotY() / 2);

        // paint.setColor(0x40000000);
        // paint.setStyle(Paint.Style.FILL);
        // canvas.drawCircle(0, 0, 1, paint);

        float width = getWidth();
        float height = getHeight();
        float cx = 0;
        float cy = 0;

        float radius;
        if (width > height) {
            radius = height / 4;
        } else {
            radius = width / 4;
        }

        // Paint paint = new Paint();
        paint.setColor(Color.parseColor("#352457"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20.0f);
        canvas.drawCircle(cx, cy, radius, paint);

        paint.setColor(Color.parseColor("#221638"));
        paint.setStrokeWidth(40.0f);
        float radiusSecondCircle = radius - 30.0f;
        canvas.drawCircle(cx, cy, radiusSecondCircle, paint);

        paint.setColor(Color.parseColor("#004f53"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx - radius, cy, radius - 250.0f, paint);

        canvas.restore();
    }
}
