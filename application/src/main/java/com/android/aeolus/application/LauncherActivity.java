package com.android.aeolus.application;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

public class LauncherActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        textView = findViewById(R.id.text_view);
        main();
    }

    public void main() {
        Utils.INSTANCE.request(new Function2<CoroutineScope, Continuation<? super String>, String>() {
            @Override
            public String invoke(CoroutineScope coroutineScope, Continuation<? super String> continuation) {
                try {
                    Thread.sleep(3000L);
                    return "Hello World";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "";
            }
        }, new Function2<String, Continuation<? super Unit>, String>() {
            @Override
            public String invoke(String s, Continuation<? super Unit> continuation) {
                textView.setText(s);
                return null;
            }
        });
    }
}
