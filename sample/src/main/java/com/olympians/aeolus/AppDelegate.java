package com.olympians.aeolus;

import android.app.Application;

import com.olympians.aeolus.config.AeolusConfig;
import com.olympians.aeolus.config.AeolusFilter;

import org.jetbrains.annotations.Nullable;

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AeolusConfig.INSTANCE.setHost("http://www.baidu.com/")
                .addFilter(new AeolusFilter() {
                    @Nullable
                    @Override
                    public String filter(@Nullable String body) {
                        return null;
                    }
                });
    }
}
