# Aeolus

[![](https://jitpack.io/v/contafu/aeolus.svg)](https://jitpack.io/#kontafu/aeolus)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

## 下载

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

``` groovy
implementation 'com.github.contafu:aeolus:latest-integer'
```

## 使用

### 1、Application中初始化配置
```java
AeolusConfig.INSTANCE
            .setHost(String host)
            .addHeader(String key, String value)
            .addHeaders(Map<String, String> headers)
            .addFilter(AeolusFilter aeolusFilter)
            .setHttpClient(OkHttpClient okHttpClient)
            .setTimeout(long timeout, TimeUnit unit)
```

- `setHost(String host)` 配置全局host
- `addHeader(String key, String value)` 添加单个Header
- `addHeaders(Map<String, String> headers)` 添加多个Header
- `setHttpClient(OkHttpClient okHttpClient)` 配置本地已存在的client实例
- `setTimeout(long timeout, TimeUnit unit)` 配置超时
- `addFilter(AeolusFilter aeolusFilter)` 配置请求过滤器

<em>当配置 setHttpClient 后 setTimeout 不生效</em>

<hr>

### 2、创建Request类并实现AeolusRequest接口
```java
@Get(host = "http://localhost:80", api = "/api/login")
public class Request implements AeolusRequest {
}
```

```java
@Post(host = "http://localhost:80", api = "/api/login", contentType = ContentType_JSON)
public class Request implements AeolusRequest {

    @Strip
    private List<String> appList;

}
```

- host 为可选参数。若不设置，则取全局配置；若设置则该Request优先使用本处配置；host是否以`/`结尾都可。
- api 是否以`/`开头或结尾都可
- contentType 默认为 application/json

<em>如果Request继承于父类，则父类用 `@Query` 注解修饰，否则父类变量不会被检索添加</em>
<em> `@Strip` 注解用于修饰 `Post` 中的变量，此时当该变量作为JSON方式传递给后端时，只序列化变量值，变量名不参加序列化；一般用作传递JsonArray作为顶层的JSON格式</em>

<hr>

### 3、创建Response类
```java
public class Response {
}
```

<hr>

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

- `addOnStart()` 当请求开始时回调
- `addOnEnd()` 当请求结束时回调

<hr>

### 5、异常码
```text
    AEOLUS_CODE_OK 请求成功

    AEOLUS_CODE_JSON_ERROR 解析json异常

    AEOLUS_CODE_SOCKET_ERROR 请求超时

    AEOLUS_CODE_CONNECT_ERROR 连接超时

    AEOLUS_CODE_INTERNAL_ERROR 内部异常

    AEOLUS_CODE_UNKNOWN_HOSTNAME_ERROR 域名解析异常

    AEOLUS_CODE_IO_ERROR 流操作异常

    BUSINESS_EXCEPTION 业务异常
```

<hr>

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