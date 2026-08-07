# Compose UI 与 Material3 更新调研（2026-08）

> 调研日期：2026-08-07  
> 范围：Compose Multiplatform 1.11.x、项目升级前的 Material3 1.10.0-alpha05、官方 Sheet/预测性返回，以及对 UI 组件库有直接价值的升级与修复。  
> 来源策略：只引用 JetBrains/Kotlin、Android Developers、AndroidX/AOSP 等第一方资料。

> 实施状态：2026-08-07 已完成建议的生产基线升级和 Sheet 迁移。本文件中的“升级前”用于保留调研上下文；实际版本与行为以“实施结果”为准。

## 结论摘要

1. **升级前项目并非完全处于最新稳定线。** Compose Multiplatform 已从 `1.11.0` 升级到截至调研日的最新稳定版 `1.11.1`；后者修复 iOS Full Keyboard Access 元素选择与 Web 无障碍节点崩溃。[Compose Multiplatform 1.11.1 发布说明](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.11.1)
2. **Material3 已切到 1.11 配套版本。** `org.jetbrains.compose.material3:material3` 已从 `1.10.0-alpha05` 升级到 `1.11.0-alpha07`，即 Compose Multiplatform 1.11.1 配套、基于 AndroidX Material3 `1.5.0-alpha17` 的版本。[Compose Multiplatform 1.10.3 组件映射](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.10.3)；[Compose Multiplatform 1.11.1 依赖映射](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html#dependencies)
3. **生产基线已升级，未采用预发布 1.12。** 当前为 Compose Multiplatform `1.11.1` + JetBrains Material3 `1.11.0-alpha07`；`1.12.0-beta03` 仍只作为观察项。[Compose Multiplatform 发布列表](https://github.com/JetBrains/compose-multiplatform/releases)
4. **官方 Sheet 组件能满足返回键、外部点击、下拉关闭、半展开/展开、Insets、拖动手柄和预测性返回等基础能力。** 对本项目更推荐 1.11 配套 Material3 新增的同树 `BottomSheet + Scrim`，而不是直接换成会创建独立 Dialog window 的 `ModalBottomSheet`，因为它更贴合当前 `sheetBuilderListFlow` 叠层架构。[BottomSheet API](https://developer.android.com/reference/kotlin/androidx/compose/material3/BottomSheet.composable)；[ModalBottomSheet API](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-modal-bottom-sheet.html)

## 当前项目版本审计

| 项目项 | 升级前 | 实施结果 | 状态 |
|---|---:|---:|---|
| Compose Multiplatform 插件、Runtime/UI/Foundation | `1.11.0` | `1.11.1` | 已升级 |
| JetBrains Compose Material3 | `1.10.0-alpha05` | `1.11.0-alpha07` | 已升级并迁移 Sheet |
| Android Compose BOM | `2026.05.01` | `2026.06.00` | 已升级 |
| Navigation 3 | `1.1.1` | `1.1.1` | 官方配套表写 `1.1.2`，但 JetBrains KMP 根制品尚未发布，保持可解析版本 |
| Navigation Event | `1.1.0` | `1.1.0` | JetBrains KMP 坐标没有 `1.1.1`，保持可解析版本 |
| Kotlin | `2.3.21` | `2.3.21` | 满足 Compose 1.11 对 Native/Web 的最低要求 |

Compose Multiplatform 与 Jetpack Compose 独立发版，版本号不能机械地一一对应。官方表明 Compose Multiplatform 1.11.1 的 Runtime/UI/Foundation/Material `1.11.1` 基于 Jetpack Compose `1.11.2`，而 Material3 单独使用 `1.11.0-alpha07`，基于 AndroidX Material3 `1.5.0-alpha17`。[兼容性与版本映射](https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html)

Android BOM 只管理 AndroidX `androidx.compose.*` 依赖，不能替代 `commonMain` 中 `org.jetbrains.compose.*` 版本管理。官方当前示例使用 `androidx.compose:compose-bom:2026.06.00`。[Compose BOM 官方说明](https://developer.android.com/develop/ui/compose/bom)

## Sheet 与返回行为

### 官方能力

当前 Compose Multiplatform Material3 的 `ModalBottomSheet` 提供：

- `ModalBottomSheetProperties.shouldDismissOnBackPress`：返回操作是否调用 `onDismissRequest`；
- `ModalBottomSheetProperties.shouldDismissOnClickOutside`：点击遮罩是否关闭；
- `sheetGesturesEnabled`：是否允许手势操作 Sheet；
- `SheetState`：`Hidden`、`PartiallyExpanded`、`Expanded` 状态及 `show()`、`hide()`、`expand()`、`partialExpand()`；
- `confirmValueChange`：在状态切换前允许或否决目标状态；
- `sheetMaxWidth`、`dragHandle`、`scrimColor`、`contentWindowInsets` 等外观与窗口适配参数。

来源：[ModalBottomSheet](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-modal-bottom-sheet.html)、[SheetState](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-sheet-state/)、[ModalBottomSheetProperties](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-modal-bottom-sheet-properties/)

用户当前的语义是：`clickOutsideDismiss = true` 时，外部点击、下滑和系统返回都允许关闭；为 `false` 时视为强制弹窗。实际采用的同树方案映射为：

```kotlin
val userDismissEnabled = builder.clickOutsideDismiss
val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

Scrim(
    onClick = { if (userDismissEnabled) builder.dismiss() },
)
BottomSheet(
    state = sheetState,
    onDismissRequest = builder::dismiss,
    gesturesEnabled = userDismissEnabled,
    backHandlerEnabled = userDismissEnabled,
) {
    // 复用现有自定义 Sheet 内容
    builder.Build()
}
```

这里只统一“关闭策略”，不把多个官方属性暴露成多个业务配置。实现还在官方返回处理器未接管的进出场边界消费返回事件，防止穿透到底层页面；强制模式保留遮罩触摸拦截，但移除无效的可点击无障碍语义。代码主动调用 `dismiss()` 不受强制模式限制。[官方状态变更 API](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/remember-modal-bottom-sheet-state.html)

### 实施结果

本次只替换 Sheet 外壳，保留 `SheetBuilder`、`TitleBar`、Picker/Action/Radio/Date/Time 内容、`SheetBox`/`SheetColumn`、多 Sheet 列表和全局管理器。`dismiss()` 不再启动全局协程或等待固定 300ms，而是由组合层等待 `SheetState.hide()` 完成后移除。

| 场景 | `clickOutsideDismiss=true` | `clickOutsideDismiss=false` |
|---|---|---|
| 点击遮罩 | 关闭 | 拦截点击但不关闭 |
| 下滑手势 | 官方手势关闭 | 禁用 |
| 系统返回/返回手势 | 官方处理并支持预测性动画 | 消费事件但不关闭 |
| 代码调用 `dismiss()` | 官方退出动画完成后移除 | 官方退出动画完成后移除 |
| 退出动画中再次触摸/返回 | 禁用手势并拦截返回，防止动画取消或事件穿透 | 同左 |

验证覆盖 Android、JVM、iOS Simulator Arm64、JS、Wasm 编译，以及关闭策略的进入、已展示、退出、强制和已移除状态单元测试。JVM 与 Android 单元测试已运行通过；iOS 测试源码和可执行文件已编译、链接，但当前 Xcode 未安装可运行 `ios_simulator_arm64` 测试的 SDK；JS/Wasm 测试源码已编译通过。

### 替换现有外壳的收益与可能损失

现有 Builder、`TitleBar`、Picker/Action/Radio/Date/Time 等内容组件不需要丢弃；建议只替换 `Sheet.kt` 的全屏遮罩、动画、返回处理和状态机。

**本项目已采用同树 `BottomSheet + Scrim`。** 当前 `UiViewModel.SheetLayer()` 从 `sheetBuilderListFlow` 读取多个 Builder，并把各个 `Sheet` 渲染在应用自己的 Compose 层中。官方 `BottomSheet` 同样直接渲染在主 Composition hierarchy，由放置位置决定 Z 轴顺序，且和父 composable 共享生命周期与输入处理；`ModalBottomSheet` 则创建独立 `Dialog` window，并自动负责遮罩和焦点。前者保持了当前多 Sheet 列表、叠放顺序、全局管理器和跨平台渲染模型，同时由本库组合官方 `Scrim`、阻断底层点击并管理焦点。[BottomSheet 官方 API 与使用边界](https://developer.android.com/reference/kotlin/androidx/compose/material3/BottomSheet.composable)

迁移影响如下：

| 方面 | 切换官方实现后的影响 | 处理建议 |
|---|---|---|
| 官方外壳选择 | `BottomSheet` 留在当前 Compose 树；`ModalBottomSheet` 使用独立 Dialog window | 当前架构优先 `BottomSheet + Scrim`；只有不需要多 Sheet 同树管理时才优先 `ModalBottomSheet` |
| 现有业务 API 与内容 UI | 可保留 `SheetBuilder.Build()`、标题栏、颜色、圆角和所有 Picker 内容 | 把 `builder.Build()` 放入官方 content slot，避免公开 API 破坏 |
| 返回/手势/无障碍 | 获得 Android 预测性返回、拖动锚点及官方持续修复；同树版本不会自动提供完整 Modal 语义和底层输入阻断 | 复用现有遮罩/输入拦截并补充 Scrim content description |
| 强制弹窗 | 官方有三条退出路径：返回、遮罩点击、swipe-to-hidden；只关前两条仍可拖走 | 已同时关闭官方返回和 gestures，让 `Scrim` 只拦截点击，并用兼容处理器消费返回 |
| 动画观感 | 升级前 Sheet 位移使用固定 300ms `tween` | 现由 Material Motion/官方状态机驱动；遮罩淡入淡出仍保持 300ms |
| 移除时机 | 升级前 `dismiss()` 固定延迟 300ms 后移除 | 已改为等待 `sheetState.hide()` 完成后移除，不再使用全局协程或固定移除延迟 |
| 高度与顶部留白 | 当前 `topMargin()` 是显式最大高度约束；官方根据内容、窗口与 Sheet 状态布局 | 用高度上限/Insets 显式重建该约束并做大屏回归 |
| 键盘与焦点 | 当前显示时强制清焦点并隐藏键盘；官方 Sheet 支持在 Sheet 内继续使用 IME，不等同于自动执行当前策略 | 若这是既定产品行为，保留现有 `LaunchedEffect`；若 Sheet 内含输入框，应重新评估 |
| Insets | 当前 `SheetColumn` 自带 `navigationBarsPadding()`，官方还有 `contentWindowInsets` | 二选一或验证消费关系，避免双重底部留白 |
| 视觉默认值 | 官方默认有最大宽度、DragHandle、Material shape/颜色，可能改变 Web/平板/桌面观感 | 显式传 `sheetMaxWidth`、`dragHandle = null`、shape/color/scrim 以贴近现状 |

官方 API 已暴露上述状态、手势和外观控制点，但并不承诺复刻当前库自定义的固定动画与布局；因此“功能能力”基本不会丢失，“像素级行为”会变化。[BottomSheet API](https://developer.android.com/reference/kotlin/androidx/compose/material3/BottomSheet.composable)；[ModalBottomSheet API](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-modal-bottom-sheet.html)

### 预测性返回

Android 官方说明，Material3 `1.3.0` 及以上的 `ModalBottomSheet` 会自动响应预测性返回进度并播放对应动画；同批支持的组件还包括 `SearchBar` 与多种 Navigation Drawer。该能力是 Android 系统返回手势能力，不应推断为 iOS 导航边缘返回的等价实现。[Android 预测性返回与 Material 组件](https://developer.android.com/develop/ui/compose/system/predictive-back-setup)

因此，与只在手势完成时执行一次普通 `BackHandler` 相比，本次采用的官方 `BottomSheet` 在 Android 上可通过自身预测性返回处理器把手势进度反映到 Sheet 动画中；强制弹窗则有意禁用该关闭动画并消费返回事件。

## 从当前 Material3 到 1.11 配套版的重点变化

项目升级前的 JetBrains Material3 `1.10.0-alpha05` 基于 AndroidX Material3 `1.5.0-alpha08`；现已采用的 `1.11.0-alpha07` 基于 `1.5.0-alpha17`。以下变化均位于这一区间，完整逐版记录见 [AndroidX Compose Material3 发布说明](https://developer.android.com/jetpack/androidx/releases/compose-material3)。

### Sheet、遮罩与拖动

- 新增可独立使用的静态 `BottomSheet` 和可复用 `Scrim` 组件；`BottomSheet` 增加禁用 back handler 的能力。
- Bottom Sheet 在拖动和嵌套滚动时开始遵循 `MaterialTheme.motionScheme`。
- 针对 `ModalBottomSheet`、`BottomSheetScaffold` 等 anchored-draggable 组件，增加未初始化 offset 的保护、anchor 失效重算与模糊目标恢复机制。
- 修复 `SheetState.targetValue` 初始化时异常跳变、`sheetPeekHeight = 0` 时多余的半展开锚点，以及 `BottomSheetScaffold` 错误调用 `confirmValueChange` 的问题。

这些修复直接降低自定义 Sheet 外壳最难自行处理的状态机、手势竞争和锚点失效风险。[Material3 1.5.0-alpha14 至 alpha16 记录](https://developer.android.com/jetpack/androidx/releases/compose-material3#1.5.0-alpha16)

### 新组件与已稳定 API

- 新增多比例 Carousel，并支持用 lazy grid 构建；Multi-browse 与 Uncontained Carousel API 稳定。
- 新增 `ExpandedFullScreenContainedSearchBar` 与 `ExpandedDockedSearchBarWithGap`，并允许自定义搜索展开/收起动画规格。
- 新增 Material Expressive List Item，支持交互与分段样式；菜单增加 selectable、toggleable、group 和 popup 组合能力。
- Slider 的实验性 API、`TopAppBarScrollBehavior`、Motion Scheme 转为稳定；Typography 增加全局默认 `FontFamily` 能力。
- `DatePicker` 的 Java Time API 转为稳定。

来源：[Material3 1.5.0-alpha09 至 alpha17 发布记录](https://developer.android.com/jetpack/androidx/releases/compose-material3#1.5.0-alpha17)

### 重要视觉、输入与无障碍修复

- 修复 Switch 在 `ReusableContent` 中异常动画、Slider 触摸按下反馈和键盘焦点陷阱。
- 修复 Top App Bar 标题与 actions 重叠、DatePicker API 23 格式化崩溃、TimePicker AM/PM 文本样式、Toolbar 横屏键盘崩溃。
- 改进 Snackbar 多行对齐、TextButton 规范内边距、Tooltip 方向更新，以及多处 RTL、键盘和 TalkBack 行为。

来源：[AndroidX Compose Material3 发布说明](https://developer.android.com/jetpack/androidx/releases/compose-material3)

## Compose Multiplatform 1.11 的 UI 层升级

### 跨平台与基础设施

- `Dialog` 新增进入/退出动画，可通过 `DialogProperties.animateTransition` 或全局开关禁用。
- 非 Android 平台支持 Compose UI Test v2；默认使用 `StandardTestDispatcher`，并允许配置 `effectContext`，旧的测试入口被弃用。
- `SelectionContainer` 支持拖选越界时自动滚动。
- Window Insets padding modifier 迁移到 Modifier Node 实现，并修复系统 Insets 消费问题。
- Skia 从 m138 升到 m144；Gradle 插件新增 Runtime 与 Skiko 版本一致性检查。

来源：[Compose Multiplatform 1.11.0 发布说明](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.11.0)、[Compose Multiplatform 1.11 新特性](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)

### iOS

- 实验性 Native iOS Text Input 可用于 `BasicTextField`，带来原生光标定位、放大镜、选择手势、系统上下文菜单、Autofill 与自动纠错体验。
- 并发渲染从实验性选择项变为默认开启。
- `ComposeUIView` 支持把 Compose 嵌入现有 UIKit view hierarchy。
- Sheet/弹层相关修复包括：`ModalBottomSheet` Insets 错误消费、`ModalBottomSheetDialog` 内容 Insets、Popup/Dialog 外点无法关闭、返回导航同时关闭 Popup 的崩溃，以及 Popup/Dialog 快速开关时的 Metal 崩溃。

来源：[Compose Multiplatform 1.11.0 发布说明](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.11.0)

### Web 与 Desktop

- Web 端重构触摸与指针事件，改善惯性、短滑误 fling、Magic Mouse 与移动浏览器输入体验；Web 滚动体验是 1.11 的官方重点。
- Desktop 修复文本快捷键、数字键盘方向键、无障碍节点移除崩溃、SwingPanel 黑框与 Metal 离屏渲染问题。

来源：[Compose Multiplatform 1.11 新特性](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)、[Compose Multiplatform 1.11.0 发布说明](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.11.0)

### Runtime 性能

Jetpack Compose Runtime 1.11 重写了内部 `SlotTable`，目标是减少重组时的内存复制；官方称长列表重排场景可达到两倍以上重组速度，多数其他操作约提升 10%。这是 Runtime 内部收益，不需要 UI 组件逐个适配，但仍应通过本项目的 UI 回归与基准测试确认实际表现。[Compose Runtime 1.11.0 发布说明](https://developer.android.com/jetpack/androidx/releases/compose-runtime#1.11.0)

## 1.12 / 更前沿 Material3 的观察项

截至调研日最新 Compose Multiplatform 预发布版为 `1.12.0-beta03`，它携带 JetBrains Material3 `1.12.0-alpha03`，基于 AndroidX Material3 `1.5.0-alpha22`。这条线带来 Mesh Gradient、GraphicsLayer `LayerOutsets`、iOS 返回手势冲突修复等变化，但不宜作为当前 UI 库的生产默认线。[Compose Multiplatform 1.12.0-beta03 发布说明](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0-beta03)

AndroidX Material3 自身截至 2026-07-29 已到稳定 `1.4.0` 与实验 `1.5.0-alpha25`；后续 alpha 还在统一 `rememberBottomSheetState`、确定性管理半展开锚点、改进 Sheet dismiss 手势物理、推进 SearchBar/Expressive List/Top App Bar/BasicAlertDialog 稳定化，以及补充焦点环和键盘无障碍能力。它们是后续 Compose Multiplatform 版本的前瞻信息，`commonMain` 不应直接改用 AndroidX 坐标。[AndroidX Compose 版本表](https://developer.android.com/jetpack/androidx/releases/compose)、[Material3 1.5 发布记录](https://developer.android.com/jetpack/androidx/releases/compose-material3#1.5.0-alpha25)

## 迁移风险与验证建议

Compose Multiplatform 1.11 的主要兼容性注意项包括：

- non-Android `Shader` 从 Skia typealias 改为 Compose wrapper，是 ABI 不兼容变更；依赖该 API 的第三方库需要更新。
- Native/Web 需要 Kotlin 2.3；Web 使用 Compose 1.11 时要求 Kotlin 2.3.20。当前项目 `2.3.21` 满足要求。
- Apple x86_64 目标被移除，最低 iOS 提升到 14；当前若仍声明 `iosX64` 需要先迁移。
- `Key.Home`、`NativePaint`、`NativeCanvas`、`Canvas.nativeCanvas` 等 API 弃用或替换。
- Navigation 不再把 `androidx.collection` 作为 `api` 传递依赖；如源码直接使用相关类型，需要显式声明依赖。

来源：[Compose Multiplatform 1.11.0 迁移说明](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.11.0)、[Compose Multiplatform 1.11 新特性](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)

建议升级顺序：

1. ✅ Compose Multiplatform 已从 `1.11.0` 提升至 `1.11.1`，并完成多平台编译。
2. ✅ JetBrains Material3 已从 `1.10.0-alpha05` 提升至 `1.11.0-alpha07`。
3. ✅ Android Compose BOM 已从 `2026.05.01` 提升至 `2026.06.00`。
4. ✅ 官方同树 `BottomSheet + Scrim` 已作为内部实现接入，现有 Builder/内容 API 和 `sheetBuilderListFlow` 管理方式保留。
5. ✅ 已增加关闭策略单元测试；仍建议在发布前用真机专项回归 Android 返回手势完成/取消、IME、Insets、连续快速显示/隐藏，以及 iOS/Web 输入行为。
