package com.rg.nomadvpn.utils;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rg.nomadvpn.MainActivity;

public class MyApplication extends Application {
    private AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();

        MyApplicationContext myApplicationContext = new MyApplicationContext(this);

        MobileAds.initialize(MyApplicationContext.getAppContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                appOpenManager = new AppOpenManager(MyApplication.this);
            }
        });
    }
}
