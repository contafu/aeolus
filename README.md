# Aeolus

[![](https://jitpack.io/v/kontafu/aeolus.svg)](https://jitpack.io/#kontafu/aeolus)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

## Download

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

``` groovy
implementation 'com.github.kontafu:aeolus:latest-integer'
```

### 初始化配置
```
    AeolusConfig.INSTANCE
        .setHost(String host)
        .addHeader(String key, String value)
        .addHeaders(Map<String, String> headers)
        .setHttpClient(OkHttpClient okHttpClient)
        .setHostnameVerifier(HostnameVerifier hostnameVerifier);
```

### 创建Request类并实现AeolusRequest接口
```java
@Get(host = "", api = "")
public class Request implements AeolusRequest {
}
```

### 创建Response类
```java
public class Response {
}
```

### 调用请求
```java
    new Aeolus.Builder<Response>()
        .addRequest(new Request())
        .addOnStart(() -> {
            
        })
        .addCallback(new OnAeolusCallback<Response>() {
            @Override
            public void onFailure(@NotNull AeolusException exception) {

            }

            @Override
            public void onSuccess(Response response) {

            }
        })
        .addOnEnd(() -> {

        })
        .build();
```

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.