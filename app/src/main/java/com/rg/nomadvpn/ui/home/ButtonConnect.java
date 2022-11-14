package com.rg.nomadvpn.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Handler;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.model.ServerStatusEnum;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

import de.blinkt.openvpn.core.OpenVPNService;

public class ButtonConnect {
    private View rootView;
    private CardView cardConnect;
    private CardView cardDisconnect;
    private TextView titleConnect;
    private TextView titleDisconnect;
    private ConstraintLayout layoutView;
    private ConstraintLayout constraintMain;
    private Animation animationFadeIn;
    private Animation animationProgress;
    private ProgressBar progressBar;
    private VpnConnectionService vpnConnectionService;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(2);
    private int cardConnectWidth;
    private int buttonLayoutWidth;
    private Handler handler = new Handler();
    private int progressValue = 0;

    public ButtonConnect(View rootView) {
        this.rootView = rootView;
        this.cardConnect = rootView.findViewById(R.id.button_card);
        this.titleConnect = rootView.findViewById(R.id.button_title);
        this.layoutView = rootView.findViewById(R.id.button_layout);
        this.progressBar = rootView.findViewById(R.id.button_progress);
        this.cardDisconnect = rootView.findViewById(R.id.card_disconnect);
        this.constraintMain = rootView.findViewById(R.id.constraint_main);
        this.titleDisconnect = rootView.findViewById(R.id.title_disconnect);

        this.cardConnectWidth = this.cardConnect.getMeasuredWidth();
        this.buttonLayoutWidth = this.layoutView.getMeasuredWidth();

        atomicIntegerArray.set(0, 0);
        atomicIntegerArray.set(1, 0);

        animationFadeIn = AnimationUtils.loadAnimation(MyApplicationContext.getAppContext(), R.anim.fade_in);
        clickInit();
    }

    public void setOnClickListener(VpnConnectionService vpnConnectionService) {
        this.vpnConnectionService = vpnConnectionService;
    }

