package com.rg.nomadvpn.service;

import static android.app.Activity.RESULT_OK;

import android.app.Notification;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.rg.nomadvpn.model.ServerStatusEnum;
import com.rg.nomadvpn.model.ServerVpnConfiguration;

import com.rg.nomadvpn.utils.MyApplicationContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;

public class VpnConnectionService {
    private final static String LOGTAG = "Logtag";
    private final static String CONFIG = "vpnconfig.ovpn";
    public Fragment fragment;
    // private OpenVPNThread openVPNThread = new OpenVPNThread();
    // private OpenVPNService openVPNService = new OpenVPNService();
    public static VpnConnectionService instance;
    public static Callback callback;

    public interface Callback {
        void callingBack();
    }

    public VpnConnectionService(Fragment fragment) {
        this.fragment = fragment;
        VpnConnectionService.instance = this;
    }

    public void startVpnService() {
        ServerVpnConfiguration serverVpnConfiguration = getVpnConfiguration();
        connectToServer(serverVpnConfiguration);
    }

    public int getStatusPercent() {
        int breakPoint = 0;
        String status = OpenVPNService.getStatus();

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

    public String getStatus() {
        String status = null;
        status = OpenVPNService.getStatus();

        if (status.isEmpty()) {
            return "";
        }

        ServerStatusEnum serverStatusEnum = ServerStatusEnum.valueOf(status);

        switch (serverStatusEnum) {
            case RECONNECTING:
                return "Reconnecting";
            case CONNECTRETRY:
                return "Retry";
            case DISCONNECTED:
                return "Disconnected";
            case NONETWORK:
                return "No network";
            case NOPROCESS:
                return "No process";
            case VPN_GENERATE_CONFIG:
                return "Generate conf";
            case WAIT:
                return "Wait";
            case AUTH:
                return "Auth";
            case GET_CONFIG:
                return "Get config";
            case ASSIGN_IP:
                return "Assign ip";
            case ADD_ROUTES:
                return "Add routes";
            case CONNECTED:
                return "Connected";
        }

        return status;
    }

    public ServerVpnConfiguration getVpnConfiguration() {
        ServerVpnConfiguration serverVpnConfiguration = new ServerVpnConfiguration();
        serverVpnConfiguration.setCountry("Germany");
        serverVpnConfiguration.setUser("vpnuser");
        serverVpnConfiguration.setPassword("vpnpassword");

        String configuration = readConfiguration();
        if (configuration == null) {
            Log.d(LOGTAG, "No configuration");
        }
        serverVpnConfiguration.setConfiguration(configuration);

        return serverVpnConfiguration;
    }

    public void vpnProfileInstall(Callback callback) {
        VpnConnectionService.callback = callback;
        Intent intent = android.net.VpnService.prepare(MyApplicationContext.getAppContext());
        fragment.startActivityForResult(intent, 1);

    }

    public boolean isVpnProfileInstalled() {
        Intent intent = android.net.VpnService.prepare(MyApplicationContext.getAppContext());

        if (intent == null) {
            return true;
        }

        return false;
    }

    // Automatic start from main fragment
    public static void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            VpnConnectionService.callback.callingBack();
        } else {
            Log.d(LOGTAG, "No permission");
        }
    }

    private String readConfiguration() {
        try {
            InputStream inputStream = this.fragment.getActivity().getAssets().open(CONFIG);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String configurationText = "";
            String oneLine = "";

            while (true) {
                oneLine = bufferedReader.readLine();

                if (oneLine == null) {
                    break;
                }

                configurationText += oneLine + "\n";
            }

            bufferedReader.readLine();

            // Log.d(LOGTAG, "Configuration text: ");
            // Log.d(LOGTAG, configurationText);

            return configurationText;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void connectToServer(ServerVpnConfiguration serverVpnConfiguration) {
        Log.d(LOGTAG, "Connect");

        try {
            OpenVpnApi.startVpn(
                    fragment.getContext(),
                    serverVpnConfiguration.getConfiguration(),
                    serverVpnConfiguration.getCountry(),
                    serverVpnConfiguration.getUser(),
                    serverVpnConfiguration.getPassword()
            );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void disconnectServer() {
        OpenVPNThread.stop();
    }

    public void startForeground(int notifyId, Notification notification) {
        OpenVPNService.getInstance().startForeground(notifyId, notification);
    }

    public boolean isOpnVpnServiceCreated() {
        return OpenVPNService.getInstance() != null;
    }
}
