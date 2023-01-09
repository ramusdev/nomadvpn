package com.rg.nomadvpn.service;

import android.animation.ValueAnimator;
import android.media.MediaCodec;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.util.Log;

import com.rg.nomadvpn.MainActivity;
import com.rg.nomadvpn.R;
import com.rg.nomadvpn.db.ServerHolder;
import com.rg.nomadvpn.locator.ServiceBase;
import com.rg.nomadvpn.model.ServerHolderConfiguration;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingServerService extends ServiceBase {

    public PingServerService() {

    }

    public void updateServerPing() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pingServers();
            }
        }).start();
    }

    private void pingServers() {
        ServerHolder serverHolder = ServerHolder.getInstance();
        ArrayList<ServerHolderConfiguration> serverList = serverHolder.getServerList();

        for (ServerHolderConfiguration server : serverList) {
            String ping = ping(server.getIp());
            server.setPing(ping);
        }

        /*
        for (ServerHolderConfiguration server : serverList) {
            Log.d(MainActivity.LOGTAG, "Server ping: ----------------------->");
            Log.d(MainActivity.LOGTAG, server.getPing());
        }
        */
    }

    private String ping(String serverIp) {
        String res = "";
        String pingTime = "";
        // String ms = MyApplicationContext.getAppContext().getResources().getString(R.string.ping_ms);

        try {
            Runtime runtime = Runtime.getRuntime();
            String command = "/system/bin/ping -c 3 " + serverIp;
            // Process ipProcess = runtime.exec("/system/bin/ping -c 1 51.89.112.101");
            Process ipProcess = runtime.exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ipProcess.getInputStream()));
            boolean exitValue = ipProcess.waitFor(5000, TimeUnit.MILLISECONDS);

            if(exitValue){
                String s = "";
                while((s = bufferedReader.readLine()) != null) {
                    res += s + "\n";
                }
                pingTime = parseResults(res);
            } else {
                pingTime = "550";
            }

            ipProcess.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Log.d(MainActivity.LOGTAG, "Ping server exception --------------------->");
            Log.d(MainActivity.LOGTAG, e.getMessage());
        }

        return pingTime;
    }

    private String parseResults(String results) {
        String res = new String(results);
        // Log.d(MainActivity.LOGTAG, "Ping --------------------------------------------->");
        // Log.d(MainActivity.LOGTAG, results);

        // Pattern pattern = Pattern.compile("(.*?)time=([0-9]*)\\sms(.*?)", Pattern.DOTALL);
        Pattern pattern = Pattern.compile("(.*?)=\\s[0-9/.]*/([0-9]*)\\.[0-9]*/[0-9/.]*/[0-9/.]*(.*?)", Pattern.DOTALL);
        // Pattern pattern = Pattern.compile("/(PING)/", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(res);

        /*
        if (matcher.matches()) {
            Log.d(MainActivity.LOGTAG, "String pattern -------------------------------->");
            Log.d(MainActivity.LOGTAG, String.valueOf(matcher.group(2)));
        }
        */

        if (matcher.matches()) {
            return matcher.group(2);
        }

        return "";
    }
}
