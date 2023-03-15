# DLBasicJetpackComposeApp
jetpack compose android 项目基础框架，含封装弹窗
[![](https://jitpack.io/v/D10NGYANG/DLBasicJetpackComposeApp.svg)](https://jitpack.io/#D10NGYANG/DLBasicJetpackComposeApp)
# 使用
1. 添加 jitpack.io 仓库
```gradle 
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
2. 添加依赖

```gradle
dependencies {
    implementation 'com.github.D10NGYANG:DLBasicJetpackComposeApp:1.3.0'
}
```

3. 混淆
```pro
-keep class com.d10ng.compose.** {*;}
-dontwarn com.d10ng.compose.**
```
