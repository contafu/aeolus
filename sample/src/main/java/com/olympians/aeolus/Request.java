package com.olympians.aeolus;

import com.olympians.aeolus.annotations.Get;

@Get(host = "http://192.168.1.20", api = "/mobile/app-name")
public class Request extends BaseRequest implements AeolusRequest {

    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
