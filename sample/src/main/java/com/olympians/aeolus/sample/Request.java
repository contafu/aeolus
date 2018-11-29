package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.ContentTypeKt;
import com.olympians.aeolus.annotations.Post;
import com.olympians.aeolus.annotations.Strip;

import java.util.List;

@Post(host = "http://192.168.43.64:3000", api = "/mobile/test", contentType = ContentTypeKt.ContentType_JSON)
public class Request implements AeolusRequest {

    @Strip
    private List<DataBean> appName;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getAppName() {
        return appName;
    }

    public void setAppName(List<DataBean> appName) {
        this.appName = appName;
    }

    public static class DataBean {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
