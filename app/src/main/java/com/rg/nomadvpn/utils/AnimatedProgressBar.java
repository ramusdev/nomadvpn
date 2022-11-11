package com.rg.nomadvpn.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import com.rg.nomadvpn.ui.home.ButtonConnect;

public class AnimatedProgressBar extends ProgressBar {
    private static final int ANIMATION_SMOOTHNESS = 50;
    private static final int ANIMATION_DURATION = 1000;

    public AnimatedProgressBar(Context context) {
        super(context);
        init();
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {

    }

    public void makeProgress(int progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "progress", progress * ANIMATION_SMOOTHNESS);
        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
