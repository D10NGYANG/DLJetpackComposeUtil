# DLJetpackComposeUtil
jetpack compose android 项目基础框架，含封装弹窗和一些自定义UI控件
[![](https://jitpack.io/v/D10NGYANG/DLBasicJetpackComposeApp.svg)](https://jitpack.io/#D10NGYANG/DLBasicJetpackComposeApp)
# 使用
1. 添加仓库
```gradle 
allprojects {
  repositories {
    ...
    maven { url 'https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository' }
  }
}
```
2. 添加依赖

```gradle
dependencies {
    implementation 'com.github.D10NGYANG:DLJetpackComposeUtil:1.3.5'
}
```

3. 混淆
```pro
-keep class com.d10ng.compose.** {*;}
-dontwarn com.d10ng.compose.**
```
