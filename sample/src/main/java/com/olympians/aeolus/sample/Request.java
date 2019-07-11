package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.annotations.Get;

@Get(host = "http://192.168.31.150:3000", api = "test")
public class Request implements AeolusRequest {

    private String tag;

    public Request(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
