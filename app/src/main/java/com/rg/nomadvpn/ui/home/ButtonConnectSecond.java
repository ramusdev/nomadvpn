package com.rg.nomadvpn.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.model.ServerStatusEnum;
import com.rg.nomadvpn.service.NotificationService;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.Date;

import de.blinkt.openvpn.core.OpenVPNService;

public class ButtonConnectSecond {
    private View view;
    private CardView cardConnect;
    private TextView titleConnect;
    private TextView titleDisconnect;
    private ConstraintLayout layoutView;
    private ConstraintLayout constraintMain;
    private ConstraintLayout layoutDisconnect;
    private ConstraintLayout supportLayout;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private int progressValue = 0;
    private ConnectedCallBack connectedCallBack;

    public interface ConnectedCallBack {
        void onConnected();
    }

    public void setConnectedCallBack(ConnectedCallBack connectedCallBack) {
        this.connectedCallBack = connectedCallBack;
    }

    public ButtonConnectSecond(View view) {
        this.view = view;
        init();
    }

    public void init() {
        this.cardConnect = view.findViewById(R.id.button_card);
        this.titleConnect = view.findViewById(R.id.button_title);
        this.progressBar = view.findViewById(R.id.button_progress);

        int animationSmoothness = 1000;
        int maxProgress = 100;
        progressBar.setMax(maxProgress * animationSmoothness);
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        cardConnect.setOnClickListener(clickListener);
    }

