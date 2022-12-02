package com.rg.nomadvpn.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.rg.nomadvpn.R;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class ButtonProfile {
    private View view;
    private CardView card;
    private ConstraintLayout layout;
    private TextView title;

    private Handler handler = new Handler();

    public ButtonProfile(View view) {
        this.view = view;
        init();
    }

    private void init() {
        card = view.findViewById(R.id.card_profile);
        layout = view.findViewById(R.id.layout_profile);
        title = view.findViewById(R.id.title_profile);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        card.setOnClickListener(onClickListener);
    }

    public void showButton() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                card.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideButton() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                card.setVisibility(View.GONE);
            }
        });
    }

    public void clickAnimation() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                int heightIntFrom = (int) (60 * MyApplicationContext.getAppContext().getResources().getDisplayMetrics().density);
                int heightIntTo = (int) (50 * MyApplicationContext.getAppContext().getResources().getDisplayMetrics().density);


                float translateFromTop = 0f;
                float translateToTop = 7f;
                ValueAnimator translateTop = ValueAnimator.ofFloat(translateFromTop, translateToTop);
                translateTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        card.setTranslationY(value);
                    }
                });
                translateTop.start();

                // ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) card.getLayoutParams();
                // layoutParams.height = heightInt;
                // card.setLayoutParams(layoutParams);

                // ConstraintLayout root = view.findViewById(R.id.constraint_main);

                // ConstraintSet constraintSet = new ConstraintSet();
                // constraintSet.clone(MyApplicationContext.getAppContext(), R.layout.fragment_home_2);

                // TransitionManager.beginDelayedTransition(root);
                // constraintSet.applyTo(root);

                // ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) card.getLayoutParams();
                // layoutParams.height = heightIntTo;
                // card.setLayoutParams(layoutParams);

                // constraintSet.applyTo(layoutParams);

                // Drawable drawable = MyApplicationContext.getAppContext().getResources().getDrawable(R.drawable.profile_backgrounddown);
                // layout.setBackground(drawable);
            }
        });

        /*
        int duration = 150;

        // Text
        ValueAnimator animatorTextOut = ValueAnimator.ofFloat(1f, 0.3f);
        animatorTextOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                title.setAlpha(alpha);
            }
        });

        ValueAnimator animatorTextIn = ValueAnimator.ofFloat(0.3f, 1f);
        animatorTextIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                title.setAlpha(alpha);
            }
        });

        // Button
        int colorFrom = MyApplicationContext.getAppContext().getResources().getColor(R.color.profile_background);
        int colorTo = MyApplicationContext.getAppContext().getResources().getColor(R.color.profile_background_animation);
        ValueAnimator animatorButtonOut = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animatorButtonOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layout.setBackgroundColor(value);
            }
        });

        ValueAnimator animatorButtonIn = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        animatorButtonIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layout.setBackgroundColor(value);
            }
        });

        float translateFrom = 0f;
        float translateTo = 7f;
        ValueAnimator translateBottom = ValueAnimator.ofFloat(translateFrom, translateTo);
        translateBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                card.setTranslationY(value);
            }
        });

        float translateFromTop = 0f;
        float translateToTop = -7f;
        ValueAnimator translateTop = ValueAnimator.ofFloat(translateFromTop, translateToTop);
        translateTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                card.setTranslationY(value);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorButtonOut).with(animatorTextOut).with(translateBottom);
        animatorSet.play(animatorButtonIn).after(animatorButtonOut);
        animatorSet.play(animatorTextIn).after(animatorButtonOut);
        animatorSet.play(translateTop).after(animatorButtonOut);
        animatorSet.setDuration(duration);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Drawable drawable = MyApplicationContext.getAppContext().getResources().getDrawable(R.drawable.profile_background);
                layout.setBackground(drawable);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

         */



    }
}
