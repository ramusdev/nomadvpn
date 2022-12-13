package com.rg.nomadvpn.db;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import com.rg.nomadvpn.utils.MyApplicationContext;

public class ServerCurrent {
    private static final String PREFERRNCE_NAME = "server_name";
    private static final String PREFERRNCE_SERVER = "server_name";

    public static int getServerIndex() {
        int serverIndex = getSharedPreferencesInstance().getInt(PREFERRNCE_SERVER, 0);
        return serverIndex;
    }

    public static void setServerId(int id) {
        Editor editor = getSharedPreferencesInstance().edit();
        editor.putInt(PREFERRNCE_SERVER, id);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferencesInstance() {
        return MyApplicationContext.getAppContext().getSharedPreferences(PREFERRNCE_NAME, Context.MODE_PRIVATE);
    }
}
