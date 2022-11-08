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
import com.rg.nomadvpn.model.ServerStatusEnum;
import com.rg.nomadvpn.service.StartConnectListener;
import com.rg.nomadvpn.utils.MyApplicationContext;

import de.blinkt.openvpn.core.OpenVPNService;

public class ButtonConnect {
    private View rootView;
    private CardView cardView;
    private TextView textView;
    private ConstraintLayout layoutView;
    private Animation animationFadeIn;
    private Animation animationFadeInTwo;
    private ProgressBar progressBar;
    private StartConnectListener listener;

    private Handler handler = new Handler();
    private int progressValue = 0;

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

    public void setOnClickListener(StartConnectListener listener) {
        this.listener = listener;
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
        this.listener.onClick();
        layoutView.startAnimation(animationFadeIn);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    progressValue += 1;

                    String status = OpenVPNService.getStatus();
                    int breakPoint = getBreakPoint(status);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // progressBar.setProgress(progressValue);
                            // textView.setText("Progress  " + progressItem + "%");
                            // String status = OpenVPNService.getStatus();
                            // Log.d(MainActivity.LOGTAG, "Status: " + status);
                            // ButtonConnect.this.textView.setText(status);
                            // ButtonConnect.this.buttonProgress(progressValue);

                            // if (progressItem == 100) {
                                // ButtonConnect.this.buttonFinished();
                            // }
                        }
                    });
                }
            }
        }).start();
    }

    public int getBreakPoint(String status) {
        int breakPoint = 0;

        if (status.equals("VPN_GENERATE_CONFIG")) {
            breakPoint = 15;
        } else if (status.equals("WAIT")) {
            breakPoint = 30;
        } else if (status.equals("AUTH")) {
            breakPoint = 45;
        }

        return breakPoint;
    }

    public void buttonFinished() {
        ButtonConnect.this.layoutView.startAnimation(animationFadeIn);
        // ButtonConnect.this.cardView.startAnimation(animationFadeIn);
        ButtonConnect.this.textView.setText("Connected");
    }
}
