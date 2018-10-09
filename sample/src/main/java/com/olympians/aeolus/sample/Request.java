package com.olympians.aeolus.sample;

import com.olympians.aeolus.AeolusRequest;
import com.olympians.aeolus.annotations.Get;

@Get(host = "http://www.baidu.com/")
public class Request implements AeolusRequest {
}
