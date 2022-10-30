package com.rg.nomadvpn.service;

import android.content.Intent;
import android.net.VpnService;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.rg.nomadvpn.model.ServerVpnConfiguration;
import com.rg.nomadvpn.utils.MyApplicationContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import de.blinkt.openvpn.OpenVpnApi;

public class StartConnectService implements View.OnClickListener {
    private final static String LOGTAG = "Logtag";
    private final static String CONFIG = "vpnconfig.ovpn";
    public Fragment fragment;

    public StartConnectService(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View view) {
        Log.d(LOGTAG, "Button click connect");

        ServerVpnConfiguration serverVpnConfiguration = new ServerVpnConfiguration();
        serverVpnConfiguration.setCountry("Germany");
        serverVpnConfiguration.setUser("vpnuser");
        serverVpnConfiguration.setPassword("vpnpassword");

        String configuration = readConfiguration();
        if (configuration == null) {
            Log.d(LOGTAG, "No configuration");
        }

        serverVpnConfiguration.setConfiguration(configuration);

        startVpn(serverVpnConfiguration);
    }

    private void startVpn(ServerVpnConfiguration serverVpnConfiguration) {
        if (isVpnProfile()) {
            connectToServer(serverVpnConfiguration);
        } else {
            Log.d(LOGTAG, "No vpn profile");
        }
    }

    private boolean isVpnProfile() {
        Intent intent = VpnService.prepare(MyApplicationContext.getAppContext());

        if (intent != null) {
            this.fragment.startActivityForResult(intent, 1);
            return false;
            // TODO must implement connect to server
        } else {
            return true;
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
}