    public void clickInit() {
        cardConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart();
            }
        });

        cardDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDisconnect();
            }
        });
    }

    public void buttonStart() {
        // Start server
        vpnConnectionService.onClick();

        // Ui change
        buttonStartAnimation();
    }

    public void buttonStartAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int breakPoint = 0;
                int animationSmoothness = 1000;

                ButtonConnect.this.progressBar.setMax(100 * animationSmoothness);

                while (true) {
                    int breakPointCurrent = ButtonConnect.this.getBreakPoint();
                    if (breakPointCurrent != breakPoint) {
                        breakPoint = breakPointCurrent;
                        atomicIntegerArray.set(1, breakPointCurrent);
                        updateProgressBar(breakPoint);
                    }

                    if (breakPointCurrent >= 100) {
                        // progressFinished(progressValue);
                        break;
                    }

                    try {
                        Thread.sleep(2000);
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
                int animationDuration = 1800;

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
                // animatorProgress.addListener(progressAnimationListener());




                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animatorProgress).with(animatorTitle);
                // animatorSet.play(getAnimatorFadeOutInText("Connected", titleConnect)).after(animatorProgress);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        /*
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                int from = atomicIntegerArray.get(0);
                                int to = atomicIntegerArray.get(1);
                                atomicIntegerArray.set(0, to);
                                for (int i = from; i <= to; ++i) {
                                    int finalNum = i;
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ButtonConnect.this.titleConnect.setText("Progress  " + finalNum + "%");
                                        }
                                    });

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });
                        */
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (breakPoint >= 100) {
                            animationFinishedConnect();
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

    public int getBreakPoint() {
        int breakPoint = 10;
        String status = OpenVPNService.getStatus();

        Log.d(MainActivity.LOGTAG, "Break point status: " + status);

        if (status.isEmpty()) {
            return breakPoint;
        }

        ServerStatusEnum serverStatusEnum = ServerStatusEnum.valueOf(status);

        /*
        if (status.equals("VPN_GENERATE_CONFIG")) {
            return breakPoint = 15;
        } else if (status.equals("WAIT")) {
            return breakPoint = 30;
        } else if (status.equals("AUTH")) {
            return breakPoint = 45;
        } else if (status.equals("GET_CONFIG")) {
            return breakPoint = 60;
        } else if (status.equals("ASSIGN_IP")) {
            return breakPoint = 75;
        } else if (status.equals("ADD_ROUTES")) {
            return breakPoint = 85;
        } else if (status.equals("CONNECTED")) {
            return breakPoint = 100;
        }
        */

        switch (serverStatusEnum) {
            case CONNECTRETRY:
                // TODO Connect retry
                return breakPoint = 10;
            case DISCONNECTED:
                return breakPoint = 10;
                // TODO Disconnected
            case NONETWORK:
                // TODO Error no network
                return breakPoint = 10;
            case NOPROCESS:
                return breakPoint = 10;
            case VPN_GENERATE_CONFIG:
                return breakPoint = 20;
            case WAIT:
                return breakPoint = 40;
            case AUTH:
                return breakPoint = 60;
            case GET_CONFIG:
                return breakPoint = 80;
            case ASSIGN_IP:
                return breakPoint = 85;
            case ADD_ROUTES:
                return breakPoint = 90;
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
        animatorFadeOut.setDuration(600);

        ValueAnimator animatorFadeIn = ValueAnimator.ofFloat(0f, 1f);
        animatorFadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                textView.setAlpha(alpha);
            }
        });
        animatorFadeIn.setDuration(600);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorFadeOut);
        animatorSet.play(animatorFadeIn).after(animatorFadeOut);

        return animatorSet;
    }

    public void progressLoading(int progressValue) {
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                progressBar.setProgress(progressValue);
                titleConnect.setText("Progress  " + progressValue + " %");

                int progress = progressValue;
                int animationSmoothness = 1000;
                int animationDuration = 5000;
                ButtonConnect.this.progressBar.setMax(100 * animationSmoothness);


                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(ButtonConnect.this.progressBar, "progress", progress * animationSmoothness);
                objectAnimator.setCurrentFraction(0.5F);
                objectAnimator.setDuration(animationDuration);
                objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator.start();



                // ButtonConnect.this.progressBar.startAnimation(animationProgress);


            }
        });
    }

    public void animationFinishedConnect() {
       handler.post(new Runnable() {
           @Override
           public void run() {
               int layoutWidth = ButtonConnect.this.constraintMain.getMeasuredWidth();
               int halfLayoutWidth = (int) (layoutWidth / 2) - 60;
               int endWidth = halfLayoutWidth;
               int duration = 1500;

               ButtonConnect.this.cardConnectWidth = cardConnect.getMeasuredWidth();






               ValueAnimator animatorConnect = ValueAnimator.ofInt(cardConnect.getMeasuredWidth(), endWidth);
               animatorConnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                   @Override
                   public void onAnimationUpdate(ValueAnimator animation) {
                       int val = (Integer) animation.getAnimatedValue();
                       ViewGroup.LayoutParams layoutParams = cardConnect.getLayoutParams();
                       layoutParams.width = val;
                       cardConnect.setLayoutParams(layoutParams);
                   }
               });
               animatorConnect.setDuration(duration);
               animatorConnect.setInterpolator(new DecelerateInterpolator());

               int durationDisconnect = 1500;
               ValueAnimator animatorDisconnect = ValueAnimator.ofInt(cardDisconnect.getMeasuredWidth(), endWidth);
               animatorDisconnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                   @Override
                   public void onAnimationUpdate(ValueAnimator animation) {
                       int val = (Integer) animation.getAnimatedValue();
                       ViewGroup.LayoutParams layoutParams = cardDisconnect.getLayoutParams();
                       layoutParams.width = val;
                       cardDisconnect.setLayoutParams(layoutParams);
                   }
               });
               animatorDisconnect.setDuration(durationDisconnect);
               animatorDisconnect.setInterpolator(new DecelerateInterpolator());

               AnimatorSet animatorSet = new AnimatorSet();
               animatorSet.play(animatorConnect).with(animatorDisconnect);
               animatorSet.play(getAnimatorFadeOutInText("Connected", titleConnect)).with(getAnimatorFadeOutInText("Disconnect", titleDisconnect)).after(animatorConnect);
               animatorSet.start();

           }
       });
    }

    public void buttonDisconnect() {
        // Ui change
        this.buttonDisconnectAnimationTwo();

        // Program change
        this.vpnConnectionService.disconnectServer();
    }

    public void buttonDisconnectAnimationTwo() {

        int startWidth = ButtonConnect.this.cardConnect.getMeasuredWidth();
        int endWidth = ButtonConnect.this.cardConnectWidth;
        int duration = 1500;

        // Animation connect
        ValueAnimator animatorConnect = ValueAnimator.ofInt(startWidth, endWidth);
        animatorConnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardConnect.getLayoutParams();
                layoutParams.width = val;
                cardConnect.setLayoutParams(layoutParams);
            }
        });
        animatorConnect.setDuration(duration);
        animatorConnect.setInterpolator(new LinearInterpolator());
        // animationConnect.setStartDelay(1570);
        // valueAnimator.start();

        // Animation disconnect
        int startDisconnect = cardDisconnect.getMeasuredWidth();
        ValueAnimator animatorDisconnect = ValueAnimator.ofInt(startDisconnect, 0);
        animatorDisconnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardDisconnect.getLayoutParams();
                layoutParams.width = val;
                cardDisconnect.setLayoutParams(layoutParams);
            }
        });
        animatorDisconnect.setDuration(duration);
        animatorDisconnect.setInterpolator(new LinearInterpolator());
        // animationDisconnect.setStartDelay(1500);
        // valueAnimatorDisconnect.start();




        /*
        ViewPropertyAnimator animatorText = ButtonConnect.this.titleDisconnect
                .animate()
                .alpha(0f)
                .setDuration(1500)
                .setStartDelay(0);

         */

        // Text animation disconnect button
        ValueAnimator animatorTextDisconnect = ValueAnimator.ofFloat(1f, 0f);
        animatorTextDisconnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ButtonConnect.this.titleDisconnect.setAlpha(alpha);
            }
        });
        animatorTextDisconnect.setDuration(1500);

        // Text animation connect button
        ValueAnimator animatorTextConnectFadeout = ValueAnimator.ofFloat(1f, 0f);
        animatorTextConnectFadeout.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ButtonConnect.this.titleConnect.setAlpha(alpha);
            }
        });
        animatorTextConnectFadeout.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                titleConnect.setText("Start connection");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorTextConnectFadeout.setDuration(1500);

        // Text animation connect button
        ValueAnimator animatorTextConnectFadein = ValueAnimator.ofFloat(0f, 1f);
        animatorTextConnectFadein.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                titleConnect.setAlpha(alpha);
            }
        });
        animatorTextConnectFadein.setDuration(1500);

        int animationDuration = 5000;
        ObjectAnimator animatorProgress = ObjectAnimator.ofInt(ButtonConnect.this.progressBar, "progress", 0);
        animatorProgress.setDuration(animationDuration);
        animatorProgress.setInterpolator(new DecelerateInterpolator());

        ValueAnimator animatorTitle = ValueAnimator.ofInt(100, 0);
        animatorTitle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                titleConnect.setText("Progress  " + value + "%");
            }
        });
        animatorTitle.setDuration(animationDuration);
        animatorTitle.setInterpolator(new DecelerateInterpolator());


        Animator animatorTextTitleProgress = getAnimatorFadeOutInText("Progress  100%", titleConnect);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorConnect).with(animatorDisconnect);
        animatorSet.play(animatorTextTitleProgress).after(animatorConnect);
        animatorSet.play(animatorProgress).with(animatorTitle).after(animatorTextTitleProgress);
        animatorSet.play(animatorTextDisconnect).before(animatorConnect);
        // animatorSet.play(animatorTextConnectFadeout).after(animatorProgress);
        // animatorSet.play(animatorTextConnectFadein).after(animatorTextConnectFadeout);
        animatorSet.play(getAnimatorFadeOutInText("Start connection", titleConnect)).after(animatorProgress);
        animatorSet.start();

    }












}
