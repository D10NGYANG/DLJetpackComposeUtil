# Material 3 Top App Bar 替代性评估

评估日期：2026-08-12
评估对象：`NavBar` 及其独立 Demo 页面 `NavBarScreen`
项目依赖：Compose Multiplatform Material 3 `1.11.0-alpha07`（其 POM 声明 AndroidX Material 3 `1.5.0-alpha17`）

## 结论

Material 3 小型 `TopAppBar` / `CenterAlignedTopAppBar` 可以接管 `NavBar` 的主要布局基础设施：标题、导航图标、尾随操作、窗口 Insets、颜色、长标题约束、RTL 起止方向布局、遍历分组语义，以及可选的 pinned / enter-always / exit-until-collapsed 滚动行为。

不建议删除 `NavBar` 后直接让现有调用方改用官方 API。CodeGraph 与调用面检索显示它已经是库内 Demo 的通用页头，当前共有约 60 个调用表达式（包含定义文件预览和 `NavBarScreen` 场景），且公开 API 还约定了 60 dp 内容高度、可选状态栏占位、任意 `Alignment`、底部分割线、自有 22 sp 粗体标题和自有返回资源。这些都不是官方 Top App Bar 的逐项同义参数。

推荐保留现有函数签名和参数顺序，先把它改为官方组件的兼容包装：

- `Alignment.Center` 委托 `CenterAlignedTopAppBar`，`Alignment.CenterStart` 委托 `TopAppBar`；两者均传 `expandedHeight = 60.dp`，保持旧内容栏高度。
- `withStatusBar = true` 使用官方默认 `TopAppBarDefaults.windowInsets`；为 `false` 时传 `WindowInsets(0)`。这会把默认安全区从旧实现的“只留顶部高度”提升为“顶部与水平安全区”，属于有意修复，需跨平台截图验证。
- 标题 slot 继续显式使用 `AppText.Bold.Title.large`、`maxLines = 1` 和 `TextOverflow.Ellipsis`，不要接受官方默认 `titleLarge` 后声称视觉兼容。
- `background` 同时映射为 `containerColor` 和 `scrolledContainerColor`，否则加入滚动行为后颜色可能切换到官方 `surfaceContainer`。
- 返回 slot 使用官方 `IconButton` 承担 48 dp 触控目标、涟漪、Button role、键盘点击和禁用语义；图形可以首步保留旧资源，但必须补 RTL 镜像和可本地化的 content description。
- `right` 继续包装进 `actions`。官方只提供 `RowScope` 和内容颜色，任意按钮、文字及其语义仍由 slot 内容负责。
- `border` 仍由兼容包装在 Top App Bar 下方组合 `HorizontalDivider`，它不应为了迁移而变成官方滚动阴影。

`NavBar` 整体不适合改成 Modifier。它创建标题、导航动作、尾随内容和分割线，并控制 Insets 与语义节点。适合成为 Modifier 的只有容器上的滚动连接（官方已经提供 `Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)`）或纯装饰分割线；没有必要再包装一个同义 Modifier 扩展。

## 当前实现与调用面

CodeGraph 首先定位到：

| 符号 | 位置 | 当前职责 | 调用面 |
| --- | --- | --- | --- |
| `NavBar` | `library/src/commonMain/kotlin/com/d10ng/compose/ui/navigation/NavBar.kt` | 组合状态栏占位、60 dp 页头、返回按钮、标题、尾随 slot 和分割线 | Demo 大多数独立组件页共同使用；CodeGraph 报告 51 个调用方，文本检索当前有约 60 个调用表达式 |
| `NavBarScreen` | `composeApp/src/commonMain/kotlin/com/d10ng/compose/demo/pages/NavBarScreen.kt` | 展示基础、返回、左对齐、图标/按钮尾随内容 | 由 Demo 导航入口调用 |

当前公开签名为：

```kotlin
fun NavBar(
    title: String,
    titleAlignment: Alignment = Alignment.Center,
    background: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    withStatusBar: Boolean = true,
    border: Boolean = false,
    onClickBack: (() -> Unit)? = null,
    right: @Composable (() -> Unit)? = null,
)
```

既有行为不能只从参数名推断：

