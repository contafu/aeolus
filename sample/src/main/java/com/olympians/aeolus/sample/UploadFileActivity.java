package com.olympians.aeolus.sample;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.olympians.aeolus.Aeolus;
import com.olympians.aeolus.callback.OnAeolusCallback;
import com.olympians.aeolus.exception.AeolusException;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadFileActivity extends AppCompatActivity {

    public static final int FILE_SELECT_CODE = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        findViewById(R.id.activity_upload_file_select_file_btn).setOnClickListener(v -> selectFile());
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
    }

    private void upload(File file) {
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(30000, TimeUnit.SECONDS)
                .readTimeout(30000, TimeUnit.SECONDS)
                .connectTimeout(30000, TimeUnit.SECONDS)
                .callTimeout(30000, TimeUnit.SECONDS)
                .build();

        MultipartBody formBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
                .addFormDataPart("uid", "48")
                .addFormDataPart("pigeonId", "16")
                .addFormDataPart("type", "1")
                .setType(Objects.requireNonNull(MediaType.parse("multipart/form-data")))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.15:3700/common/upload")
                .post(formBody)
                .build();

        runOnUiThread(() -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("TAG", "onFailure: ");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("TAG", "onResponse: ");
                }
            });
        });
    }

    private void uploadWithAeolus(File file) {
        UploadRequest request = new UploadRequest();
        request.setImageFile(file);
        request.setPigeonId(27);
        request.setUid(48);
        request.setType(1);

        new Aeolus.Builder<UploadResponse>()
                .addRequest(request)
                .addCallback(new OnAeolusCallback<UploadResponse>() {
                    @Override
                    public void onSuccess(@Nullable UploadResponse response) {
                        Log.d("TAG", "onSuccess: ");
                    }

                    @Override
                    public void onFailure(@NotNull AeolusException exception) {
                        Log.e("TAG", "onFailure: ");
                    }
                })
                .build();
    }

    private String getRealPath(@Nullable Uri uri) {
        if (null != uri) {
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                try (Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null)) {
                    assert cursor != null;
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        String path = cursor.getString(columnIndex);
                        return path;
                    }
//                    String[] columnNames = cursor.getColumnNames();
//                    if (null != columnNames && 0 != columnNames.length && cursor.moveToFirst()) {
//                        for (String columnName : columnNames) {
//                            int columnIndex = cursor.getColumnIndex(columnName);
//                            String name = cursor.getString(columnIndex);
//
//                            Log.d("TAG", String.format("%s - %d - %s", columnName, columnIndex, name));
//                        }
//                    }
                } catch (Exception e) {
                    return "";
                }
            }
            return "";
        } else {
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (FILE_SELECT_CODE == requestCode) {
                if (null != data) {
                    String realPath = getRealPath(data.getData());
                    if (!TextUtils.isEmpty(realPath)) {
                        File file = new File(realPath);
                        if (file.exists()) {
//                            upload(file);
                            uploadWithAeolus(file);
                        }
                    }
                }
            }
        } else if (Activity.RESULT_CANCELED == resultCode) {
            Toast.makeText(this, "Operation canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
