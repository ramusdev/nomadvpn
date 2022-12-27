package com.rg.nomadvpn.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.rg.nomadvpn.MainActivity;

import java.util.Date;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    // private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private static final String AD_UNIT_ID = "ca-app-pub-9449302163805983/6255335472";
    private final int TIME_FOR_AD_LOAD = 10000;
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private final MyApplication myApplication;
    private Activity currentActivity;
    private static boolean isShowingAd = false;
    private long loadTime = 0;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_DATE = "date";
    public static final String APP_PREFERENCES_ISHOW = "isshow";

    public AppOpenManager(MyApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    // Request an add
    public void fetchAd() {
        if (isAdAvailable()) {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(AppOpenAd ad) {
                AppOpenManager.this.appOpenAd = ad;
                AppOpenManager.this.loadTime = (new Date()).getTime();
                AppOpenManager.this.showAdIfAvailable();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.d(MainActivity.LOGTAG, "AppOpenManager: Ad failed to load");
            }
        };

        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication, AD_UNIT_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    // Show ad
    public void showAdIfAvailable() {
        Log.d(MainActivity.LOGTAG, "AppOpenManager: Show ad");
        if (!isShowingAd && isAdAvailable() && isInterstitialAllowed()) {
            Log.d(MainActivity.LOGTAG, "AppOpenManager: Show ad inside");

            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    AppOpenManager.this.appOpenAd = null;
                    isShowingAd = false;
                    // fetchAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {

                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                }
            };

            // appOpenAd.show(currentActivity, fullScreenContentCallback);
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);

        } else {
            Log.d(MainActivity.LOGTAG, "AppOpenManager: Can not show ad");
            // fetchAd();
        }
    }

    public boolean isInterstitialAllowed() {
        SharedPreferences sharedPreferences = MyApplicationContext.getAppContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        long dateTime = sharedPreferences.getLong(APP_PREFERENCES_DATE, 0);
        long currentDateTime = (new Date()).getTime();
        long dateDifference = currentDateTime - dateTime;
        long numMilliSecondsPerHour = 3600000;
        long hoursInDay = 24;
        long numMilliSecondsInDay = numMilliSecondsPerHour * hoursInDay;

        return dateDifference > numMilliSecondsInDay;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d(MainActivity.LOGTAG, "AppOpenManager: On start");

        if (isAdAvailable()) {
            showAdIfAvailable();
        } else {
            fetchAd();
        }
    }

    // Creates and returns ad request
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    // Utility that checks if ad exists and can be shown
    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanHoursAgo(1);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }
}
