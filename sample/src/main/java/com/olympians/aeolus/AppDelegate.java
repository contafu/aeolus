package com.olympians.aeolus;

import android.app.Application;
import android.util.Log;

import com.olympians.aeolus.config.AeolusConfig;

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AeolusConfig.INSTANCE
                .addFilter(body -> {
                    Log.d("TAG", "body: " + body);
                    return body;
                });
    }
}
