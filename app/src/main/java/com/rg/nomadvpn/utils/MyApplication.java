package com.rg.nomadvpn.utils;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MyApplicationContext myApplicationContext = new MyApplicationContext(this);
    }
}
