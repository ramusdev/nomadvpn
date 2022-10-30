package com.rg.nomadvpn.service;

import android.util.Log;
import android.view.View;

import com.rg.nomadvpn.utils.InternetConnection;

public class StartConnectService implements View.OnClickListener {
    private final static String LOGTAG = "Logtag";

    @Override
    public void onClick(View view) {
        Log.d(LOGTAG, "This is button click");

        if (InternetConnection.isConnected()) {
            Log.d(LOGTAG, "Internet is connected");
        }
    }
}
