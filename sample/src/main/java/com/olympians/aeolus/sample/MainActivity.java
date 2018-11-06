package com.olympians.aeolus.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.olympians.aeolus.Aeolus;
import com.olympians.aeolus.callback.OnAeolusCallback;
import com.olympians.aeolus.exception.AeolusException;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Request request = new Request();

        findViewById(R.id.button).setOnClickListener(v -> {
            new Aeolus.Builder<Response>()
                    .addRequest(request)
                    .addCallback(new OnAeolusCallback<Response>() {
                        @Override
                        public void onFailure(@NotNull AeolusException exception) {
                            Log.e("TAG", "code: " + exception.getCode());
                            Log.e("TAG", "businessCode: " + exception.getBusinessCode());
                            Log.e("TAG", "msg: " + exception.getMessage());
                        }

                        @Override
                        public void onSuccess(Response response) {
                            Log.d("TAG", "onSuccess: " + response.toString());
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
