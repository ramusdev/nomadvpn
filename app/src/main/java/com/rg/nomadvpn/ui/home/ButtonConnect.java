package com.rg.nomadvpn.ui.home;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class ButtonConnect {
    private View rootView;
    private CardView cardView;
    private TextView textView;
    private ConstraintLayout layoutView;
    private Animation animationFadeIn;
    private Animation animationFadeInTwo;
    private ProgressBar progressBar;

    // private final Handler handler = new Handler();
    private Handler handler = new Handler();
    private int progressItem = 0;

    public ButtonConnect(View rootView) {
        this.rootView = rootView;
        this.cardView = rootView.findViewById(R.id.button_card);
        this.textView = rootView.findViewById(R.id.button_title);
        this.layoutView = rootView.findViewById(R.id.button_layout);
        this.progressBar = rootView.findViewById(R.id.button_progress);

        animationFadeIn = AnimationUtils.loadAnimation(MyApplicationContext.getAppContext(), R.anim.fade_in);
        animationFadeInTwo = AnimationUtils.loadAnimation(MyApplicationContext.getAppContext(), R.anim.fade_in);
        clickHandler();
    }

    public void clickHandler() {
        // progressBar.setAnimation(animationFadeIn);
        // animationFadeIn.start();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d(MainActivity.LOGTAG, "Click on the card");
                // progressBar.setAnimation(animationFadeIn);
                // progressBar.startAnimation(animationFadeIn);
                // textView.setAnimation(animationFadeInTwo);
                buttonStart();
            }
        });
    }

    public void buttonStart() {
        // cardView.setAnimation(animationFadeIn);
        // textView.setText("Start connection");
        // Log.d(MainActivity.LOGTAG, "Button start");
        // progressBar.setAnimation(animationFadeIn);
        // cardView.startAnimation(animationFadeIn);
        layoutView.startAnimation(animationFadeIn);
        // progressBar.startAnimation(animationFadeIn);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressItem < 100) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressItem += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressItem);
                            textView.setText("Progress  " + progressItem + "%");
                            if (progressItem == 100) {
                                ButtonConnect.this.buttonFinished();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void buttonFinished() {
        ButtonConnect.this.layoutView.startAnimation(animationFadeIn);
        // ButtonConnect.this.cardView.startAnimation(animationFadeIn);
        ButtonConnect.this.textView.setText("Connected");
    }
}
