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

        return root;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String duration = intent.getStringExtra("duration");
            String status = vpnConnectionService.getStatus();

            if (duration == null) {
                duration = "00:00:00";
            }

            if (status == null) {
                status = "Disconnected";
            }

            // Log.d(MainActivity.LOGTAG, "Duration broadcast: " + duration);
            Log.d(MainActivity.LOGTAG, "Status broadcast: " + status);

            homeViewModel.setDuration(duration);
            homeViewModel.setStatus(status);
        }
    };

    public void updateViewInformation(String duration) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView titleDuration = root.findViewById(R.id.value_time);
                titleDuration.setText(duration);
            }
        });
    }

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