- 背景覆盖状态栏占位、内容栏和分割线所在外层 Column。
- `withStatusBar` 通过 `windowInsetsTopHeight(WindowInsets.systemBars)` 创建一个独立顶部 Box，只读取顶部高度；不为左右 display cutout / safe area 留空间。
- 内容栏精确 `height(60.dp)`。底部分割线另占高度，因此 `border = true` 时总高度会再增加分割线厚度。
- 返回图是一个 `Image`，前置 6 dp、节点 40 dp、圆形裁剪和普通 `clickable`；资源本身约为 13.6 x 22 dp，content description 写死为英文 `back`。
- 返回资源 XML 没有自动镜像属性；当前 RTL 下 Row 的起始位置会翻转，但箭头图形方向不会翻转。
- 有返回按钮时，标题容器固定左右各留 59 dp；没有返回按钮时固定左右各留 16 dp。尾随内容宽度不会参与标题测量，所以足够宽的自定义 `right` 可能与标题视觉重叠。
- 标题使用自有 `AppText.Bold.Title.large`，即自有标题色、Bold、22 sp；固定单行省略。
- `titleAlignment` 的类型是完整 `Alignment`，理论上允许中心、起止、顶部/底部等任意二维对齐，实际 Demo 只使用 `Alignment.Center` 与 `Alignment.CenterStart`。
- `right` 是无接收者的任意 Composable。现有 Demo 会放置自绘可点击图标或自有文字按钮，而非只放官方 IconButton。

因此首步应保持公开签名。若后续希望公开官方 `scrollBehavior`、`WindowInsets`、`navigationIcon` 或 `RowScope.actions`，应新增一个更现代的重载或新 API，而不是把参数插入现有位置造成 Kotlin 源码/二进制兼容风险。

## 项目精确版本的官方实现

Material 3 `1.11.0-alpha07` 的小型页头公开两个稳定方向：

```kotlin
@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    contentPadding: PaddingValues = TopAppBarDefaults.ContentPadding,
)

@Composable
fun CenterAlignedTopAppBar(/* 同类参数 */)
```

这个版本的精确实现有以下重要事实：

- `AppBarSmallTokens.ContainerHeight` 是 64 dp；不是仓库的 60 dp。不过 `expandedHeight` 允许传有限、已指定的 60 dp，因此无需在外层缩放或裁切。
- 默认内容 padding 是 0 dp；布局内部仍在导航与尾随内容外各留 4 dp，并给标题自身各留 4 dp。
- `TopAppBar` 使用 `Alignment.Start`，`CenterAlignedTopAppBar` 使用 `Alignment.CenterHorizontally`。新的带 `titleHorizontalAlignment` 的小型重载同时需要实验性的 Expressive API，不应只为兼容 `Alignment.CenterStart` 而引入。
- 默认 Insets 是 `systemBarsForVisualComponents` 的 Top + Horizontal，并通过 `windowInsetsPadding` 应用；它不是旧实现单独创建的状态栏高度 Box。
- 默认背景色 token 是 `surface`，滚动覆盖后是 `surfaceContainer`；仓库默认是 `surfaceContainerLowest`。必须显式传颜色才能维持外观。
- Top App Bar 本身标记 `isTraversalGroup = true`，使页头内容作为语义遍历组；标题的 Text 语义来自标题 slot。
- 官方不会替 slot 内容自动增加“返回”或“保存”语义。文档明确导航与操作项通常应使用 `IconButton` / `IconToggleButton`。
- 官方 `IconButton` 安装 `minimumInteractiveComponentSize()`、48 dp 最小触控目标、涟漪、`Role.Button` 与 clickable 语义。旧返回节点仅 40 dp，且普通 clickable 没有显式 Button role。

项目 sources JAR 中还有一个带 `subtitle` 与任意水平标题对齐的小型 `TopAppBar`，但它标记 `ExperimentalMaterial3ExpressiveApi`。当前 `NavBar` 没有副标题需求，不建议为了统一两个分支采用实验性 API。

## 固定高度、Insets 与 Scaffold

### 固定高度

将 `expandedHeight = 60.dp` 传给官方组件可以保持无滚动时内容区域的 60 dp。标题或 slot 内容如果自身高于 60 dp，官方测量会用 `max(requestedHeight, titleHeight)` 扩高；旧实现的精确 `height(60.dp)` 会受父约束并可能裁压异常大内容。这是一个合理的稳健性改进，但也意味着极端字体缩放或超大自定义内容时不再保证像素级固定总高。

