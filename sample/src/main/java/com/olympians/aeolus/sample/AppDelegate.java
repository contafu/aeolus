package com.olympians.aeolus.sample;

import android.app.Application;
import android.util.Log;

import com.olympians.aeolus.config.AeolusConfig;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AeolusConfig.INSTANCE
                .addFilter((url, body) -> {
                    Log.d("TAG", body);
                    return body;
                })
                .setTimeout(30L, TimeUnit.SECONDS);
    }
}


