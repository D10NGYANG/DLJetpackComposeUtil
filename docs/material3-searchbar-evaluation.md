# Material 3 SearchBar 替代评估

> 评估日期：2026-08-12  
> 项目版本：`org.jetbrains.compose.material3:material3:1.11.0-alpha07`  
> 范围：`library/.../ui/form/Search.kt`，不包含 `MenuInput` / `ListMenuItem` 的下拉菜单评估

## 结论

现有 `Search` **不应直接整体替换为官方完整 `SearchBar`，也不适合改成 Modifier 扩展函数**。

现有组件本质是固定高度的行内搜索输入：38dp 输入区放在 65dp 行中，可选清除、loading 和右侧文字动作；它没有“展开”和“搜索结果内容”模型。官方 `SearchBar` 则是一套由折叠输入框、`SearchBarState`、全屏或 Docked 展开容器、返回处理和结果 `content` 共同组成的搜索体验。两者职责并不相同。

推荐渐进处理：

1. 暂时保留 `Search` 作为兼容的“行内 SearchField”，不要用完整 `SearchBar` 强行保持旧外观。
2. Demo 新增独立的官方 SearchBar 页面，完整展示折叠、全屏展开、Docked 展开、建议、结果、空态、IME Search、清除和返回。
3. 若库确实需要完整搜索体验，新增显式暴露 `TextFieldState` / `SearchBarState` 和结果 `content` 的新 API；验证成熟后再弃用旧 `Search`。
4. 旧 API 若只做内部现代化，应优先评估官方 `TextField` / `BasicTextField` 装饰能力，而不是把 `SearchBarDefaults.InputField` 当作无状态皮肤。

## 当前实现与调用面

CodeGraph 定位结果：

