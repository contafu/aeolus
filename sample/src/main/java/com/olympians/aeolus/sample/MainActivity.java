package com.olympians.aeolus.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.olympians.aeolus.Aeolus;
import com.olympians.aeolus.callback.OnAeolusCallback;
import com.olympians.aeolus.exception.AeolusException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("C++");
        arrayList.add("Python");
        Request request = new Request();
        request.setAppName(arrayList);

        findViewById(R.id.button).setOnClickListener(v -> {
            new Aeolus.Builder<Response>()
                    .addRequest(request)
                    .addCallback(new OnAeolusCallback<Response>() {
                        @Override
                        public void onFailure(@NotNull AeolusException exception) {
                            Log.e("TAG", "onFailure: ");
                        }

                        @Override
                        public void onSuccess(Response response) {
                            Log.d("TAG", "onSuccess: ");
                        }
                    })
                    .addOnStart(() -> {
                        Log.d("TAG", "addOnStart: ");
                    })
                    .addOnEnd(() -> {
                        Log.d("TAG", "addOnEnd: ");
                    })
                    .build();
        });
    }
}
