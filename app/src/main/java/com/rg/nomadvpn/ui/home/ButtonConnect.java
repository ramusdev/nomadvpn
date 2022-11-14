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
        // progressBar.setAnimation(animationFadeIn);
        // animationFadeIn.start();
        cardConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d(MainActivity.LOGTAG, "Click on the card");
                // progressBar.setAnimation(animationFadeIn);
                // progressBar.startAnimation(animationFadeIn);
                // textView.setAnimation(animationFadeInTwo);
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
        this.vpnConnectionService.onClick();

        // Ui change
        /*
        handler.post(new Runnable() {
            @Override
            public void run() {
                ButtonConnect.this.layoutView.startAnimation(animationFadeIn);
            }
        });
        */

        /*
        handler.post(new Runnable() {
            @Override
            public void run() {

                int progress = 30;
                int animationSmoothness = 1000;
                int animationDuration = 5000;
                ButtonConnect.this.progressBar.setMax(100 * animationSmoothness);

                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(ButtonConnect.this.progressBar, "progress", progress * animationSmoothness);
                objectAnimator.setDuration(animationDuration);
                objectAnimator.setInterpolator(new BounceInterpolator());
                objectAnimator.start();

            }
        });
        */





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
                        progressFinished(progressValue);
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
        // this.atomicProgressBar.set(this.progressBar.getProgress());

        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void run() {
                int animationSmoothness = 1000;
                int animationDuration = 2000;

                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(ButtonConnect.this.progressBar, "progress", breakPoint * animationSmoothness);
                objectAnimator.setDuration(animationDuration);
                objectAnimator.setInterpolator(new DecelerateInterpolator());

                objectAnimator.addListener(progressAnimationListener());
                objectAnimator.start();
            }
        });

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                int start = ButtonConnect.this.atomicProgressBar.get();
                for (int i = 0; i < 100; ++i) {
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int finalI = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String text = "Progress  " + String.valueOf(finalI) + "%";
                            ButtonConnect.this.textView.setText(text);
                        }
                    });
                }
            }
        }).start();

         */
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

    public void progressFinished(int progressValue) {
       handler.post(new Runnable() {
           @Override
           public void run() {
               // ButtonConnect.this.layoutView.startAnimation(animationFadeIn);
               // ButtonConnect.this.textView.setText("Connected");

               int layoutWidth = ButtonConnect.this.constraintMain.getMeasuredWidth();
               // Log.d(MainActivity.LOGTAG, "Measured width: " + layoutWidth);
               int halfLayoutWidth = (int) (layoutWidth / 2) - 70;
               int endWidth = halfLayoutWidth;
               int duration = 1500;

               ButtonConnect.this.cardConnectWidth = cardConnect.getMeasuredWidth();

               ValueAnimator valueAnimator = ValueAnimator.ofInt(cardConnect.getMeasuredWidth(), endWidth);
               valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                   @Override
                   public void onAnimationUpdate(ValueAnimator animation) {
                       int val = (Integer) animation.getAnimatedValue();
                       ViewGroup.LayoutParams layoutParams = cardConnect.getLayoutParams();
                       layoutParams.width = val;
                       cardConnect.setLayoutParams(layoutParams);
                   }
               });
               valueAnimator.setDuration(duration);
               valueAnimator.setInterpolator(new DecelerateInterpolator());
               valueAnimator.setStartDelay(1500);
               valueAnimator.start();

               int durationDisconnect = 1500;
               ValueAnimator valueAnimatorDisconnect = ValueAnimator.ofInt(cardDisconnect.getMeasuredWidth(), endWidth);
               valueAnimatorDisconnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                   @Override
                   public void onAnimationUpdate(ValueAnimator animation) {
                       int val = (Integer) animation.getAnimatedValue();
                       ViewGroup.LayoutParams layoutParams = cardDisconnect.getLayoutParams();
                       layoutParams.width = val;
                       cardDisconnect.setLayoutParams(layoutParams);
                   }
               });
               valueAnimatorDisconnect.setDuration(durationDisconnect);
               valueAnimatorDisconnect.setInterpolator(new DecelerateInterpolator());
               valueAnimatorDisconnect.setStartDelay(1500);
               valueAnimatorDisconnect.start();

               showFadeinText("Disconnect");



               /*
               ConstraintSet constraintSet = new ConstraintSet();
               constraintSet.clone(ButtonConnect.this.constraintMain);
               constraintSet.constrainPercentWidth(R.id.button_card, 0.47F);
               constraintSet.constrainPercentWidth(R.id.card_disconnect, 0.47F);

               final ChangeBounds transition = new ChangeBounds();
               transition.setDuration(500L);

               TransitionManager.beginDelayedTransition(ButtonConnect.this.constraintMain, transition);
               constraintSet.applyTo(ButtonConnect.this.constraintMain);
               */
           }
       });
    }

    public void buttonDisconnect() {
        // Ui change
        // this.buttonDisconnectAnimation();
        this.buttonDisconnectAnimationTwo();

        // Program change
        this.vpnConnectionService.disconnectServer();
    }

    public void buttonDisconnectAnimationTwo() {

        int startWidth = ButtonConnect.this.cardConnect.getMeasuredWidth();
        int endWidth = ButtonConnect.this.cardConnectWidth;
        int duration = 1500;

        ValueAnimator animationConnect = ValueAnimator.ofInt(startWidth, endWidth);
        animationConnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardConnect.getLayoutParams();
                layoutParams.width = val;
                cardConnect.setLayoutParams(layoutParams);
            }
        });
        animationConnect.setDuration(duration);
        animationConnect.setInterpolator(new LinearInterpolator());
        // animationConnect.setStartDelay(1570);
        // valueAnimator.start();

        int startDisconnect = cardDisconnect.getMeasuredWidth();
        ValueAnimator animationDisconnect = ValueAnimator.ofInt(startDisconnect, 0);
        animationDisconnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardDisconnect.getLayoutParams();
                layoutParams.width = val;
                cardDisconnect.setLayoutParams(layoutParams);
            }
        });
        animationDisconnect.setDuration(duration);
        animationDisconnect.setInterpolator(new LinearInterpolator());
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
        ValueAnimator animatorText = ValueAnimator.ofFloat(1f, 0f);
        animatorText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ButtonConnect.this.titleDisconnect.setAlpha(alpha);
            }
        });
        animatorText.setDuration(1500);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animationConnect, animationDisconnect);
        // animatorSet.playTogether(animationConnect, animationDisconnect);
        animatorSet.start();

    }

    private void buttonDisconnectAnimation() {
        showFadeoutText();

        // int layoutWidth = ButtonConnect.this.layoutView.getMeasuredWidth();
        int startWidth = ButtonConnect.this.cardConnect.getMeasuredWidth();
        int endWidth = ButtonConnect.this.cardConnectWidth;
        int duration = 1500;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(startWidth, endWidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardConnect.getLayoutParams();
                layoutParams.width = val;
                cardConnect.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setStartDelay(1550);
        valueAnimator.start();

        int startDisconnect = cardDisconnect.getMeasuredWidth();
        ValueAnimator valueAnimatorDisconnect = ValueAnimator.ofInt(startDisconnect, 0);
        valueAnimatorDisconnect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardDisconnect.getLayoutParams();
                layoutParams.width = val;
                cardDisconnect.setLayoutParams(layoutParams);
            }
        });
        valueAnimatorDisconnect.setDuration(duration);
        valueAnimatorDisconnect.setInterpolator(new LinearInterpolator());
        valueAnimatorDisconnect.setStartDelay(1500);
        valueAnimatorDisconnect.start();

        clearProgressAnimation();

    }

    public Animator.AnimatorListener progressAnimationListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
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
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    public Animator.AnimatorListener progressAnimationClearListener() {
        // executorService.shutdown();
        // executorService = Executors.newFixedThreadPool(1);
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        int from = 100;
                        int to = 0;
                        for (int i = from; i >= to; --i) {
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
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showFadeoutFadeinText("Start connect");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    public void showFadeoutFadeinText(String text) {
        Runnable endAction;

        ButtonConnect.this.titleConnect
                .animate()
                .alpha(0f)
                .setDuration(500)
                .setStartDelay(0)
                .withEndAction(endAction = new Runnable() {
                    @Override
                    public void run() {
                        ButtonConnect.this.titleConnect.setText(text);
                        ButtonConnect.this.titleConnect
                                .animate()
                                .alpha(1f)
                                .setDuration(500)
                                .setStartDelay(500);
                    }
                });
    }
    public void showFadeinText(String text) {
        ButtonConnect.this.titleDisconnect.setText(text);
        // Runnable endAction;

        ButtonConnect.this.titleDisconnect
                .animate()
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(2000);

                /*
        ButtonConnect.this.titleDisconnect
                .animate()
                .alpha(0f)
                .setDuration(1000)
                .setStartDelay(0)
                .withEndAction(endAction = new Runnable() {
                    @Override
                    public void run() {
                        ButtonConnect.this.titleDisconnect
                                .animate()
                                .alpha(1f)
                                .setDuration(1000)
                                .setStartDelay(2000);

                    }
                });

                 */
    }

    public void showFadeoutText() {
        ButtonConnect.this.titleDisconnect
                .animate()
                .alpha(0f)
                .setDuration(1500)
                .setStartDelay(0);
    }

   public void clearProgressAnimation() {
       int animationSmoothness = 1000;
       int animationDuration = 5000;

       ObjectAnimator objectAnimator = ObjectAnimator.ofInt(ButtonConnect.this.progressBar, "progress", 0);
       objectAnimator.setDuration(animationDuration);
       objectAnimator.setInterpolator(new DecelerateInterpolator());
       objectAnimator.addListener(progressAnimationClearListener());
       objectAnimator.setStartDelay(3100);
       objectAnimator.start();
   }


}
