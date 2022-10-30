package com.rg.nomadvpn.utils;

import android.content.Context;

public class MyApplicationContext {
    private static Context context;

    public MyApplicationContext(MyApplication myApplication) {
        MyApplicationContext.context = myApplication.getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplicationContext.context;
    }

}
