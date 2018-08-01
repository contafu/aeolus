package com.olympians.aeolus;

import android.app.Application;

import com.olympians.aeolus.config.AeolusConfig;

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AeolusConfig.INSTANCE
                .setHost("")
                .setHostnameVerifier((hostname, session) -> true);
    }
}
