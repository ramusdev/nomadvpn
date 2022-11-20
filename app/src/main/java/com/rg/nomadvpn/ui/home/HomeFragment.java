package com.rg.nomadvpn.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.databinding.FragmentHomeBinding;
import com.rg.nomadvpn.service.VpnConnectionService;

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

        vpnConnectionService = new VpnConnectionService(this);

        ButtonConnect buttonConnect = new ButtonConnect();
        buttonConnect.setView(root);
        buttonConnect.setService(vpnConnectionService);
        buttonConnect.init();

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

        return root;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String duration = intent.getStringExtra("duration");
            String status = vpnConnectionService.getStatus();
            // String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
            // String byteIn = intent.getStringExtra("byteIn");
            // String byteOut = intent.getStringExtra("byteOut");
            String receiveIn = intent.getStringExtra("receiveIn");
            String receiveOut = intent.getStringExtra("receiveOut");


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

            // Log.d(MainActivity.LOGTAG, "Duration broadcast: " + duration);
            // Log.d(MainActivity.LOGTAG, "Status broadcast: " + status);
            // Log.d(MainActivity.LOGTAG, "Last packet broadcast: " + lastPacketReceive);
            // Log.d(MainActivity.LOGTAG, "Receive in broadcast: " + receiveIn);
            // Log.d(MainActivity.LOGTAG, "Receive out broadcast: " + receiveOut);
            // Log.d(MainActivity.LOGTAG, "Byte out broadcast: " + byteOut);

            homeViewModel.setDuration(duration);
            homeViewModel.setStatus(status);
            homeViewModel.setReceiveIn(receiveIn);
            homeViewModel.setReceiveOut(receiveOut);
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