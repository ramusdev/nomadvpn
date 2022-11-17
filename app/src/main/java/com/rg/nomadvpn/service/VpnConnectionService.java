package com.rg.nomadvpn.service;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.VpnService;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;

import com.rg.nomadvpn.MainActivity;
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
    // private final static String CONFIG = "server.ovpn";
    public Fragment fragment;

    private OpenVPNThread openVPNThread = new OpenVPNThread();
    private OpenVPNService openVPNService = new OpenVPNService();

    public static VpnConnectionService instance;

    public VpnConnectionService(Fragment fragment) {
        this.fragment = fragment;
        VpnConnectionService.instance = this;
    }

    public boolean startVpnService() {
        Log.d(LOGTAG, "Button click connect");



        return true;
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

    public boolean startConnection(ServerVpnConfiguration serverVpnConfiguration) {
        connectToServer(serverVpnConfiguration);
        return true;
    }

    public void vpnProfileInstall() {
        Intent intent = VpnService.prepare(MyApplicationContext.getAppContext());
        fragment.startActivityForResult(intent, 1);
    }

    public boolean isVpnProfileInstalled() {
        Intent intent = VpnService.prepare(MyApplicationContext.getAppContext());

        if (intent == null) {
            return true;
        }

        return false;

        /*
        if (intent != null) {
            Log.d(MainActivity.LOGTAG, "Before");
            this.fragment.startActivityForResult(intent, 1);
            Log.d(MainActivity.LOGTAG, "After 1");
            Log.d(MainActivity.LOGTAG, "After 2");
            return false;
            // TODO must implement connect to server
        } else {
            return true;
        }

         */
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            // VpnConnectionService.instance.startVpnService(VpnConnectionService.instance.getVpnConfiguration());

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
}
