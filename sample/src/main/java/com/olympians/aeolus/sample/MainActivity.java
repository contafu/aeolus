package com.olympians.aeolus.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.contafu.Aeolus;
import com.olympians.aeolus.callback.OnAeolusCallback;
import com.olympians.aeolus.exception.AeolusException;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.coroutine).setOnClickListener(v -> {
            for (int i = 0; i < 50; i++) {
                request(String.valueOf(i));
            }
        });

        findViewById(R.id.coroutine).setOnClickListener(v -> startActivity(new Intent(this, CoroutineTestActivity.class)));
        findViewById(R.id.thread_pool).setOnClickListener(v -> startActivity(new Intent(this, ThreadPoolActivity.class)));
    }

    private void request(String tag) {

    }
}
