package com.rg.nomadvpn.controller;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

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
                buttonProfile.hideButton();
                buttonConnectSecond.hideButton();
                buttonDisconnect.showButton();
            } else {
                buttonProfile.hideButton();
                buttonDisconnect.hideButton();
                buttonConnectSecond.clear();
                buttonConnectSecond.showButton();
            }
            buttonConnectSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startConnectionClick();
                }
            });
            buttonDisconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    disconnectClick();
                }
            });
        } else {
            buttonConnectSecond.hideButton();
            buttonDisconnect.hideButton();
            buttonProfile.showButton();
            buttonProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileClick();
                }
            });
        }
    }

    public void startConnectionClick() {
        this.vibrate();
        buttonConnectSecond.buttonPressAnimation("Progress: 0%");
        vpnConnectionService.startVpnService();
        this.startConnectionProgress();
        buttonConnectSecond.setConnectedCallBack(new ButtonConnectSecond.ConnectedCallBack() {
            @Override
            public void onConnected() {
                buttonConnectSecond.hideButton();
                buttonDisconnect.showButton();
            }
        });

        buttonConnectSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopConnectionClick();
            }
        });
    }

    public void disconnectClick() {
        this.vibrate();
        vpnConnectionService.disconnectServer();
        notificationService.showDisconnectMessage();
        buttonDisconnect.clickAnimation(new ButtonDisconnect.AnimationEndInterface() {
            @Override
            public void animationEnd() {
                initClick();
            }
        });
    }

    public void stopConnectionClick() {
        this.vibrate();
        buttonConnectSecond.buttonPressAnimation("", new ButtonDisconnect.AnimationEndInterface() {
            @Override
            public void animationEnd() {
                initClick();
            }
        });
        vpnConnectionService.disconnectServer();
        notificationService.showDisconnectMessage();
    }

    public void profileClick() {
        this.vibrate();
        buttonProfile.clickAnimation();
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
                        Thread.sleep(1400);
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
        // int[] amplitudes = new int[] {0, 255};
        Vibrator vibrator = (Vibrator) MyApplicationContext.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator.hasVibrator()) {
            if (vibrator.hasAmplitudeControl()) {
                // VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, amplitudes, -1);
                VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
                vibrator.vibrate(vibrationEffect);
            } else {
                vibrator.vibrate(pattern, -1);
            }
        }
    }
}
