package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.annotations.Post;

import java.util.List;

import static com.olympians.aeolus.ContentTypeKt.ContentType_JSON;

@Post(host = "http://192.168.1.10:3000", api = "test/submit", contentType = ContentType_JSON)
public class Request implements AeolusRequest {

    private List<String> appName;

    public List<String> getAppName() {
        return appName;
    }

    public void setAppName(List<String> appName) {
        this.appName = appName;
    }
}
