package com.rg.nomadvpn.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.Sampler;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.model.ServerStatusEnum;
import com.rg.nomadvpn.service.NotificationService;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

import de.blinkt.openvpn.core.OpenVPNService;

public class ButtonConnect {
    private View view;
    private CardView cardConnect;
    private CardView cardDisconnect;
    private TextView titleConnect;
    private TextView titleDisconnect;
    private ConstraintLayout layoutView;
    private ConstraintLayout constraintMain;
    private ConstraintLayout layoutDisconnect;
    private ConstraintLayout supportLayout;
    private Animation animationFadeIn;
    private Animation animationProgress;
    private ProgressBar progressBar;
    private VpnConnectionService vpnConnectionService;
    private NotificationService notificationService;
    // private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private int cardConnectWidth;
    private int buttonLayoutWidth;
    private Handler handler = new Handler();
    private int progressValue = 0;

    public ButtonConnect() {

    }

    public void setView(View view) {
        this.view = view;
    }

    public void setService(VpnConnectionService vpnConnectionService) {
        this.vpnConnectionService = vpnConnectionService;
    }

    public void setNotification(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void init() {
        initComponents();
        buttonInit();
    }

    public void initComponents() {
        this.cardConnect = view.findViewById(R.id.button_card);
        this.titleConnect = view.findViewById(R.id.button_title);
        this.layoutView = view.findViewById(R.id.button_layout);
        this.progressBar = view.findViewById(R.id.button_progress);
        this.cardDisconnect = view.findViewById(R.id.card_disconnect);
        this.constraintMain = view.findViewById(R.id.constraint_main);
        this.titleDisconnect = view.findViewById(R.id.title_disconnect);
        this.layoutDisconnect = view.findViewById(R.id.layout_disconnect);
        this.supportLayout = view.findViewById(R.id.support_layout);
    }

    public void buttonInit() {
        if (vpnConnectionService.isVpnProfileInstalled()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    cardDisconnect.setVisibility(View.GONE);
                    progressBar.setProgress(0);
                    cardConnect.setVisibility(View.VISIBLE);
                    titleConnect.setText("Start connection");
                    titleConnect.setTextColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.connection_text));
                    progressBar.setBackgroundColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.connection_background));
                    // supportLayout.setVisibility(View.GONE);
                    showSupportMessage(false);
                }
            });
            cardConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonStart();
                }
            });
            cardDisconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonDisconnect();
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    titleConnect.setText("Add vpn profile");
                    titleConnect.setTextColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.profile_text));
                    progressBar.setBackgroundColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.profile_background));
                }
            });
            cardConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animationActionProfile();
                    vpnConnectionService.vpnProfileInstall(new VpnConnectionService.Callback() {
                        @Override
                        public void callingBack() {
                            buttonInit();
                        }
                    });
                }
            });
        }
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
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public AnimatorSet buttonAnimationActionDown(String text) {
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

        ValueAnimator animatorTitleFadeOut = ValueAnimator.ofFloat(1f, 0.2f);
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
        animatorSet.setDuration(300);

        return animatorSet;
    }

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


    public void buttonStart() {
        this.buttonStartAnimation();
        this.vpnConnectionService.startVpnService();

        cardConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStopConnection();
            }
        });
    }

    public void buttonStartAnimation() {
        AnimatorSet buttonAnimationAction = buttonAnimationActionDown("Progress: 0%");

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(buttonAnimationAction);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                buttonStartAnimationProgress();
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

    public void buttonStartAnimationProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int breakPoint = 0;
                int animationSmoothness = 1000;
                ButtonConnect.this.progressBar.setMax(100 * animationSmoothness);
                long currentDateTime = new Date().getTime();
                long waitSeconds = 7;
                long waitMilliseconds = waitSeconds * 1000;
                boolean isShowed = false;

                while (true) {
                    int breakPointCurrent = ButtonConnect.this.getBreakPoint();
                    if (breakPointCurrent != breakPoint) {
                        breakPoint = breakPointCurrent;
                        updateProgressBar(breakPoint);
                    }

                    long dateTime = new Date().getTime();
                    long dateDifference = dateTime - currentDateTime;
                    if (dateDifference > waitMilliseconds) {
                        if (! isShowed) {
                            showSupportMessage(true);
                            isShowed = true;
                        }
                    }

                    String status = vpnConnectionService.getStatus();
                    if (status.equals("Connected") || status.equals("Disconnected")) {
                        notificationService.showConnectMessage();
                        break;
                    }

                    try {
                        Thread.sleep(1400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void updateProgressBar(int breakPoint) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int animationSmoothness = 1000;
                int animationDuration = 1000;

                ValueAnimator animatorTitle = ValueAnimator.ofInt(ButtonConnect.this.progressBar.getProgress(), breakPoint * animationSmoothness);
                animatorTitle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = ((int) animation.getAnimatedValue()) / 1000;
                        titleConnect.setText("Progress  " + value + "%");
                    }
                });
                animatorTitle.setDuration(animationDuration);
                animatorTitle.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator animatorProgress = ObjectAnimator.ofInt(ButtonConnect.this.progressBar, "progress", breakPoint * animationSmoothness);
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
                            animationFinishedConnectTwo();
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

    public void showSupportMessage(boolean isVisible) {
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
                            supportLayout.setAlpha(alpha);
                        }
                    });
                    valueAnimator.setDuration(duration);
                    valueAnimator.start();
                } else {
                    float alpha = supportLayout.getAlpha();
                    if (alpha == 1) {
                        valueAnimator = ValueAnimator.ofFloat(1f, 0f);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float alpha = (float) animation.getAnimatedValue();
                                supportLayout.setAlpha(alpha);
                            }
                        });
                        valueAnimator.setDuration(duration);
                        valueAnimator.start();
                    }
                }
            }
        });
    }

    public int getBreakPoint() {
        int breakPoint = 10;
        String status = OpenVPNService.getStatus();

        Log.d(MainActivity.LOGTAG, "Break point status: " + status);

        if (status.isEmpty()) {
            return breakPoint;
        }

        ServerStatusEnum serverStatusEnum = ServerStatusEnum.valueOf(status);

        switch (serverStatusEnum) {
            case CONNECTRETRY:
                // TODO Connect retry
                return breakPoint = 0;
            case DISCONNECTED:
                return breakPoint = 0;
                // TODO Disconnected
            case NONETWORK:
                // TODO Error no network
                return breakPoint = 0;
            case NOPROCESS:
                return breakPoint = 0;
            case VPN_GENERATE_CONFIG:
                return breakPoint = 10;
            case WAIT:
                return breakPoint = 30;
            case AUTH:
                return breakPoint = 50;
            case GET_CONFIG:
                return breakPoint = 60;
            case ASSIGN_IP:
                return breakPoint = 70;
            case ADD_ROUTES:
                return breakPoint = 80;
            case CONNECTED:
                return breakPoint = 100;
        }

        return breakPoint;
    }

    public AnimatorSet getAnimatorFadeOutInText(String text, TextView textView) {
        ValueAnimator animatorFadeOut = ValueAnimator.ofFloat(1f, 0f);
        animatorFadeOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ButtonConnect.this.titleConnect.setAlpha(alpha);
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


    public void animationFinishedConnectTwo() {
        int duration = 500;
        showSupportMessage(false);
        Animator animatorText = getAnimatorFadeOutInText("Connected", titleConnect);

        AnimatorSet animatorDelay = new AnimatorSet();
        animatorDelay.setStartDelay(2000);
        animatorDelay.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardConnect.setVisibility(View.GONE);
                cardDisconnect.setVisibility(View.VISIBLE);
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

    public void buttonDisconnect() {
        this.buttonDisconnectAnimation();

        this.vpnConnectionService.disconnectServer();
        this.notificationService.showDisconnectMessage();
    }

    public void buttonStopConnection() {
        this.vpnConnectionService.disconnectServer();
        this.notificationService.showDisconnectMessage();

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
                buttonInit();
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

    public void buttonDisconnectAnimation() {
        int duration = 500;

        // Text
        ValueAnimator animatorTextOut = ValueAnimator.ofFloat(1f, 0.2f);
        animatorTextOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ButtonConnect.this.titleDisconnect.setAlpha(alpha);
            }
        });

        ValueAnimator animatorTextIn = ValueAnimator.ofFloat(0.2f, 1f);
        animatorTextIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ButtonConnect.this.titleDisconnect.setAlpha(alpha);
            }
        });

        // Button
        int colorFrom = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_from_disconnect);
        int colorTo = MyApplicationContext.getAppContext().getResources().getColor(R.color.background_to_disconnect);
        ValueAnimator animatorButtonOut = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animatorButtonOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layoutDisconnect.setBackgroundColor(value);
            }
        });

        ValueAnimator animatorButtonIn = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        animatorButtonIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                layoutDisconnect.setBackgroundColor(value);
            }
        });





        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorButtonOut).with(animatorTextOut);
        animatorSet.play(animatorButtonIn).after(animatorButtonOut);
        animatorSet.play(animatorTextIn).after(animatorButtonOut);
        animatorSet.setDuration(duration);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                buttonInit();
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
}
