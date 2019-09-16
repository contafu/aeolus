package com.olympians.aeolus.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.contafu.Aeolus;
import com.contafu.callback.OnAeolusCallback;
import com.contafu.exception.AeolusException;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.coroutine).setOnClickListener(v -> {
            new Aeolus.Builder<Response>()
                    .addRequest(new Request(""))
                    .addCallback(new OnAeolusCallback<Response>() {
                        @Override
                        public void onSuccess(Response response) {

                        }

                        @Override
                        public void onFailure(@NotNull AeolusException exception) {

                        }
                    })
                    .launch();

        });

    }
}
