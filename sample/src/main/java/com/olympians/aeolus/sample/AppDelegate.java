package com.olympians.aeolus.sample;

import android.app.Application;
import android.util.Log;

import com.olympians.aeolus.config.AeolusConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AeolusConfig.INSTANCE
                .addFilter((url, body) -> {
                    Log.d("TAG", body);
                    return body;
                })
                .setTimeout(45L, TimeUnit.SECONDS)
                .setHttpClient(new OkHttpClient.Builder().build())
                .addHeader("Cookie", "JSESSIONID=B7A9689A7AE46A02FC0A600A644A3CE4");
    }
}


