package com.rg.nomadvpn.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.db.ServerCurrent;
import com.rg.nomadvpn.db.ServerHolder;
import com.rg.nomadvpn.locator.ServiceLocator;
import com.rg.nomadvpn.model.ServerHolderConfiguration;
import com.rg.nomadvpn.service.NotificationService;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.ui.connection.ButtonConnect;
import com.rg.nomadvpn.ui.connection.ButtonDisconnect;
import com.rg.nomadvpn.ui.connection.ButtonProfile;
import com.rg.nomadvpn.ui.connection.ButtonServer;
import com.rg.nomadvpn.ui.connection.ConnectionFragment;
import com.rg.nomadvpn.ui.connection.ConnectionViewModel;
import com.rg.nomadvpn.ui.connection.SupportMessage;
import com.rg.nomadvpn.ui.server.ServerFragment;
import com.rg.nomadvpn.ui.speed.SpeedView;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.Date;

public class ConnectionController {
    private VpnConnectionService vpnConnectionService;
    private NotificationService notificationService;
    private View view;
    private ConnectionFragment fragment;
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
    public static BroadcastReceiver broadcastReceiverStatic;
    private ButtonConnect buttonConnect;
    private ButtonDisconnect buttonDisconnect;
    private ButtonProfile buttonProfile;
    private ButtonServer buttonServer;
    private ConnectionViewModel connectionViewModel;
    private Handler handler = new Handler();
    private static ConnectionController instance;
    private TextView downloadSpeedTextView;
    private StringBuilder speedDownloadStore = new StringBuilder("0.0");

