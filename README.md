# DLJetpackComposeUtil

> 参考 [Vant4](https://vant-contrib.gitee.io/vant/#/zh-CN/home)

# 特性

> 下方支持程度图标说明
> - ✅ 完全支持
> - ⚠️ 部分支持
> - ℹ️ 计划实现，但暂时未实现
> - 🚫 不考虑实现（原生已经有更好的）

| 类型   | 组件                | 支持程度 | 备注                                                                                                          |
|:-----|:------------------|:-----|:------------------------------------------------------------------------------------------------------------|
| 基础组件 | Button 按钮         | ⚠️   | 暂不支持渐变色背景、动画按钮                                                                                              |
|      | Cell 单元格          | ✅    |                                                                                                             |
|      | Toast 轻提示         | ✅    |                                                                                                             |
| 表单组件 | Calendar 日历       | ℹ️   | TODO                                                                                                        |
|      | Cascader 级联选择     | ℹ️   | TODO                                                                                                        |
|      | Checkbox 复选框      | ✅    |                                                                                                             |
|      | Field 输入框         | ✅    |                                                                                                             |
|      | Picker 选择器        | ✅    |                                                                                                             |
|      | PickerGroup 选择器组  | ℹ️   | TODO                                                                                                        |
|      | Search 搜索         | ✅    |                                                                                                             |
|      | Slider 滑块         | ℹ️   | TODO                                                                                                        |
|      | Stepper 步进器       | ✅    |                                                                                                             |
|      | Switch 开关         | ✅    |                                                                                                             |
|      | CheckButton 选择按钮  | ✅    |                                                                                                             |
| 反馈组件 | Sheet 动作面板        | ✅    |                                                                                                             |
|      | Dialog 弹出框        | ✅    |                                                                                                             |
|      | DropdownMenu 下拉菜单 | ℹ️   | TODO                                                                                                        |
|      | Loading 加载        | ✅    |                                                                                                             |
|      | Notify 消息提示       | ✅    |                                                                                                             |
|      | Overlay 遮罩层       | ✅    |                                                                                                             |
|      | ShareSheet 分享面板   | ℹ️   | TODO                                                                                                        |
|      | SwipeCell 滑动单元格   | ℹ️   | TODO                                                                                                        |
|      | PullRefresh 下拉刷新  | ✅    |                                                                                                             |
| 展示组件 | Badge 徽标          | ✅    |                                                                                                             |
|      | Collapse 折叠面板     | ℹ️   | TODO                                                                                                        |
|      | CountDown 倒计时     | ℹ️   | TODO                                                                                                        |
|      | Divider 分割线       | 🚫   | 不考虑二次封装，建议选择原生                                                                                              |
|      | Empty 空状态         | ℹ️   | TODO                                                                                                        |
|      | ImagePreview 图片预览 | ⚠️   | ~~目前仅支持单张本地图片展示，拖拽效果需要进一步优化~~ 发现做得更好的作品，[jvziyaoyao/ImageViewer](https://github.com/jvziyaoyao/ImageViewer) |
|      | NoticeBar 通知栏     | ℹ️   | TODO                                                                                                        |
|      | Popover 气泡弹出框     | ⚠️   | 支持自动识别弹出方向（上下），以及靠左、靠右、居中，深色浅色风格，更多特性如"水平排列、显示图标、禁用选项"可自行在此基础上实现                                            |
|      | Skeleton 骨架屏      | ℹ️   | TODO                                                                                                        |
|      | Steps 步骤条         | ⚠️   | 暂不支持自定义样式、竖向步骤条                                                                                             |
|      | Swipe 轮播          | ℹ️   | TODO                                                                                                        |
|      | Tag 标签            | ✅    |                                                                                                             |
|      | Avatar 头像         | ✅    |                                                                                                             |
| 导航组件 | ActionBar 动作栏     | 🚫   |                                                                                                             |
|      | BackTop 回到顶部      | 🚫   |                                                                                                             |
|      | Grid 宫格           | 🚫   |                                                                                                             |
|      | IndexBar 索引栏      | ✅    |                                                                                                             |
|      | NavBar 导航栏        | ✅    |                                                                                                             |
|      | Pagination 分页     | ℹ️   | TODO                                                                                                        |
|      | Sidebar 侧边导航      | ℹ️   | TODO                                                                                                        |
|      | Tab 标签页           | ℹ️   | TODO                                                                                                        |
|      | Tabbar 标签栏        | ℹ️   | TODO                                                                                                        |
|      | TreeSelect 分类选择   | ℹ️   | TODO                                                                                                        |

# 预览

打开[https://d10ngyang.github.io/DLJetpackComposeUtil/](https://d10ngyang.github.io/DLJetpackComposeUtil/)进行查看

# 使用

> - 最新版本 *ver = `3.0.0-RC01`*
> - 匹配版本 *kotlin = `2.0.20`*

## Multiplatform
从`3.0.0-beta01`开始，当前工程转换成`kotlin multiplatform`架构，支持`Android`、`iOS`平台。下面的文档未更新

1 添加仓库

```gradle 
allprojects {
  repositories {
    ...
    maven { url 'https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository' }
  }
}
``` 

2 添加依赖

```gradle
dependencies {
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:$ver")

    // 可选：Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    // 可选：Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0-RC.2")

    // 可选：导航路由
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.10.2")

    // 可选：时间工具
    implementation("com.github.D10NGYANG:DLDateUtil:2.0.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    // 必须：公共工具
    implementation("com.github.D10NGYANG:DLCommonUtil:0.5.3")
}
```

3 混淆

```pro
-keep class com.d10ng.compose.** {*;}
-dontwarn com.d10ng.compose.**
```

4 代码演示

> 直接查看demo中app模块里代码[/app/src/main/java/com/d10ng/compose/demo/pages/](./app/src/main/java/com/d10ng/compose/demo/pages/HomeScreen.kt)

5 颜色配置

根据[Vant4的色彩规范](https://vant-contrib.gitee.io/vant/#/zh-CN/design#se-cai-gui-fan)，我们定义一套可修改变量；