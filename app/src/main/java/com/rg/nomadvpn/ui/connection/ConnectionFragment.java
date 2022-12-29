package com.rg.nomadvpn.ui.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.controller.ConnectionController;
import com.rg.nomadvpn.locator.ServiceLocator;
import com.rg.nomadvpn.service.NotificationService;
import com.rg.nomadvpn.service.VpnConnectionService;
import com.rg.nomadvpn.utils.MyApplicationContext;

import de.blinkt.openvpn.core.VpnStatus;

public class ConnectionFragment extends Fragment {

    private ConnectionViewModel connectionViewModel;
    private VpnConnectionService vpnConnectionService;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.connectionViewModel = new ViewModelProvider(this).get(ConnectionViewModel.class);
        view = inflater.inflate(R.layout.fragment_speed, container, false);

        // Toolbar
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(MyApplicationContext.getAppContext().getResources().getString(R.string.menu_connection));

        // Connection service
        ConnectionController connectionController = new ConnectionController();
        connectionController.setView(view);
        connectionController.setFragment(this);
        connectionController.init();

        // VpnStatus.initLogCache(getActivity().getCacheDir());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionController.onResume(this);

        NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_connection);
        menuItem.setChecked(true);
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