`border = true` 仍会额外增加分割线高度。不要把 60 dp 与官方默认 64 dp 混用，否则每个 Demo 页首屏内容都会整体下移 4 dp。

### 状态栏和水平安全区

官方默认 Insets 同时处理 Top 和 Horizontal，并将安全区与 60 dp 内容区组合为整体 Top App Bar。旧实现用 `WindowInsets.systemBars` 的顶部高度创建空白 Box，左右内容仍能进入刘海或圆角安全区。

兼容映射建议：

| 旧参数 | 官方参数 | 说明 |
| --- | --- | --- |
| `withStatusBar = true` | `TopAppBarDefaults.windowInsets` | 增加水平安全区处理，是有意修复 |
| `withStatusBar = false` | `WindowInsets(0)` | 保持嵌入 CellGroup 等场景没有额外系统栏高度 |

调用方若已经在 `Scaffold`、父容器或窗口层消费 Insets，仍可能出现双重顶部留白。这不是官方组件能自动判断的，Demo 应展示父层已处理 Insets 的嵌入场景。官方 `Scaffold` 的 `contentWindowInsets` 与 `TopAppBar.windowInsets` 也要按宿主结构选一个消费边界，不能同时手工 padding。

跨平台上，`WindowInsets` 的实际值来自 Compose 平台实现：Android、iOS、Desktop、JS/Wasm 的系统栏与安全区模型不同。commonMain 编译通过只能证明 API 可用，不能证明各平台截图相同；iOS 刘海/横屏和浏览器 Canvas 是重点实测边界。

## 标题对齐、长文本与 RTL

官方布局会先测量导航和操作区，再把标题最大宽度限制为：总宽度减去起始保留区、尾随操作宽度与 content padding。因此长标题会在真实操作项之前被约束，修复旧实现中任意宽 `right` 与标题重叠的风险。

中心标题不是简单放在导航与操作之间的剩余区域中心。官方先相对整个 App Bar 居中，再在碰到较宽导航或操作项时移动标题。这能优先避免重叠，但左右操作宽度不对称时，最终标题可能偏离屏幕绝对中心。旧实现只要标题仍落在固定 59 dp 对称边距内，就保持绝对居中，即使与较宽 `right` 重叠。迁移必须用“短/长标题 x 无操作/单图标/文字按钮/超宽操作”做截图对比。

官方在测量 start/end padding 时使用当前 `layoutDirection`，最终通过 `placeRelative` 放置导航、标题和 actions，因此 LTR/RTL 的起止布局会翻转。内部中心对齐计算刻意使用 LTR 后再相对放置，保持中心逻辑稳定。

但 RTL 只解决布局方向，不会自动镜像任意 painter。当前 `ic_round_back_22.xml` 没有 auto-mirrored 标记，迁移时应改用支持自动镜像的官方返回图标，或根据 `LocalLayoutDirection` 对旧 painter 做水平镜像。否则导航按钮会移到右侧，但箭头仍指向左侧。

公开的 `Alignment` 参数比官方稳定 API 的水平 Start/Center 更宽。建议只映射仓库实际支持并演示的 `Alignment.Center` 与 `Alignment.CenterStart`；对其他 Alignment 要么保留旧布局分支，要么在后续主版本把 API 收窄为明确枚举。静默把 `CenterEnd`、`TopStart` 等全部解释成 Start 会改变既有语义。

## 返回按钮、右侧内容与语义

官方 Top App Bar 负责 slot 布局和 LocalContentColor，不负责 slot 内部交互。推荐返回按钮结构为 `IconButton { Icon(...) }`，并使用本地化的“返回”描述。这样可以继承 48 dp 最小触控目标、焦点/键盘点击、Button role、涟漪和禁用语义。

旧实现的 40 dp 返回节点迁为 48 dp 后，标题可用宽度和起始位置会变化；官方布局还会保留 4 dp leading space。不能同时在外层照搬旧 6 dp padding，否则导航区会进一步变宽。应以 390 px 截图确认视觉，而不是通过叠加 magic number 强行复制旧坐标。

`right` 可以直接作为官方 `actions` 内容调用，且 `actions` 自带水平 Row。但是：

