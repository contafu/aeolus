# Aeolus

[![](https://jitpack.io/v/kontafu/aeolus.svg)](https://jitpack.io/#kontafu/aeolus)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

## 下载

下载 [Jar][1] 或:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

``` groovy
implementation 'com.github.kontafu:aeolus:latest-integer'
```

## 使用

### 1、Application中初始化配置
```
    AeolusConfig.INSTANCE
                .setHost(String host)
                .addHeader(String key, String value)
                .addHeaders(Map<String, String> headers)
                .setHttpClient(OkHttpClient okHttpClient)
                .setHostnameVerifier(HostnameVerifier hostnameVerifier);
```

- `setHost(String host)`配置全局host
- `addHeader(String key, String value)`添加单个Header
- `addHeaders(Map<String, String> headers)`添加多个Header
- `setHttpClient(OkHttpClient okHttpClient)`配置本地已存在的client实例
- `setHostnameVerifier(HostnameVerifier hostnameVerifier`)配置https证书认证

### 2、创建Request类并实现AeolusRequest接口
```java
@Get(host = "http://localhost:80", api = "/api/login")
public class Request implements AeolusRequest {
}
```

- host为可选参数。若不设置，则取全局配置；若设置则该Request优先使用本处配置；host是否以`/`结尾都可。
- api是否以`/`开头或结尾都可

### 3、创建Response类
```java
public class Response {
}
```

### 4、调用请求
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

- `addOnStart()`当请求开始时回调
- `addOnEnd()`当请求结束时回调，无论请求成功与否

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
    
    [1]:https://github-production-release-asset-2e65be.s3.amazonaws.com/142985379/8b1858e2-9691-11e8-9170-4104978c3dd1?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20180802%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20180802T122123Z&X-Amz-Expires=300&X-Amz-Signature=03e0f9f29b6cf7e57d5711160a748ed2544a71a0036eed59020daa98bedaa999&X-Amz-SignedHeaders=host&actor_id=6076731&response-content-disposition=attachment%3B%20filename%3Daeolus-0.0.4.jar&response-content-type=application%2Foctet-stream