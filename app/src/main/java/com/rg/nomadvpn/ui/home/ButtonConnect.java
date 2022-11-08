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
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int breakPoint = ButtonConnect.this.getBreakPoint();
                    if (progressValue < breakPoint) {
                        progressValue += 1;
                    }

                    progressLoading(progressValue);

                    if (progressValue >= 100) {
                        progressFinished(progressValue);
                        break;
                    }
                }
            }
        }).start();
    }

    public int getBreakPoint() {
        String status = OpenVPNService.getStatus();
        int breakPoint = 0;

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

        return breakPoint;
    }

    public void progressLoading(int progressValue) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progressValue);
                textView.setText("Progress  " + progressValue + " %");
            }
        });
    }

    public void progressFinished(int progressValue) {
       handler.post(new Runnable() {
           @Override
           public void run() {
               ButtonConnect.this.layoutView.startAnimation(animationFadeIn);
               ButtonConnect.this.textView.setText("Connected");
           }
       });
    }
}
