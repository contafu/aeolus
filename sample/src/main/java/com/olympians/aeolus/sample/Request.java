package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.annotations.Post;

import java.util.List;

import static com.olympians.aeolus.ContentTypeKt.ContentType_JSON;

@Post(host = "http://10.49.3.34:8080", api = "ctuom-web/mobile/update-my-apps", contentType = ContentType_JSON)
public class Request implements AeolusRequest {

    private List<String> appName;

    public List<String> getAppName() {
        return appName;
    }

    public void setAppName(List<String> appName) {
        this.appName = appName;
    }
}
