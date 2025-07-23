# DLJetpackComposeUtil

基于 Compose Multiplatform 开发的 Vant 风格 UI 组件库，提供丰富的跨平台 UI 组件，支持 Android、iOS 和 Web(WasmJs) 平台。

[![Version](https://img.shields.io/badge/version-3.1.3-blue.svg)](https://github.com/D10NGYANG/DLJetpackComposeUtil)
[![Kotlin](https://img.shields.io/badge/kotlin-2.2.0-orange.svg)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.8.2-green.svg)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](LICENSE)

> 参考 [Vant4](https://vant-contrib.gitee.io/vant/#/zh-CN/home) 设计规范，提供统一的设计语言和用户体验。

## 目录

- [特性](#特性)
- [预览](#预览)
- [安装](#安装)
- [快速开始](#快速开始)
- [组件列表](#组件列表)
- [主要组件使用示例](#主要组件使用示例)
- [主题定制](#主题定制)
- [许可证](#许可证)

## 特性

- 🚀 **跨平台支持**：基于 Compose Multiplatform 开发，支持 Android、iOS 和 Web(WasmJs) 平台
- 🎨 **统一设计风格**：参考 Vant4 设计规范，提供一致的视觉体验
- 📦 **丰富的组件**：提供 20+ 个高质量组件，覆盖常见的 UI 场景
- 🔧 **灵活的主题定制**：支持自定义主题色、形状和文字样式
- 📱 **响应式设计**：组件自适应不同屏幕尺寸
- 🛠️ **易于使用**：API 设计简洁直观，上手成本低

## 预览

在线预览：[https://d10ngyang.github.io/DLJetpackComposeUtil/](https://d10ngyang.github.io/DLJetpackComposeUtil/)

## 安装

### 1. 添加仓库

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository' }
  }
}
```

### 2. 添加依赖

```gradle
dependencies {
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:3.1.3")
}
```

### 3. 混淆配置

```proguard
-keep class com.d10ng.compose.** {*;}
-dontwarn com.d10ng.compose.**
```

## 快速开始

### 1. 初始化 UI 管理器

在应用的入口处初始化 UI 管理器，用于管理全局的 Toast、Dialog 等组件：

```kotlin
@Composable
fun App() {
    // 应用内容
    MaterialTheme(
        colorScheme = AppColor.toColorScheme()
    ) {
        // 你的应用内容
        // TODO
        // 初始化 UI 管理器
        UiViewModelManager.Init()
    }
}
```

### 2. 使用组件示例

```kotlin
@Composable
fun SampleScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // 按钮组件
        Button(
            text = "主要按钮",
            type = ButtonType.PRIMARY,
            onClick = {
                // 显示 Toast
                UiViewModelManager.showToast("点击了按钮")
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 输入框组件
        var text by remember { mutableStateOf("") }
        Field(
            value = text,
            onValueChange = { text = it },
            label = "用户名",
            placeholder = "请输入用户名"
        )
    }
}
```

## 组件列表

> 支持程度图标说明
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

## 主要组件使用示例

### Button 按钮

```kotlin
// 基础按钮
Button(
    text = "按钮",
    onClick = { /* 点击事件 */ }
)

// 主要按钮
Button(
    text = "主要按钮",
    type = ButtonType.PRIMARY,
    onClick = { /* 点击事件 */ }
)

// 成功按钮
Button(
    text = "成功按钮",
    type = ButtonType.SUCCESS,
    onClick = { /* 点击事件 */ }
)

// 警告按钮
Button(
    text = "警告按钮",
    type = ButtonType.WARNING,
    onClick = { /* 点击事件 */ }
)

// 危险按钮
Button(
    text = "危险按钮",
    type = ButtonType.DANGER,
    onClick = { /* 点击事件 */ }
)

// 朴素按钮
Button(
    text = "朴素按钮",
    type = ButtonType.PRIMARY,
    plain = true,
    onClick = { /* 点击事件 */ }
)

// 禁用状态
Button(
    text = "禁用按钮",
    disabled = true,
    onClick = { /* 点击事件 */ }
)

// 加载状态
Button(
    text = "加载中",
    loading = true,
    onClick = { /* 点击事件 */ }
)

// 按钮尺寸
Button(
    text = "大号按钮",
    size = ButtonSize.LARGE,
    onClick = { /* 点击事件 */ }
)

Button(
    text = "小号按钮",
    size = ButtonSize.SMALL,
    onClick = { /* 点击事件 */ }
)

Button(
    text = "迷你按钮",
    size = ButtonSize.MINI,
    onClick = { /* 点击事件 */ }
)
```

### Field 输入框

```kotlin
var text by remember { mutableStateOf("") }

Field(
    value = text,
    onValueChange = { text = it },
    label = "用户名",
    placeholder = "请输入用户名"
)

// 密码输入框
Field(
    value = password,
    onValueChange = { password = it },
    label = "密码",
    type = KeyboardType.Password
)

// 带清除按钮的输入框
Field(
    value = text,
    onValueChange = { text = it },
    label = "搜索",
    canClear = true
)

// 只读输入框
Field(
    value = "只读内容",
    onValueChange = {},
    label = "只读",
    readonly = true
)

// 禁用输入框
Field(
    value = "禁用内容",
    onValueChange = {},
    label = "禁用",
    disabled = true
)

// 错误提示
Field(
    value = text,
    onValueChange = { text = it },
    label = "手机号",
    error = if (text.length != 11) "请输入11位手机号" else ""
)
```

### Dialog 对话框

```kotlin
// 确认对话框
val dialogId = UiViewModelManager.showDialog(
    ConfirmDialogBuilder(
        title = "标题",
        content = "这是一条确认信息，是否继续？",
        onConfirm = {
            // 确认操作
            UiViewModelManager.hideDialog(it)
        },
        onCancel = {
            // 取消操作
            UiViewModelManager.hideDialog(it)
        }
    )
)

// 输入对话框
val inputDialogId = UiViewModelManager.showDialog(
    InputDialogBuilder(
        title = "输入框标题",
        content = "请输入内容",
        placeholder = "请输入",
        onConfirm = { id, text ->
            // 处理输入内容
            UiViewModelManager.hideDialog(id)
        }
    )
)

// 提示对话框
val tipsDialogId = UiViewModelManager.showDialog(
    TipsDialogBuilder(
        title = "提示",
        content = "操作成功！",
        onConfirm = {
            UiViewModelManager.hideDialog(it)
        }
    )
)
```

### Toast 轻提示

```kotlin
// 普通提示
UiViewModelManager.showToast("这是一条提示消息")

// 成功提示
UiViewModelManager.showSuccessToast("操作成功")

// 失败提示
UiViewModelManager.showFailToast("操作失败")

// 加载提示
UiViewModelManager.showLoading("加载中...")
// 隐藏加载
UiViewModelManager.hideLoading()
```

### Notify 消息通知

```kotlin
// 主要通知
UiViewModelManager.showNotify(NotifyType.Primary, "这是一条通知消息")

// 成功通知
UiViewModelManager.showNotify(NotifyType.Success, "操作成功")

// 警告通知
UiViewModelManager.showNotify(NotifyType.Warning, "注意事项")

// 错误通知
UiViewModelManager.showErrorNotify("发生错误")
```

### Badge 徽标

```kotlin
// 红点徽标
Badge()

// 数字徽标
Badge(content = "5")

// 最大值徽标
Badge(num = 100, max = 99) // 显示 "99+"

// 自定义颜色
Badge(content = "NEW", color = Color.Blue)

// 徽标与内容组合
BadgeBox(
    badge = { Badge(content = "5") }
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_notification),
        contentDescription = "通知"
    )
}
```

### Tag 标签

```kotlin
// 基础标签
Tag(text = "标签")

// 主要标签
Tag(text = "标签", type = TagType.PRIMARY)

// 成功标签
Tag(text = "标签", type = TagType.SUCCESS)

// 警告标签
Tag(text = "标签", type = TagType.WARNING)

// 危险标签
Tag(text = "标签", type = TagType.DANGER)

// 朴素标签
Tag(text = "标签", type = TagType.PRIMARY, plain = true)

// 圆角标签
Tag(text = "标签", round = true)

// 可关闭标签
Tag(
    text = "标签",
    closeable = true,
    onClose = { /* 关闭事件 */ }
)
```

## 主题定制

DLJetpackComposeUtil 提供了灵活的主题定制能力，可以根据项目需求自定义颜色、形状和文字样式。组件库的颜色系统与 Material 3 的色彩角色紧密集成，通过 `AppColor.toColorScheme()` 方法将自定义颜色映射到 Material 3 的 ColorScheme。

[Material 3 色彩介绍](color.md)

### 颜色定制

组件库的颜色系统分为三大类：

#### 1. 主色系统 (AppColor.Main)

主色系统定义了应用的核心颜色，直接映射到 Material 3 的主要色彩角色：

```kotlin
// 主色系统
AppColor.Main.primary = Color(0xFF1989FA)    // 主题色，映射到 M3 的 primary
AppColor.Main.secondary = Color(0xFF07C160)  // 第二主题色，映射到 M3 的 secondary
AppColor.Main.tertiary = Color(0xFFEE0A24)   // 第三主题色，映射到 M3 的 tertiary
```

#### 2. 功能色系统 (AppColor.Func)

功能色系统定义了表达特定语义的颜色：

```kotlin
// 功能色系统
AppColor.Func.link = Color(0xFF576B95)      // 链接色，用于可点击文本
AppColor.Func.success = Color(0xFF07C160)   // 成功色，映射到相关组件的成功状态
AppColor.Func.error = Color(0xFFEE0A24)     // 错误色，映射到 M3 的 error
AppColor.Func.notice = Color(0xFFED6A0C)    // 通知色，用于警告和通知
AppColor.Func.noticeBg = Color(0xFFFFFBE8)  // 通知背景色
AppColor.Func.assist = Color(0xFFFAAB0C)    // 辅助色，用于强调和提示
```

#### 3. 中性色系统 (AppColor.Neutral)

中性色系统定义了界面的基础色调，映射到 Material 3 的表面和文本颜色：

```kotlin
// 中性色系统
AppColor.Neutral.surface = Color(0xFFFEFEFE)  // 表面色，映射到 M3 的 surface
AppColor.Neutral.bg = Color(0xFFF7F8FA)       // 背景色，映射到 M3 的 background
AppColor.Neutral.card = Color(0xFFF2F3F5)     // 卡片背景色，映射到 M3 的 surfaceVariant
AppColor.Neutral.line = Color(0xFFEBEDF0)     // 线条色，映射到 M3 的 outlineVariant
AppColor.Neutral.border = Color(0xFFDCDEF0)   // 边框色，映射到 M3 的 outline
AppColor.Neutral.hint = Color(0xFFC8C9CC)     // 提示文本色，用于占位符和禁用状态
AppColor.Neutral.tips = Color(0xFF969799)     // 辅助文本色，用于次要信息
AppColor.Neutral.body = Color(0xFF646566)     // 正文文本色，映射到 M3 的 onSurfaceVariant
AppColor.Neutral.title = Color(0xFF323233)    // 标题文本色，映射到 M3 的 onSurface 和 onBackground
AppColor.Neutral.scrim = Color(0xFF010101)    // 遮罩色，映射到 M3 的 scrim
```

#### Material 3 色彩映射

组件库通过 `AppColor.toColorScheme()` 方法将自定义颜色映射到 Material 3 的 ColorScheme。这个方法不仅直接映射基础颜色，还通过 `makeRelatedColors()` 函数自动生成相关的配色方案，确保整个应用的色彩协调一致。

```kotlin
// 在应用入口处应用自定义主题
MaterialTheme(
    colorScheme = AppColor.apply {
        // 自定义颜色，只有在此之前设置颜色才能生效
        AppColor.Main.primary = Color.Red
    }.toColorScheme()
) {
    // 应用内容
}
```

##### 基础颜色映射

| Material 3 色彩角色 | DLJetpackComposeUtil 颜色 |
|:-----------------|:------------------------|
| primary | AppColor.Main.primary |
| secondary | AppColor.Main.secondary |
| tertiary | AppColor.Main.tertiary |
| error | AppColor.Func.error |
| background | AppColor.Neutral.bg |
| surface | AppColor.Neutral.surface |
| onBackground | AppColor.Neutral.title |
| onSurface | AppColor.Neutral.title |
| surfaceVariant | AppColor.Neutral.card |
| onSurfaceVariant | AppColor.Neutral.body |
| outline | AppColor.Neutral.border |
| outlineVariant | AppColor.Neutral.line |
| scrim | AppColor.Neutral.scrim |
| surfaceTint | AppColor.Main.primary |

##### 自动生成的相关颜色

组件库使用 `makeRelatedColors()` 函数基于主要颜色自动生成相关的配色：

```kotlin
// 基于主色生成相关颜色
val primaryRelatedColors = Main.primary.makeRelatedColors()
// primaryRelatedColors[0] → onPrimary
// primaryRelatedColors[1] → primaryContainer
// primaryRelatedColors[2] → onPrimaryContainer
// primaryRelatedColors[3] → inversePrimary
```

| Material 3 色彩角色      | 生成方式                      |
|:---------------------|:--------------------------|
| onPrimary            | primaryRelatedColors[0]   |
| primaryContainer     | primaryRelatedColors[1]   |
| onPrimaryContainer   | primaryRelatedColors[2]   |
| inversePrimary       | primaryRelatedColors[3]   |
| onSecondary          | secondaryRelatedColors[0] |
| secondaryContainer   | secondaryRelatedColors[1] |
| onSecondaryContainer | secondaryRelatedColors[2] |
| onTertiary           | tertiaryRelatedColors[0]  |
| tertiaryContainer    | tertiaryRelatedColors[1]  |
| onTertiaryContainer  | tertiaryRelatedColors[2]  |
| onError              | errorRelatedColors[0]     |
| errorContainer       | errorRelatedColors[1]     |
| onErrorContainer     | errorRelatedColors[2]     |

##### 表面变体颜色

组件库还使用 `next()` 函数基于 `Neutral.surface` 生成多种表面变体颜色：

| Material 3 色彩角色         | 生成方式                       |
|:------------------------|:---------------------------|
| inverseSurface          | Neutral.surface.next(亮度调整) |
| inverseOnSurface        | 根据surface亮度自动选择黑色或白色       |
| surfaceBright           | Neutral.surface.next(1.0)  |
| surfaceDim              | Neutral.surface.next(-0.3) |
| surfaceContainer        | Neutral.surface.next(-0.2) |
| surfaceContainerHigh    | Neutral.surface.next(-0.3) |
| surfaceContainerHighest | Neutral.surface.next(-0.4) |
| surfaceContainerLow     | Neutral.surface.next(-0.1) |
| surfaceContainerLowest  | Neutral.surface.next(1.0)  |

### 形状定制

```kotlin
// 使用预定义的圆角
val buttonShape = AppShape.RC.v8 // 8dp 圆角
val cardShape = AppShape.RC.v16 // 16dp 圆角
val chipShape = AppShape.RC.Cycle // 圆形
```

### 文字样式定制

```kotlin
// 标题文字样式
val titleStyle = AppText.Bold.Title.large // 粗体大号标题
val subtitleStyle = AppText.Medium.Title.default // 中等粗细默认标题

// 内容文字样式
val bodyStyle = AppText.Normal.Body.default // 正常粗细默认内容
val smallBodyStyle = AppText.Normal.Body.small // 正常粗细小号内容

// 强调文字样式
val primaryStyle = AppText.Bold.Primary.default // 粗体主题色文字
val errorStyle = AppText.Medium.Error.default // 中等粗细错误色文字
```

## 许可证

DLJetpackComposeUtil 使用 [MIT 许可证](LICENSE)。