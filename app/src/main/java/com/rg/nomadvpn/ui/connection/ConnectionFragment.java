package com.rg.nomadvpn.ui.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.controller.ConnectionController;
import com.rg.nomadvpn.service.NotificationService;
import com.rg.nomadvpn.service.VpnConnectionService;
import de.blinkt.openvpn.core.VpnStatus;

public class ConnectionFragment extends Fragment {

    private ConnectionViewModel connectionViewModel;
    private VpnConnectionService vpnConnectionService;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.connectionViewModel = new ViewModelProvider(this).get(ConnectionViewModel.class);
        view = inflater.inflate(R.layout.fragment_connection, container, false);

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Connection");

        // Connection service
        vpnConnectionService = new VpnConnectionService(this);
        ConnectionController connectionController = new ConnectionController();
        connectionController.setView(view);
        connectionController.setFragment(this);
        connectionController.setNotificationService(new NotificationService(vpnConnectionService));
        connectionController.setVpnService(vpnConnectionService);
        connectionController.init();

        // VpnStatus.initLogCache(getActivity().getCacheDir());

        return view;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String duration = intent.getStringExtra("duration");
            String status = vpnConnectionService.getStatus();
            String receiveIn = intent.getStringExtra("receiveIn");
            String receiveOut = intent.getStringExtra("receiveOut");
            String speedIn = intent.getStringExtra("speedIn");
            String speedOut = intent.getStringExtra("speedOut");

            if (duration == null) {
                duration = "00:00:00";
            }

            if (status == null) {
                status = "Disconnected";
            }

            if (receiveIn == null) {
                receiveIn = "0 MB";
            }

            if (receiveOut == null) {
                receiveOut = "0 MB";
            }

            if (speedIn == null) {
                speedIn = "0 Mbit/s";
            }

            if (speedOut == null) {
                speedOut = "0 Mbit/s";
            }

            // Log.d(MainActivity.LOGTAG, "Speed out: " + speedOut);

            connectionViewModel.setDuration(duration);
            connectionViewModel.setStatus(status);
            connectionViewModel.setReceiveIn(receiveIn);
            connectionViewModel.setReceiveOut(receiveOut);
            connectionViewModel.setSpeedIn(speedIn);
            connectionViewModel.setSpeedOut(speedOut);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ConnectionController.onResume(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        VpnConnectionService.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}