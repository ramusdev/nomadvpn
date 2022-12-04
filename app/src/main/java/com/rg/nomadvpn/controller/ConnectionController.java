package com.rg.nomadvpn.controller;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.service.NotificationService;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.ui.home.ButtonConnectSecond;
import com.rg.nomadvpn.ui.home.ButtonDisconnect;
import com.rg.nomadvpn.ui.home.ButtonProfile;
import com.rg.nomadvpn.ui.home.SupportMessage;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.Date;

public class ConnectionController {
    private VpnConnectionService vpnConnectionService;
    private NotificationService notificationService;
    private View view;
    private SupportMessage supportMessage;
    private CardView cardConnect;
    private CardView cardDisconnect;
    private TextView titleConnect;
    private TextView titleDisconnect;
    private ConstraintLayout layoutView;
    private ConstraintLayout constraintMain;
    private ConstraintLayout layoutDisconnect;
    private ConstraintLayout supportLayout;
    private ProgressBar progressBar;
    private ButtonConnectSecond buttonConnectSecond;
    private ButtonDisconnect buttonDisconnect;
    private ButtonProfile buttonProfile;
    private Handler handler = new Handler();

    public ConnectionController() {

    }

    public void setVpnService(VpnConnectionService vpnConnectionService) {
        this.vpnConnectionService = vpnConnectionService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setView(View view) {
        this.view = view;
    }


    public void init() {
        // this.cardConnect = view.findViewById(R.id.button_card);
        // this.titleConnect = view.findViewById(R.id.button_title);
        // this.layoutView = view.findViewById(R.id.button_layout);
        // this.progressBar = view.findViewById(R.id.button_progress);
        // this.cardDisconnect = view.findViewById(R.id.card_disconnect);
        // this.constraintMain = view.findViewById(R.id.constraint_main);
        // this.titleDisconnect = view.findViewById(R.id.title_disconnect);
        // this.layoutDisconnect = view.findViewById(R.id.layout_disconnect);
        // this.supportLayout = view.findViewById(R.id.support_layout);

        this.supportMessage = new SupportMessage(view);
        this.buttonConnectSecond = new ButtonConnectSecond(view);
        this.buttonDisconnect = new ButtonDisconnect(view);
        this.buttonProfile = new ButtonProfile(view);

        initClick();
    }

    public void initClick() {
        if (vpnConnectionService.isVpnProfileInstalled()) {
            if (vpnConnectionService.isOpnVpnServiceConnected()) {
                Log.d(MainActivity.LOGTAG, "Init 1");
                buttonProfile.hideButton();
                buttonConnectSecond.hideButton();
                buttonDisconnect.showButton();
            } else {
                Log.d(MainActivity.LOGTAG, "Init 2");
                buttonProfile.hideButton();
                buttonDisconnect.hideButton();
                buttonConnectSecond.clear();
                buttonConnectSecond.showButton();
            }
            buttonConnectSecond.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startConnectionClickDown();
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startConnectionClickUp();
                    }
                    return true;
                }
            });
            buttonDisconnect.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        disconnectClickDown();
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        disconnectClickUp();
                    }
                    return true;
                }
            });
        } else {
            buttonConnectSecond.hideButton();
            buttonDisconnect.hideButton();
            buttonProfile.showButton();
            buttonProfile.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        profileClickDown();
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        profileClickUp();
                    }
                    return true;
                }
            });
        }
    }

    public void startConnectionClickDown() {
        this.vibrate();
        buttonConnectSecond.buttonPressDownAnimation();
    }
    public void startConnectionClickUp() {
        buttonConnectSecond.buttonPressUpAnimation("Progress: 0%");

        vpnConnectionService.startVpnService();
        this.startConnectionProgress();
        buttonConnectSecond.setConnectedCallBack(new ButtonConnectSecond.ConnectedCallBack() {
            @Override
            public void onConnected() {
                buttonConnectSecond.hideButton();
                buttonDisconnect.showButton();
            }
        });

        buttonConnectSecond.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    stopConnectionClickDown();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopConnectionClickUp();
                }
                return true;
            }
        });
    }

    public void disconnectClickDown() {
        this.vibrate();
        buttonDisconnect.clickAnimationDown();
    }

    public void disconnectClickUp() {
        vpnConnectionService.disconnectServer();
        notificationService.showDisconnectMessage();
        buttonDisconnect.clickAnimationUp(new ButtonDisconnect.AnimationEndInterface() {
            @Override
            public void animationEnd() {
                initClick();
            }
        });
    }

    public void stopConnectionClickDown() {
        this.vibrate();
        buttonConnectSecond.buttonPressDownAnimation();
    }

    public void stopConnectionClickUp() {
        notificationService.showDisconnectMessage();
        vpnConnectionService.disconnectServer();
        buttonConnectSecond.buttonPressUpAnimation("", new ButtonDisconnect.AnimationEndInterface() {
            @Override
            public void animationEnd() {
                Log.d(MainActivity.LOGTAG, "Stop connection click interface inside");
                initClick();
            }
        });
    }

    public void profileClickDown() {
        this.vibrate();
        buttonProfile.clickAnimationDown();
    }

    public void profileClickUp() {
        buttonProfile.clickAnimationUp();
        vpnConnectionService.vpnProfileInstall(new VpnConnectionService.Callback() {
            @Override
            public void callingBack() {
                initClick();
            }
        });
    }

    public void startConnectionProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int breakPoint = 0;
                long currentDateTime = new Date().getTime();
                long waitSeconds = 10;
                long waitMilliseconds = waitSeconds * 1000;
                boolean isShowed = false;
                boolean isShowedNotification = false;

                while (true) {
                    // Update animation
                    int breakPointCurrent = vpnConnectionService.getStatusPercent();
                    if (breakPointCurrent != breakPoint) {
                        breakPoint = breakPointCurrent;
                        buttonConnectSecond.updateProgressBar(breakPoint);
                    }

                    // Support message
                    long dateTime = new Date().getTime();
                    long dateDifference = dateTime - currentDateTime;
                    if (dateDifference > waitMilliseconds) {
                        if (! isShowed) {
                            supportMessage.showMessage(true);
                            isShowed = true;
                        }
                    }

                    // Notification
                    if (vpnConnectionService.isOpnVpnServiceCreated()) {
                        if (! isShowedNotification) {
                            notificationService.showConnectMessage();
                            isShowedNotification = true;
                        }
                    }

                    // Break
                    String status = vpnConnectionService.getStatus();
                    if (status.equals("Connected")) {
                        break;
                    } else if (status.equals("Disconnected")) {
                        break;
                    }

                    // Thread sleep
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void vibrate() {
        long[] pattern = new long[] {25};
        Vibrator vibrator = (Vibrator) MyApplicationContext.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator.hasVibrator()) {
            if (vibrator.hasAmplitudeControl()) {
                VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
                vibrator.vibrate(vibrationEffect);
            } else {
                vibrator.vibrate(pattern, -1);
            }
        }
    }
}