- 旧 slot 没有 `RowScope` 接收者，保持签名时只应 `right?.invoke()`，不能要求旧调用方使用 `weight` 等 RowScope API。
- 官方建议 actions 使用 IconButton，但仓库明确支持文字 Button。不要把任意尾随内容强制包成一个 IconButton。
- `TopAppBarColors.actionIconContentColor` 只通过 `LocalContentColor` 提供默认颜色；现有 slot 多数显式 tint/style，因此不会自动改色。
- 现有 Demo 的添加图标使用 `contentDescription = ""` 和普通 clickable，仍会产生质量较差的语义。迁移官方容器不会修复 slot，Demo 应同步改为非空描述和 IconButton。
- 分割线是纯视觉装饰，不应暴露点击或额外朗读节点。

## 颜色与排版

完整兼容需要显式映射：

```kotlin
TopAppBarDefaults.topAppBarColors(
    containerColor = background,
    scrolledContainerColor = background,
)
```

否则无滚动时官方默认 `surface` 已不同于仓库 `surfaceContainerLowest`；启用 pinned 行为并滚动后还会动画到 `surfaceContainer`。导航与尾随颜色可以先让现有 slot 自己决定，未来若统一为 Material IconButton 再评估 `navigationIconContentColor` / `actionIconContentColor`。

标题必须继续显式传 `AppText.Bold.Title.large`。官方小型 token 使用 Material `titleLarge`，虽然通常也是 22 sp，但字重、颜色和主题覆盖不保证等于仓库 Bold 22 sp。官方 `ProvideContentColorTextStyle` 会合并 LocalContentColor 与默认 token；title slot 内显式完整 TextStyle 会优先保留仓库风格。

## 滚动行为

官方提供：

- `pinnedScrollBehavior`：高度保持，跟踪内容覆盖并驱动背景色状态；
- `enterAlwaysScrollBehavior`：上滚隐藏、下滚立即出现；
- `exitUntilCollapsedScrollBehavior`：上滚折叠，内容回到顶部后再展开；
- `TopAppBarState` 与 `nestedScrollConnection`，调用方需在共同父容器安装 `Modifier.nestedScroll(...)`。

现有 `NavBar` 没有滚动参数，也没有暴露 state，因此兼容迁移首步应传 `scrollBehavior = null`。不要在内部默认创建 pinned behavior：没有把 nestedScrollConnection 交给宿主时它无法正确跟踪内容，而且会引入滚动背景色变化。

后续若 Demo 需要展示官方功能，建议在官方 Top App Bar 独立场景中直接演示三种 behavior；若要给自有 `NavBar` 增加滚动，应新增可选参数或新重载，并让调用方负责把连接安装到滚动容器。滚动连接本身是适合 Modifier 的行为，但它已经由官方 API 表达，无需自建扩展。

## 公开 API 差异矩阵

| 方面 | 当前 `NavBar` | 官方能力 | 兼容迁移处理 |
| --- | --- | --- | --- |
| 标题输入 | `String` | Composable slot | 兼容层继续创建 Text |
| 标题对齐 | 任意二维 `Alignment` | 稳定 API 分 Start / Center | 映射实际两种用法；其他值保留旧分支或后续收窄 |
| 内容高度 | 精确 60 dp | 默认 64 dp，可传有限自定义高度 | 传 60 dp；极端大内容可能扩高 |
| 背景 | 默认 `surfaceContainerLowest` | 默认 `surface`，滚动后 `surfaceContainer` | 两个容器颜色都映射为 `background` |
| 状态栏 | 可选，仅顶部 systemBars 空白 | 默认 Top + Horizontal visual-component Insets | true 用官方、false 用零 Insets；记录水平安全区改进 |
| 返回 | 固定旧 Image + 40 dp clickable | navigation slot，推荐 48 dp IconButton | 保留图形风格，委托 IconButton 交互；补 RTL 与本地化 |
| 右侧 | 任意无接收者 Composable | `RowScope.actions` | 兼容层调用旧 slot；子内容仍自负语义 |
| 长标题 | 固定 16/59 dp 对称边距，可能撞右侧 | 按真实导航/actions 宽度约束 | 接受防重叠改进，截图确认不对称场景 |
| RTL | Row/padding 的 start 会翻转，图形不镜像 | 布局使用 start/end + placeRelative | 官方接管布局，返回 painter 另补镜像 |
| 分割线 | 可选底部 Divider | 不提供相同布尔项 | 包装层继续组合 |
| 排版 | Bold 22 sp 自有标题色 | Material titleLarge token | 标题 slot 保留旧 TextStyle |
| 语义 | 分散 clickable，返回描述写死 | Top Bar traversal group；按钮语义由 slot 决定 | 返回使用 IconButton，修复描述；审查自定义 actions |
| 滚动 | 不支持 | pinned / enter-always / exit-until-collapsed | 首步关闭；官方 Demo 展示；新 API 再暴露 |

