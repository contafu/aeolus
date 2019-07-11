package com.olympians.aeolus.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.olympians.aeolus.Aeolus;
import com.olympians.aeolus.callback.OnAeolusCallback;
import com.olympians.aeolus.exception.AeolusException;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.request).setOnClickListener(v -> {
            for (int i = 0; i < 50; i++) {
                request(String.valueOf(i));
            }
        });
    }

    private void request(String tag) {
        new Aeolus.Builder<Response>()
                .addRequest(new Request(tag))
                .addCallback(new OnAeolusCallback<Response>() {

                    @Override
                    public void onFailure(@NotNull AeolusException exception) {

                    }

                    @Override
                    public void onSuccess(Response response) {

                    }
                })
                .build();
    }
}
