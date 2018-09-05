package com.olympians.aeolus.sample;

import android.app.Application;

import com.olympians.aeolus.config.AeolusConfig;

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AeolusConfig.INSTANCE
                .addFilter((url, body) -> body);
    }
}