## 是否改成 Modifier

`NavBar` 不应改成 Modifier 扩展：

1. Modifier 不能创建标题、导航图标、actions 或底部分割线等组合内容。
2. Insets、背景和语义需要围绕多个子节点共同布局，而非附着在调用方已有的单一节点上。
3. 公开 API 的核心价值是把页面级 slot 和视觉约定组合起来，仍然属于 Composable 组件职责。

适合 Modifier 的行为只有：

- 宿主滚动容器上的 `Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)`，直接复用官方 API；
- 若项目将来需要独立的“底部分割线”装饰，可以考虑通用 draw/布局 Modifier，但当前已有 `HorizontalDivider`，为一个布尔参数新增 Modifier 不会降低复杂度；
- 返回点击仍应由官方 `IconButton` 承担，不建议降级成自定义 `Modifier.clickable` 扩展。

## 建议实施与 Demo 回归

1. 保持 `NavBar` 签名和默认值，内部仅对 `Center` / `CenterStart` 两条已使用路径委托官方组件；高度固定传 60 dp，颜色和 TextStyle 显式映射。
2. 将返回节点改为 IconButton，补可本地化描述与 RTL 自动镜像；保留 `onClickBack == null` 时不生成导航节点。
3. 继续在外层组合可选 Divider；确认 `background` 覆盖状态栏安全区和内容栏，Divider 颜色仍使用仓库现有实现。
4. 扩充独立 `NavBarScreen`：默认/无 Insets、居中/左对齐、无返回/有返回、分割线、自定义背景、图标 action、文字 action、超宽 action、长中英文标题、字体缩放、LTR/RTL。
5. 在官方组件区单独展示原生 64 dp `TopAppBar`、`CenterAlignedTopAppBar` 和 pinned / enter-always / exit-until-collapsed，不把官方特性伪装成现有 `NavBar` 已支持。
6. Android、JVM、JS、Wasm、iOS Simulator 五目标编译；390 x 844 浏览器检查长标题、省略与 actions 不重叠；Android/iOS 真机或模拟器检查顶部和横屏安全区。
7. 交互检查返回/尾随点击次数、Tab/Enter/Space、读屏遍历顺序和 RTL。浏览器 Canvas 的可访问性桥接结果必须如实记录，不能由 commonMain 语义源码推断已跨平台验证。

## 官方来源

- [Compose Multiplatform Material 3 `1.11.0-alpha07` 源码构件](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07-sources.jar)：项目精确版本的 `AppBar.kt`、`IconButton.kt`、`Scaffold.kt`、`AppBarSmallTokens.kt` 和 `AppBarTokens.kt`；本地 sources JAR SHA-256 为 `1577fbd82c0c31f88d992cd2ef3b14fd27d36a98e743e991dda03da5071fd4ae`。
- [Compose Multiplatform Material 3 `1.11.0-alpha07` POM](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07.pom)：确认 Multiplatform 版本及 AndroidX Material 3 `1.5.0-alpha17` 运行时关系。
- [AndroidX Material 3 `AppBar.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/AppBar.kt)：Top App Bar API、测量布局、Insets、颜色、语义与滚动实现浏览入口。
- [AndroidX Material 3 `IconButton.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/IconButton.kt)：48 dp 最小触控目标、Button role、clickable 与 ripple 实现。
- [Android Developers：Compose App bars](https://developer.android.com/develop/ui/compose/components/app-bars)：Top/Center/Medium/Large App Bar 和滚动行为的官方使用说明与示例入口。
- [Material Design 3 Top app bar 指南](https://m3.material.io/components/top-app-bar/overview)：组件用途、布局与交互规范入口。

以上版本相关结论以项目 Gradle 缓存中的官方 `1.11.0-alpha07` sources JAR 为准；`androidx-main` 链接便于浏览上游源码，但可能随时间继续演进。
