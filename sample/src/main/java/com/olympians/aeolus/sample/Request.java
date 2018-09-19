package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.annotations.Get;

@Get(host = "http://192.168.43.64:3000")
public class Request implements AeolusRequest {
}