    public void clear() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(0);
                titleConnect.setText("Start connection");
            }
        });
    }

    public void animationActionProfile() {
        int colorFrom = MyApplicationContext.getAppContext().getResources().getColor(R.color.profile_background);
        int colorTo = MyApplicationContext.getAppContext().getResources().getColor(R.color.profile_background_animation);
        ValueAnimator animatorClickDown = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animatorClickDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progressBar.setBackgroundColor(value);
            }
        });
        animatorClickDown.setInterpolator(new LinearInterpolator());

        ValueAnimator animatorClickUp = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        animatorClickUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progressBar.setBackgroundColor(value);
            }
        });

        ValueAnimator animatorTitleFadeOut = ValueAnimator.ofFloat(1f, 0.2f);
        animatorTitleFadeOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                titleConnect.setAlpha(alpha);
            }
        });

        ValueAnimator animatorTitleFadeIn = ValueAnimator.ofFloat(0.2f, 1f);
        animatorTitleFadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                titleConnect.setAlpha(alpha);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorClickDown).with(animatorTitleFadeOut);
        animatorSet.play(animatorClickUp).with(animatorTitleFadeIn).after(animatorClickDown);
        animatorSet.setDuration(200);
        animatorSet.start();
    }

    public AnimatorSet buttonAnimationActionDown(String text) {
        int duration = 150;
        int colorFrom = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_from);
        int colorTo = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to);

        ValueAnimator animatorClickDown = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animatorClickDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progressBar.setBackgroundColor(value);
            }
        });
        animatorClickDown.setInterpolator(new LinearInterpolator());

        ValueAnimator animatorClickUp = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        animatorClickUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progressBar.setBackgroundColor(value);
            }
        });

        ValueAnimator animatorTitleFadeOut = ValueAnimator.ofFloat(1f, 0.3f);
        animatorTitleFadeOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                titleConnect.setAlpha(alpha);
            }
        });
        animatorTitleFadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (! text.isEmpty()) {
                    titleConnect.setText(text);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ValueAnimator animatorTitleFadeIn = ValueAnimator.ofFloat(0.3f, 1f);
        animatorTitleFadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                titleConnect.setAlpha(alpha);
            }
        });

        float translateFrom = 0;
        float translateTo = 7f;
        ValueAnimator translateBottom = ValueAnimator.ofFloat(translateFrom, translateTo);
        translateBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                cardConnect.setTranslationY(value);
            }
        });

        float translateFromTop = 0f;
        float translateToTop = -7f;
        ValueAnimator translateTop = ValueAnimator.ofFloat(translateFromTop, translateToTop);
        translateTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                cardConnect.setTranslationY(value);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorClickDown).with(animatorTitleFadeOut).with(translateBottom);
        animatorSet.play(animatorClickUp).with(animatorTitleFadeIn).after(animatorClickDown);
        animatorSet.play(translateTop).after(animatorClickDown);
        animatorSet.setDuration(duration);

        return animatorSet;
    }

    /*
    public AnimatorSet buttonAnimationActionDisconnect(String text) {
        int colorFrom = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_from_disconnect);
        int colorTo = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
        ValueAnimator animatorClickDown = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animatorClickDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layoutDisconnect.setBackgroundColor(value);
            }
        });
        animatorClickDown.setInterpolator(new LinearInterpolator());

        ValueAnimator animatorClickUp = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        animatorClickUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layoutDisconnect.setBackgroundColor(value);
            }
        });

        ValueAnimator animatorTitleFadeOut = ValueAnimator.ofFloat(1f, 0.2f);
        animatorTitleFadeOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                titleDisconnect.setAlpha(alpha);
            }
        });
        animatorTitleFadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                titleDisconnect.setText(text);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ValueAnimator animatorTitleFadeIn = ValueAnimator.ofFloat(0.2f, 1f);
        animatorTitleFadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                titleDisconnect.setAlpha(alpha);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorClickDown).with(animatorTitleFadeOut);
        animatorSet.play(animatorClickUp).with(animatorTitleFadeIn).after(animatorClickDown);
        animatorSet.setDuration(300);

        return animatorSet;
    }

     */

    public void buttonPressAnimation(String text) {
        AnimatorSet buttonAnimationAction = buttonAnimationActionDown(text);
        AnimatorSet animatorSetEnd = new AnimatorSet();
        animatorSetEnd.setStartDelay(2000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(buttonAnimationAction);
        animatorSet.play(animatorSetEnd).after(buttonAnimationAction);
        animatorSet.start();
    }

    public void buttonPressAnimation(String text, ButtonDisconnect.AnimationEndInterface animationCallback) {
        AnimatorSet buttonAnimationAction = buttonAnimationActionDown(text);
        AnimatorSet animatorSetEnd = new AnimatorSet();
        animatorSetEnd.setStartDelay(2000);
        animatorSetEnd.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationCallback.animationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(buttonAnimationAction);
        animatorSet.play(animatorSetEnd).after(buttonAnimationAction);
        animatorSet.start();
    }

    public void updateProgressBar(int breakPoint) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int animationSmoothness = 1000;
                int animationDuration = 1000;

                ValueAnimator animatorTitle = ValueAnimator.ofInt(ButtonConnectSecond.this.progressBar.getProgress(), breakPoint * animationSmoothness);
                animatorTitle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = ((int) animation.getAnimatedValue()) / 1000;
                        titleConnect.setText("Progress  " + value + "%");
                    }
                });
                animatorTitle.setDuration(animationDuration);
                animatorTitle.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator animatorProgress = ObjectAnimator.ofInt(ButtonConnectSecond.this.progressBar, "progress", breakPoint * animationSmoothness);
                animatorProgress.setDuration(animationDuration);
                animatorProgress.setInterpolator(new DecelerateInterpolator());

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animatorProgress).with(animatorTitle);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (breakPoint >= 100) {
                            animationFinishedConnection();
                        }
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
        });
    }

    public AnimatorSet getAnimatorFadeOutInText(String text, TextView textView) {
        ValueAnimator animatorFadeOut = ValueAnimator.ofFloat(1f, 0f);
        animatorFadeOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ButtonConnectSecond.this.titleConnect.setAlpha(alpha);
            }
        });
        animatorFadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(text);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ValueAnimator animatorFadeIn = ValueAnimator.ofFloat(0f, 1f);
        animatorFadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                textView.setAlpha(alpha);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorFadeOut);
        animatorSet.setDuration(300);
        animatorSet.play(animatorFadeIn).after(animatorFadeOut);

        return animatorSet;
    }

    public void animationFinishedConnection() {
        int duration = 500;
        // showSupportMessage(false);
        Animator animatorText = getAnimatorFadeOutInText("Connected", titleConnect);

        AnimatorSet animatorDelay = new AnimatorSet();
        animatorDelay.setStartDelay(2000);
        animatorDelay.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                connectedCallBack.onConnected();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorText);
        animatorSet.play(animatorDelay).after(animatorText);
        animatorSet.setDuration(duration);
        animatorSet.start();
    }


    public void buttonStopConnection() {
        // this.vpnConnectionService.disconnectServer();
        // this.notificationService.showDisconnectMessage();

        // Animation
        Animator animator = buttonAnimationActionDown("");
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator);
        animatorSet.start();

        AnimatorSet animatorSetConnect = new AnimatorSet();
        animatorSetConnect.setStartDelay(3000);
        animatorSetConnect.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // buttonInit();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSetConnect.start();
    }

    public void hideButton() {
        cardConnect.setVisibility(View.GONE);
    }

    public void showButton() {
        cardConnect.setVisibility(View.VISIBLE);
    }
}
