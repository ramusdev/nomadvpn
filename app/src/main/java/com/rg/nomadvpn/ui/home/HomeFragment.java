package com.rg.nomadvpn.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.controller.ConnectionController;
import com.rg.nomadvpn.databinding.FragmentHomeBinding;
import com.rg.nomadvpn.service.NotificationService;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.utils.MyApplicationContext;

import de.blinkt.openvpn.core.VpnStatus;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private VpnConnectionService vpnConnectionService;
    private View root;
    Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Connection");

        // Connection service
        vpnConnectionService = new VpnConnectionService(this);
        ConnectionController connectionController = new ConnectionController();
        connectionController.setView(root);
        connectionController.setNotificationService(new NotificationService(vpnConnectionService));
        connectionController.setVpnService(vpnConnectionService);
        connectionController.init();











        VpnStatus.initLogCache(getActivity().getCacheDir());

        TextView durationValue = root.findViewById(R.id.value_time);
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getDuration().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                durationValue.setText(value);
            }
        });

        TextView statusValue = root.findViewById(R.id.value_status);
        homeViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                statusValue.setText(value);
                if (value.equals("Connected")) {
                    statusValue.setTextColor(getResources().getColor(R.color.status_textconnected));
                } else {
                    statusValue.setTextColor(getResources().getColor(R.color.status_text));
                }
            }
        });

        TextView receiveInValue = root.findViewById(R.id.value_receivein);
        homeViewModel.getReceiveIn().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                receiveInValue.setText(value);
            }
        });

        TextView receiveOutValue = root.findViewById(R.id.value_receiveout);
        homeViewModel.getReceiveOut().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                receiveOutValue.setText(value);
            }
        });

        TextView speedInValue = root.findViewById(R.id.value_speedin);
        homeViewModel.getSpeedIn().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                speedInValue.setText(value);
            }
        });

        TextView speedOutValue = root.findViewById(R.id.value_speedout);
        homeViewModel.getSpeedOut().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                speedOutValue.setText(value);
            }
        });

        return root;
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

            homeViewModel.setDuration(duration);
            homeViewModel.setStatus(status);
            homeViewModel.setReceiveIn(receiveIn);
            homeViewModel.setReceiveOut(receiveOut);
            homeViewModel.setSpeedIn(speedIn);
            homeViewModel.setSpeedOut(speedOut);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        VpnConnectionService.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}