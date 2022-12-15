package com.rg.nomadvpn.service;

import static android.app.Activity.RESULT_OK;
import android.app.Notification;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.locator.ServiceBase;
import com.rg.nomadvpn.model.ServerStatusEnum;
import com.rg.nomadvpn.model.ServerHolderConfiguration;
import com.rg.nomadvpn.utils.MyApplicationContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;

public class VpnConnectionService extends ServiceBase {
    private final static String LOGTAG = "Logtag";
    private final static String CONFIG = "config_germany.ovpn";
    public Fragment fragment;
    // private OpenVPNThread openVPNThread = new OpenVPNThread();
    // private OpenVPNService openVPNService = new OpenVPNService();
    public static VpnConnectionService instance;
    public static Callback callback;

    public interface Callback {
        void callingBack();
    }

    public VpnConnectionService() {
        VpnConnectionService.instance = this;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void startVpnService(ServerHolderConfiguration serverHolderConfiguration) {
        // ServerHolderConfiguration serverHolderConfiguration = getVpnConfiguration();
        connectToServer(serverHolderConfiguration);
    }

    public int getStatusPercent() {
        int breakPoint = 0;
        String status = OpenVPNService.getStatus();

        if (status.isEmpty()) {
            return breakPoint;
        }

        ServerStatusEnum serverStatusEnum = ServerStatusEnum.valueOf(status);

        switch (serverStatusEnum) {
            case EXITING:
                return breakPoint = 0;
            case CONNECTRETRY:
                return breakPoint = 0;
            case DISCONNECTED:
                return breakPoint = 0;
            case NONETWORK:
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
            case EXITING:
                return "Exiting";
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

    public String getStatusName() {
        String status = null;
        int statusNameInt = 0;
        status = OpenVPNService.getStatus();

        if (status.isEmpty()) {
            return "";
        }

        ServerStatusEnum serverStatusEnum = ServerStatusEnum.valueOf(status);

        switch (serverStatusEnum) {
            case EXITING:
                statusNameInt = R.string.exiting_status;
                break;
            case RECONNECTING:
                statusNameInt = R.string.reconnecting_status;
                break;
            case CONNECTRETRY:
                statusNameInt = R.string.retry_status;
                break;
            case DISCONNECTED:
                statusNameInt = R.string.disconnected_status;
                break;
            case NONETWORK:
                statusNameInt = R.string.nonetwork_status;
                break;
            case NOPROCESS:
                statusNameInt = R.string.noprocess_status;
                break;
            case VPN_GENERATE_CONFIG:
                statusNameInt = R.string.generateconf_status;
                break;
            case WAIT:
                statusNameInt = R.string.wait_status;
                break;
            case AUTH:
                statusNameInt = R.string.auth_status;
                break;
            case GET_CONFIG:
                statusNameInt = R.string.getconfig_status;
                break;
            case ASSIGN_IP:
                statusNameInt = R.string.assignip_status;
                break;
            case ADD_ROUTES:
                statusNameInt = R.string.addroutes_status;
                break;
            case CONNECTED:
                statusNameInt = R.string.connected_status;
                break;
        }

        String statusName = MyApplicationContext.getAppContext().getResources().getString(statusNameInt);

        return statusName;
    }

    public String getVpnConfiguration(String fileName) {
        String stringConfiguration = readConfiguration(fileName);
        if (stringConfiguration.isEmpty()) {
            Log.d(LOGTAG, "No configuration");
        }

        return stringConfiguration;
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

    private String readConfiguration(String fileName) {
        try {
            InputStream inputStream = this.fragment.getActivity().getAssets().open(fileName);
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

    private void connectToServer(ServerHolderConfiguration serverHolderConfiguration) {
        Log.d(MainActivity.LOGTAG, "Connect -------------------->");
        Log.d(MainActivity.LOGTAG, "Connect conf: " + serverHolderConfiguration.getFileName());

        String configuration = getVpnConfiguration(serverHolderConfiguration.getFileName());
        try {
            OpenVpnApi.startVpn(
                    fragment.getContext(),
                    configuration,
                    serverHolderConfiguration.getCountry(),
                    serverHolderConfiguration.getUser(),
                    serverHolderConfiguration.getPassword()
            );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void disconnectServer() {
        OpenVPNThread.stop();
    }

    /*
    public static void disconnectServerStatic() {
        OpenVPNThread.stop();
    }
    */

    public void startForeground(int notifyId, Notification notification) {
        OpenVPNService.getInstance().startForeground(notifyId, notification);
    }

    public void stopForeground() {
        OpenVPNService.getInstance().stopForeground(false);
    }

    public boolean isOpnVpnServiceCreated() {
        return OpenVPNService.getInstance() != null;
    }

    public boolean isOpnVpnServiceConnected() {
        String status = this.getStatus();
        if (status.equals("Connected")) {
            return true;
        }
        return false;
    }
}
