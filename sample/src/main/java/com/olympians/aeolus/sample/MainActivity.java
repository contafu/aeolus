package com.olympians.aeolus.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

        List<Request.DataBean> appName = new ArrayList<>();

        Request.DataBean db1 = new Request.DataBean();
        db1.setAge(20);
        db1.setName("Google");
        appName.add(db1);

        Request.DataBean db2 = new Request.DataBean();
        db2.setAge(25);
        db2.setName("Android");
        appName.add(db2);

        Request.DataBean db3 = new Request.DataBean();
        db3.setAge(30);
        db3.setName("Microsoft");
        appName.add(db3);

        Request request = new Request();
        request.setAppName(appName);
        request.setMsg("成功");

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

        findViewById(R.id.next).setOnClickListener(v -> startActivity(new Intent(this, UploadFileActivity.class)));
        findViewById(R.id.coroutine).setOnClickListener(v -> startActivity(new Intent(this, CoroutineTestActivity.class)));
        findViewById(R.id.thread_pool).setOnClickListener(v -> startActivity(new Intent(this, ThreadPoolActivity.class)));
    }
}
