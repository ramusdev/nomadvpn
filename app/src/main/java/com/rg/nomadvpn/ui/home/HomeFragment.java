package com.rg.nomadvpn.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.databinding.FragmentHomeBinding;
import com.rg.nomadvpn.service.StartConnectService;

import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ButtonConnect buttonConnect = new ButtonConnect(root);

        // OpenVPNThread openVPNThread = new OpenVPNThread();
        // OpenVPNService openVPNService = new OpenVPNService();
        // OpenVPNThread.stop();

        // Button button = root.findViewById(R.id.start_connect);
        // button.setOnClickListener(new StartConnectService(this));

        /*
        Button buttonStatus = root.findViewById(R.id.start_status);
        buttonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = OpenVPNService.getStatus();
                Log.d(MainActivity.LOGTAG, "Status connection: " + status);
            }
        });
        */

        VpnStatus.initLogCache(getActivity().getCacheDir());



        // final TextView textView = binding.textHome;
        /*
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}