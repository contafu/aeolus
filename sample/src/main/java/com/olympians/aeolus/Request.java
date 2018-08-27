package com.olympians.aeolus;

import com.olympians.aeolus.annotations.Get;

@Get(host = "http://192.168.1.20", api = "/mobile/app-name")
public class Request implements AeolusRequest {
}