    public ConnectionController() {
        instance = this;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setFragment(ConnectionFragment fragment) {
        this.fragment = fragment;
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

        downloadSpeedTextView = view.findViewById(R.id.download_speed);

        vpnConnectionService = (VpnConnectionService) ServiceLocator.getService(VpnConnectionService.class);
        vpnConnectionService.setFragment(fragment);

        notificationService = (NotificationService) ServiceLocator.getService(NotificationService.class);

        this.supportMessage = new SupportMessage(view);
        this.buttonConnect = new ButtonConnect(view);
        this.buttonDisconnect = new ButtonDisconnect(view);
        this.buttonProfile = new ButtonProfile(view);
        this.buttonServer = new ButtonServer(view);

        updateData();
        broadcastReceiverRegister();
        initClick();
    }

    public void initClick() {
        if (vpnConnectionService.isVpnProfileInstalled()) {
            if (vpnConnectionService.isOpnVpnServiceConnected()) {
                Log.d(MainActivity.LOGTAG, "Init 1");
                buttonProfile.hideButton();
                buttonConnect.hideButton();
                buttonDisconnect.showButton();
            } else {
                Log.d(MainActivity.LOGTAG, "Init 2");
                buttonProfile.hideButton();
                buttonDisconnect.hideButton();
                buttonConnect.clear();
                buttonConnect.showButton();
            }
            buttonConnect.setOnTouchListener(new View.OnTouchListener() {
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
            buttonServer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openServerFragment();
                }
            });
        } else {
            buttonConnect.hideButton();
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
        initServerButton();
    }

    public void startConnectionClickDown() {
        this.vibrate();
        buttonConnect.buttonPressDownAnimation();
    }
    public void startConnectionClickUp() {
        ServerHolder serverHolder = new ServerHolder();
        ServerHolderConfiguration serverHolderConfiguration = serverHolder.getServerById(ServerCurrent.getServerIndex());

        vpnConnectionService.startVpnService(serverHolderConfiguration);
        buttonConnect.buttonPressUpAnimation();
        this.startConnectionProgress();
        buttonConnect.setConnectedCallBack(new ButtonConnect.ConnectedCallBack() {
            @Override
            public void onConnected() {
                buttonConnect.hideButton();
                buttonDisconnect.showButton();
            }
        });

        buttonConnect.setOnTouchListener(new View.OnTouchListener() {
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // vpnConnectionService.disconnectServer();
                        // notificationService.showDisconnectMessage();
                        initClick();
                    }
                }).start();
            }
        });
    }

    public void disconnectClickSleep() {
        if (vpnConnectionService.isOpnVpnServiceConnected()) {
            vpnConnectionService.disconnectServer();
            notificationService.showDisconnectMessage();
        }
    }

    public void stopConnectionClickDown() {
        this.vibrate();
        buttonConnect.buttonPressDownAnimation();
    }

    public void stopConnectionClickUp() {
        buttonConnect.buttonPressUpAnimation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                vpnConnectionService.disconnectServer();
                notificationService.showDisconnectMessage();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initClick();
            }
        }).start();
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
                    // Thread sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update animation
                    int breakPointCurrent = vpnConnectionService.getStatusPercent();
                    if (breakPointCurrent != breakPoint) {
                        breakPoint = breakPointCurrent;
                        buttonConnect.updateProgressBar(breakPoint);
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
                            // notificationService.showConnectMessage();
                            notificationService.showWaitingMessage();
                            isShowedNotification = true;
                        }
                    }

                    // Break
                    String status = vpnConnectionService.getStatus();
                    if (status.equals("Connected")) {
                        notificationService.showConnectMessage();
                        break;
                    } else if (status.equals("Disconnected")) {
                        // initClick();
                        break;
                    }

                    // Thread sleep
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void updateData() {
        TextView durationValue = view.findViewById(R.id.value_time);
        String textStatus = MyApplicationContext.getAppContext().getString(R.string.connected_status);

        connectionViewModel = new ViewModelProvider(fragment).get(ConnectionViewModel.class);

        /*
        connectionViewModel.getDuration().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                durationValue.setText(value);
            }
        });

        TextView statusValue = view.findViewById(R.id.value_status);
        connectionViewModel.getStatus().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                statusValue.setText(value);
                if (value.equals(textStatus)) {
                    statusValue.setTextColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.status_textconnected));
                } else {
                    statusValue.setTextColor(MyApplicationContext.getAppContext().getResources().getColor(R.color.status_text));
                }
            }
        });

        TextView receiveInValue = view.findViewById(R.id.value_receivein);
        connectionViewModel.getReceiveIn().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                receiveInValue.setText(value);
            }
        });

        TextView receiveOutValue = view.findViewById(R.id.value_receiveout);
        connectionViewModel.getReceiveOut().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                receiveOutValue.setText(value);
            }
        });

        TextView speedInValue = view.findViewById(R.id.value_speedin);
        connectionViewModel.getSpeedIn().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                speedInValue.setText(value);
            }
        });

        TextView speedOutValue = view.findViewById(R.id.value_speedout);
        connectionViewModel.getSpeedOut().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                speedOutValue.setText(value);
            }
        });

        */






        SpeedView speedView = view.findViewById(R.id.speed_view);
        speedView.setView(view);
        speedView.init();
        connectionViewModel.getSpeedIn().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                String mbitString = value.replace(",", ".");
                // downloadSpeedTextView.setText(mbitString);

                speedView.downloadAnimation(value);
            }
        });


    }

    public void broadcastReceiverRegister() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String duration = intent.getStringExtra("duration");
                String status = vpnConnectionService.getStatusName();
                String receiveIn = intent.getStringExtra("receiveIn");
                String receiveOut = intent.getStringExtra("receiveOut");
                String speedIn = intent.getStringExtra("speedIn");
                String speedOut = intent.getStringExtra("speedOut");

                if (duration == null) {
                    duration = "00:00:00";
                }

                if (status == null) {
                    status = MyApplicationContext.getAppContext().getString(R.string.disconnected_status);
                }

                if (receiveIn == null) {
                    receiveIn = "0 MB";
                }

                if (receiveOut == null) {
                    receiveOut = "0 MB";
                }

                if (speedIn == null) {
                    speedIn = "0";
                }

                if (speedOut == null) {
                    speedOut = "0";
                }

                connectionViewModel.setDuration(duration);
                connectionViewModel.setStatus(status);
                connectionViewModel.setReceiveIn(receiveIn);
                connectionViewModel.setReceiveOut(receiveOut);
                connectionViewModel.setSpeedIn(speedIn);
                connectionViewModel.setSpeedOut(speedOut);
            }
        };

        ConnectionController.broadcastReceiverStatic = broadcastReceiver;
    }

    public static void onResume(ConnectionFragment connectionFragment) {
        LocalBroadcastManager.getInstance(connectionFragment.getActivity()).registerReceiver(broadcastReceiverStatic, new IntentFilter("connectionState"));
    }

    public void openServerFragment() {
        FragmentTransaction fragmentTransaction = MainActivity.getInstance().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ServerFragment.class, null).commit();
    }

    public void initServerButton() {
        buttonServer.initServerButton();
    }

    public void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.vibrateApiTen();
        } else {
            this.vibrateApiEight();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void vibrateApiTen() {
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

    public void vibrateApiEight() {
        int pattern = 25;
        Vibrator vibrator = (Vibrator) MyApplicationContext.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);

        vibrator.vibrate(pattern);
    }

    public static ConnectionController getInstance() {
        return instance;
    }
}
