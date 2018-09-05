package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.annotations.Get;
import com.olympians.aeolus.sample.BaseRequest;

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
