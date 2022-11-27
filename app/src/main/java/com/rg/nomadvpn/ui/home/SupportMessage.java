package com.rg.nomadvpn.ui.home;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.rg.nomadvpn.R;

public class SupportMessage {
    private View view;
    private ConstraintLayout constraintLayout;
    private Handler handler = new Handler();

    public SupportMessage(View view) {
        this.view = view;
        constraintLayout = view.findViewById(R.id.support_layout);
    }

    public void showMessage(boolean isVisible) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int duration = 1000;
                ValueAnimator valueAnimator = new ValueAnimator();
                if (isVisible) {
                    valueAnimator = ValueAnimator.ofFloat(0f, 1f);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float alpha = (float) animation.getAnimatedValue();
                            constraintLayout.setAlpha(alpha);
                        }
                    });
                    valueAnimator.setDuration(duration);
                    valueAnimator.start();
                } else {
                    float alpha = constraintLayout.getAlpha();
                    if (alpha == 1) {
                        valueAnimator = ValueAnimator.ofFloat(1f, 0f);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float alpha = (float) animation.getAnimatedValue();
                                constraintLayout.setAlpha(alpha);
                            }
                        });
                        valueAnimator.setDuration(duration);
                        valueAnimator.start();
                    }
                }
            }
        });
    }
}
