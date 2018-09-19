package com.olympians.aeolus.sample;

public class Response {
    private int success;
    private String msg;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "success=" + success
                + ";"
                + "msg=" + msg;
    }
}