- 公开实现只有 [`Search`](../library/src/commonMain/kotlin/com/d10ng/compose/ui/form/Search.kt#L60)，公开参数顺序为 `value`、`onValueChange`、`label`、`placeholder`、`align`、`disabled`、`loading`、`shape`、`backgroundColor`、`actionText`、`actionTextStyle`、`onClickAction`。
- 生产源码未发现外部调用；当前覆盖面主要是 [`SearchScreen`](../composeApp/src/commonMain/kotlin/com/d10ng/compose/demo/pages/SearchScreen.kt#L35) 和同文件 Preview。
- `Search` 内部委托自有 [`Input`](../library/src/commonMain/kotlin/com/d10ng/compose/view/Input.kt#L68)，后者使用 `BasicTextField`，支持 `KeyboardOptions` / `KeyboardActions`，但 `Search` 没有把这些能力向外暴露。

现有行为契约：

- 外层固定 65dp；输入容器固定 38dp；左侧有默认 padding，右侧无动作时保留 16dp。
- `value` 是受控 `String`；获得焦点且内容非空时显示清除按钮。
- `loading=true` 时以 20dp 进度圈替代清除按钮，但仍允许输入。
- `disabled=true` 只禁用文本输入并隐藏清除按钮；右侧动作按钮仍可点击。
- `label` 非空时用纯文本替换搜索图标；`align` 可设置输入文本对齐。
- 没有 `ImeAction.Search`、提交回调、展开状态、建议/结果内容、空结果反馈或返回收起行为。
- 清除图标是 20dp 的 `clickable`，没有使用 `IconButton` 的标准触控目标；搜索图标描述为硬编码英文 `search`。

## 项目实际 Material 3 API

本节以 Gradle 缓存中的官方源码构件 `material3-1.11.0-alpha07-sources.jar` 为准，而不是套用更新版 AndroidX API。该构件的 `commonMain/androidx/compose/material3/SearchBar.kt` 包含以下仍标记为 `ExperimentalMaterial3Api` 的能力：

- 新状态 API：`SearchBarState`、`rememberSearchBarState`、折叠 `SearchBar(state, inputField)`、`ExpandedFullScreenSearchBar` 和 `ExpandedDockedSearchBar`。
- `SearchBarDefaults.InputField(textFieldState, searchBarState, onSearch, ...)`：使用 `TextFieldState`，支持输入/输出转换、键盘参数、单行或多行限制。
- 兼容 API：`SearchBar(inputField, expanded, onExpandedChange, content)`、`DockedSearchBar(...)`，以及受控 `query` 版 `InputField`。直接接收 `query` 的完整 `SearchBar` / `DockedSearchBar` 重载已被弃用。
- Expressive API：`ExpandedFullScreenContainedSearchBar`、`ExpandedDockedSearchBarWithGap`，额外标记 `ExperimentalMaterial3ExpressiveApi`，不宜成为稳定公共封装的默认依赖。

一个关键实现约束是：新折叠 `SearchBar(state, inputField)` 会通过 `InterceptPlatformTextInput` 禁用软键盘，必须与一个 Expanded SearchBar 同时使用。它不是可以单独替换现有行内输入框的容器。

官方输入交互包含：

- 强制合并 `ImeAction.Search` 并调用 `onSearch`。
- 触摸模式下，输入获得焦点会展开；收起后清理焦点。
- 非触摸/硬件键盘模式下，开始输入或按方向下键展开；已展开时方向下键将焦点移向建议列表。
- 展开容器首次出现时请求输入焦点；收起时主动隐藏软键盘。
- 全屏旧状态重载使用预测性返回处理；Docked 使用 BackHandler 收起。新状态 Docked 实现也通过 BackHandler 动画收起。

## 特性差异

| 维度 | 现有 `Search` | Material 3 1.11.0-alpha07 | 迁移影响 |
| --- | --- | --- | --- |
| 文本状态 | 受控 `String` | 推荐 `TextFieldState`；仍有受控 query InputField | 直接改状态类型会破坏 API；兼容层同步 `TextFieldState` 容易影响光标/选区 |
| 搜索状态 | 无，仅焦点布尔值 | `SearchBarState` 或 `expanded` + 回调 | 完整迁移必须新增并提升状态所有权 |
| 提交 | 无 | 固定 `ImeAction.Search` + `onSearch` | 官方能力更完整，但旧 API 没有对应回调 |
| 焦点 | 焦点只控制清除按钮 | 触摸焦点展开；键盘输入/方向下展开；收起清焦点 | 行为变化明显，不能静默替换 |
| 返回 | 无组件内行为 | 全屏返回/预测性返回、Docked 返回均收起 | Android 预测性返回是增益；其他平台需实机验证 |
| 结果内容 | 无 | `content: ColumnScope.() -> Unit` | 官方只提供容器，过滤、建议、结果仍由业务提供 |
| 空态 | 无 | 没有内建“无结果”文案或视图 | Demo 和新 API 必须显式渲染空态 |
| loading | 内建 20dp 进度圈 | 无专用参数，可由 trailingIcon 自定义 | 可薄适配，但需规定 loading 时清除按钮和输入是否可用 |
| 清除 | 聚焦且非空时显示 | 无内建策略，可由 trailingIcon 自定义 | 应改用 IconButton 并提供本地化描述 |
| 左侧标签 | 文本替换图标 | `leadingIcon` 插槽 | 可以复现，但“标签作为图标”不是官方搜索语义 |
| 右侧动作 | 输入容器外 TextButton | 输入仅有 trailingIcon；AppBar 另有 actions | 保持旧布局需继续使用外层 Row，不能仅靠 SearchBar 参数映射 |
| 禁用 | 禁止编辑；右侧动作仍可用 | InputField `enabled=false` 同时提供禁用视觉与无障碍状态 | 输入可映射；外部动作是否禁用仍需保持旧契约或明确改动 |
| 文本对齐 | `TextAlign` 参数 | 状态版 InputField 可由 `textStyle` 映射 | 可映射；受控 query 版没有直接 textStyle 参数 |
| 形状/颜色 | 任意圆角和单一背景色 | `Shape` + SearchBarColors + TextFieldColors | 可映射，但要同时配置容器与 focused/unfocused/disabled 输入色 |
| 高度 | 输入 38dp，整行 65dp | token 输入高度 56dp | 视觉和页面密度发生明显变化 |
| 宽度 | weight 填充剩余空间 | InputField `sizeIn(360.dp..720.dp)`；Docked 旧布局固定 360dp | 390px 手机叠加右侧动作后必须专项测量 |
| 展开布局 | 无 | 手机全屏；中大屏推荐 Docked；Docked 内容最小 240dp | 需要响应式策略，不能在每个 CellGroup 中展开空白结果区 |
| Insets/IME | 无专门处理 | 全屏处理 safe drawing，Docked 使用 imePadding | 官方更完整，但跨平台窗口和输入法行为必须五目标验证 |
| 语义 | 依赖 BasicTextField；图标描述硬编码 | 本地化“搜索”描述，展开时提供“有建议”状态描述，disabled 传给 BasicTextField | 官方输入语义更完整；建议项、空态和自定义图标仍需调用方补齐 |
| API 稳定性 | 本库公开稳定 API | SearchBar 主路径仍为 Experimental；部分 Expressive API 双实验 | 不宜把官方实验类型无隔离地扩散到稳定公共 API |

## 为什么不能做成 Modifier

Modifier 适合给已有 UI 节点附加尺寸、绘制、语义、焦点或输入手势，但不能替代这里的核心职责：

- 创建并装饰可编辑的 `BasicTextField`；
- 发出 leading/trailing/action 等多个子节点；
- 在清除按钮和 loading 之间切换；
- 管理搜索文本、展开动画、Popup/Dialog 结果容器和返回；
- 提供建议/结果 `content` 插槽。

因此 `Search` 应继续是 Composable。最多只把高度、背景、焦点监听等可复用的单节点行为提炼为内部 Modifier；这不会消除组件本身。

## 推荐迁移设计

### 第一阶段：兼容旧行内搜索

- 保留现有 `Search(...)` 的参数顺序、默认值和 38dp/65dp 布局契约。
- 不把完整 SearchBar 塞进旧组件，也不固定传入 `expanded=false`：官方 InputField 在焦点与收起状态间有联动，常量 false 会触发清焦点，无法成为普通输入框。
- 可独立修复现有组件的明显缺口：增加可选 Modifier/IME Search 能力时使用新重载而非插入参数；清除改为标准按钮语义；避免 `remember(value, isFocus)` 包装简单布尔表达式。
- 如果要求“委托官方视觉”，应接受高度改为 56dp 的迁移版本，并作为新 API/新样式发布，不能宣称保持旧视觉兼容。

### 第二阶段：新增完整搜索体验

建议新增职责明确的 API，而不是继续膨胀旧 `Search`。新 API 至少应显式暴露：

- `TextFieldState` 与 `SearchBarState`；
- `onSearch`；
- `enabled`、placeholder、leading/trailing 插槽；
- 全屏 / Docked 展开策略；
- `content: ColumnScope.() -> Unit`；
- 调用方定义的 loading、空结果和错误状态。

库若要隔离实验 API，可让包装层持有官方状态，公共面提供自有稳定状态接口；代价是以后官方 API 变化时需要维护状态桥接。若使用方本来就能接受实验 API，则直接在 Demo/业务使用官方组件比再包一层更透明。

### 第三阶段：决定弃用

收集实际调用后再决定：

- 只做列表过滤、永不展示建议的场景迁移到明确命名的 `SearchField`；
- 需要建议、历史记录、结果和返回收起的场景迁移到官方 SearchBar 体验；
- 当旧 `Search` 无剩余调用时再弃用，避免把两类需求继续混在同一 API 中。

## Demo 与验证要求

官方 SearchBar 页面建议至少覆盖：

1. 折叠态、触摸展开、全屏结果和返回收起。
2. Docked 建议列表，键盘方向下移动焦点，点击建议回填。
3. IME Search 提交及提交次数显示。
4. 非空查询有结果、无结果空态、loading 和错误态。
5. 清除按钮、disabled、只读，以及焦点收起后软键盘隐藏。
6. 390x844 窄屏，特别检查 360dp 最小宽度与右侧动作并排时的测量。
7. Android、JVM、JS、Wasm、iOS Simulator 五目标编译；Android 验证预测性返回，桌面验证 Esc/方向键，Web/Wasm 记录 Canvas 无障碍桥实际暴露结果，iOS 验证键盘和返回/弹层行为。

旧 `Search` 页面还应补充清除回调、disabled 时输入与动作差异、loading 期间输入、IME 行为、长 placeholder/label、窄屏右侧动作和清除按钮触控目标，避免只展示静态样式。

## 跨平台边界

当前源码中的 SearchBar 主体位于 `commonMain`；本机 Gradle 缓存同时存在 Desktop、JS、Wasm、iOS Simulator 构件，Skiko 实现用 `LocalWindowInfo.containerSize` 计算 Docked 最大高度。这证明 API 可用于项目各目标编译，但不证明运行时交互完全一致。

- 预测性返回是 Android 系统能力，不应等同为 iOS 边缘返回、桌面 Esc 或浏览器历史返回。
- Popup/Dialog 的焦点、窗口边界和软件键盘控制由平台实现决定，必须逐平台验证。
- Web/Wasm 的 Compose Canvas 语义桥可能无法完整反映 Compose disabled、editable 和状态描述；浏览器 AX 结果应作为实测记录，不能由 commonMain 代码直接推断。
- 官方 InputField 内置的键盘方向导航主要针对非触摸输入模式；触屏设备与桌面键盘应分别覆盖。

## 一手来源

- 项目实际源码构件：[JetBrains Compose Material3 1.11.0-alpha07 sources](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07-sources.jar)，重点文件 `commonMain/androidx/compose/material3/SearchBar.kt`、`tokens/SearchBarTokens.kt`、`tokens/SearchViewTokens.kt`、`skikoMain/.../SearchBar.skiko.kt`。
- Android Developers：[Search bar 组件指南](https://developer.android.com/develop/ui/compose/components/search-bar)。该页面用于核对官方设计意图；具体签名以项目锁定的 JetBrains 构件为准。
- Android Developers：[Material 3 SearchBar API](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#SearchBar(androidx.compose.material3.SearchBarState,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Shape,androidx.compose.material3.SearchBarColors,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp))。AndroidX 当前页面可能领先 Compose Multiplatform 版本，不能直接复制未进入 1.11.0-alpha07 的 API。
- Material Design 3：[Search overview](https://m3.material.io/components/search/overview)。
