package com.rg.nomadvpn.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rg.nomadvpn.R;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class ButtonDisconnect {
    private View view;
    private CardView card;
    private ConstraintLayout layout;
    private TextView title;

    private Handler handler = new Handler();

    public interface AnimationEndInterface {
        void animationEnd();
    }

    public ButtonDisconnect(View view) {
        this.view = view;
        init();
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        card.setOnTouchListener(onTouchListener);
    }

    private void init() {
        card = view.findViewById(R.id.card_disconnect);
        layout = view.findViewById(R.id.layout_disconnect);
        title = view.findViewById(R.id.title_disconnect);
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

    public void clickAnimationUp(AnimationEndInterface animationEndInterface) {
        int heightFrom = (int) (60 * MyApplicationContext.getAppContext().getResources().getDisplayMetrics().density);
        int heightTo = (int) (54 * MyApplicationContext.getAppContext().getResources().getDisplayMetrics().density);
        ValueAnimator translateHeightUp = ValueAnimator.ofInt(heightTo, heightFrom);
        translateHeightUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                layoutParams.height = value;
                card.setLayoutParams(layoutParams);

            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translateHeightUp);
        animatorSet.setDuration(200);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationEndInterface.animationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public void clickAnimationDown() {
        int heightFrom = (int) (60 * MyApplicationContext.getAppContext().getResources().getDisplayMetrics().density);
        int heightTo = (int) (54 * MyApplicationContext.getAppContext().getResources().getDisplayMetrics().density);
        ValueAnimator translateHeightDown = ValueAnimator.ofInt(heightFrom, heightTo);
        translateHeightDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = card.getLayoutParams();
                layoutParams.height = value;
                card.setLayoutParams(layoutParams);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translateHeightDown);
        animatorSet.setDuration(200);
        animatorSet.start();
    }
}
