package com.olympians.aeolus;

import com.olympians.aeolus.annotations.Get;

@Get(host = "http://www.baidu.com/")
public class Request extends BaseRequest implements AeolusRequest {

    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
