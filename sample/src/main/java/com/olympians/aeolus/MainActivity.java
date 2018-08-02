package com.olympians.aeolus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.olympians.aeolus.callback.OnAeolusCallback;
import com.olympians.aeolus.exception.AeolusException;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v -> {
            new Aeolus.Builder<LoginResponse>()
                    .addRequest(new LoginRequest())
                    .addCallback(new OnAeolusCallback<LoginResponse>() {
                        @Override
                        public void onFailure(@NotNull AeolusException exception) {

                        }

                        @Override
                        public void onSuccess(LoginResponse response) {

                        }
                    })
                    .addOnStart(() -> {

                    })
                    .addOnEnd(() -> {

                    })
                    .build();
        });
    }
}
