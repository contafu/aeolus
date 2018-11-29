## `0.3.2`
- 增加 `@Strip` 注解用于剥除变量名，只传递变量值作为 json

## `0.3.1`
- 处理读流时中断异常

## `0.3.0`
- 升级 kotlin to 1.3.0
- support library 迁移到 androidX

## `0.2.7`
- 增加超时配置

## `0.2.6`
- 增加无网络时将请求处理成失败并抛出相应异常Code

## `0.2.5`
- 针对 GET 请求，参数是中文或特殊字符优化

## `0.2.4`
- 修复 kotlin module 打包提示重复的问题

## `0.2.3`
- 升级 kotlin to 1.2.71
- 升级 gradle to 4.10.2
- 升级 android gradle plugin to 3.2.0
- 升级 fastjson to 1.1.70.android
- 升级 buildToolsVersion to 28.0.3

## `0.2.2`
- 增加请求成功但业务失败的处理

## `0.2.1`
- 修复依赖包 `META-INF/library_release.kotlin_module` 重复冲突

## `0.2.0`
- 增加 json 方式 post 请求

## `0.1.2`
- 修复部分手机上 kotlin 执行 forEach 高阶函数时报 ClassNotFoundException 异常
- 升级 kotlin to 1.2.61

## `0.1.1`
- 增加支持 json 解析部分返回字段

## `0.1.0`
- 增加 @Query 注解，用于标记 Request 父类变量是否可被检索成参数

## `0.0.9`
- 增加 http code 404 的错误判断

## `0.0.8`
- 升级 gradle wrapper to 4.9
- 升级 kotlin to 1.2.60

## `0.0.7`
- 修复兼容低版本 Okhttp 没有 response.close 的调用崩溃

## `0.0.6`
- 修复连接超时不回调 bug

## `0.0.5`
- 添加 AeolusFilter 过滤器
- 添加生成 sources.jar 和 classes.jar 的 task

## `0.0.4`
- 增加 AeolusException 异常类

## `0.0.3`
- 添加可配置已存在 httpclient 和证书校验类

## `0.0.2`
- init

## `0.0.1`
- init