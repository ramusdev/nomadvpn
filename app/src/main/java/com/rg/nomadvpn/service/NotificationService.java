package com.rg.nomadvpn.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.annotation.RequiresApi;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.utils.MyApplicationContext;

import de.blinkt.openvpn.api.APIVpnProfile;
import de.blinkt.openvpn.core.OpenVPNService;

public class NotificationService {
    private static final String NOTIFICATION_TITLE = "Namad VPN";
    private static final String TEXT_CONNECTED = MyApplicationContext.getAppContext().getString(R.string.notification_connected);
    private static final String TEXT_DISCONNECTED = MyApplicationContext.getAppContext().getString(R.string.notification_disconnected);
    private static final String TEXT_WAITING = MyApplicationContext.getAppContext().getString(R.string.notification_waiting);
    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFY_ID = 100;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private SpannableString title;
    private Notification notification;
    private VpnConnectionService vpnConnectionService;

    public NotificationService(VpnConnectionService vpnConnectionService) {
        this.vpnConnectionService = vpnConnectionService;
        buildNotification();
    }

    public void buildNotification() {
        Intent intent = new Intent(MyApplicationContext.getAppContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(MyApplicationContext.getAppContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Notification channel
        String channelName = "channel_name";
        String channelDescription = "channel_description";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        notificationChannel.setDescription(channelDescription);
        notificationChannel.enableLights(false);

        // Notification manager
        notificationManager = (NotificationManager) MyApplicationContext.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        // Title
        title = new SpannableString(NOTIFICATION_TITLE);
        int color = MyApplicationContext.getAppContext().getResources().getColor(R.color.main_background);
        title.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(new ForegroundColorSpan(color), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void showConnectMessage() {
        SpannableString text = null;

        text = new SpannableString(TEXT_CONNECTED);
        int color = MyApplicationContext.getAppContext().getResources().getColor(R.color.status_textconnected);
        text.setSpan(new StyleSpan(Typeface.BOLD), 19, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(color), 19, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        notification = new Notification.Builder(MyApplicationContext.getAppContext())
                .setSmallIcon(R.drawable.ic_status)
                .setChannelId(CHANNEL_ID)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(text)
                .build();

        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void showWaitingMessage() {
        SpannableString text = null;

        text = new SpannableString(TEXT_WAITING);
        int color = MyApplicationContext.getAppContext().getResources().getColor(R.color.status_textwaiting);
        text.setSpan(new StyleSpan(Typeface.BOLD), 19, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(color), 19, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        notification = new Notification.Builder(MyApplicationContext.getAppContext())
                .setSmallIcon(R.drawable.ic_status)
                .setChannelId(CHANNEL_ID)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(text)
                .build();

        notificationManager.notify(NOTIFY_ID, notification);

        vpnConnectionService.stopForeground();
        vpnConnectionService.startForeground(NOTIFY_ID, notification);
    }

    public void showDisconnectMessage() {
        SpannableString text = null;

        text = new SpannableString(TEXT_DISCONNECTED);
        int color = MyApplicationContext.getAppContext().getResources().getColor(R.color.status_text);
        text.setSpan(new StyleSpan(Typeface.BOLD), 19, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(color), 19, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        notification = new Notification.Builder(MyApplicationContext.getAppContext())
                .setSmallIcon(R.drawable.ic_status)
                .setChannelId(CHANNEL_ID)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(text)
                .build();

        notificationManager.notify(NOTIFY_ID, notification);

        vpnConnectionService.stopForeground();
    }
}
