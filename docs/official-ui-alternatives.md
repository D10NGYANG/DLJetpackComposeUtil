# 官方 Compose UI 替代方案评估

> 调研截止：2026-08-10  
> 仓库基线：Compose Multiplatform 1.11.1、`org.jetbrains.compose.material3:material3:1.11.0-alpha07`  
> 范围：`library/src/commonMain` 的公开 UI API，以及 Android 专属 `ImagePreview`。仅使用 JetBrains、Android Developers、AndroidX/AOSP 一手资料。

## 结论摘要

当前库不应整体废弃。更合适的方向是把它从“重写通用控件”收敛为“项目设计系统薄封装 + 少量领域组件”：

1. `HorizontalDivider`、`VerticalDivider`、`BadgeBox`/`Badge`、自绘 `Checkbox` 等已有成熟官方等价物，应直接迁移。
2. `Button`、`Switch`、`RadioCell`、`Cell`/`ListItem`、`NavBar`、Dialog/Sheet builders 已经或可以建立在 Material3 上，保留 API 价值但不再维护底层交互，结论为薄封装。
3. `Stepper`、`IndexBar`、`Steps`、`BDSignalBeamBox`、通用任意内容 `Popover` 等没有官方等价物，继续保留，但必须补齐 semantics、键盘操作与 UI 测试。
4. 官方最新 Material3 已出现与本库滚轮 `Picker` 高度重合的 `ScrollField`，但当前 CMP 1.11.1 所映射的 Material3 1.5.0-alpha17 尚未包含它。CMP 1.12.0-beta03 映射到 AndroidX Material3 1.5.0-alpha22，而 `ScrollField` 的可访问性完善从 alpha23 延续到 alpha25，因此当前应保持“待定”，不要为了它把生产基线切到预发布版。
5. 官方 DatePicker/TimePicker 不是本库滚轮日期/时间控件的像素级替代：官方日期选择是日历/输入体验且使用 UTC 日界；官方时间选择目前聚焦小时和分钟，本库还支持秒及 Y/YM/MD/MS 等局部模式。应双轨迁移，而不是删除现有 API。

## 版本与平台边界

Compose Multiplatform 与 AndroidX Compose 独立发版。JetBrains 的 1.11.1 依赖表明确给出：Runtime/UI/Foundation 1.11.1 基于 Jetpack Compose 1.11.2；Material3 1.11.0-alpha07 基于 AndroidX Material3 1.5.0-alpha17；Material3 Adaptive 1.3.0-alpha07 基于 AndroidX Adaptive 1.3.0-alpha10。[CMP 1.11.1 依赖表](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html#dependencies)

本项目目标为 Android、iOS Arm64/iOS Simulator Arm64、JVM、JS、Wasm。下文标记“Common”的官方 API，可在 JetBrains 对应制品已映射的前提下用于 `commonMain`；不能因为 AndroidX 最新 API 页面已出现某组件，就假定当前 JetBrains 制品已经携带它。JetBrains 当前把 Android/iOS/Desktop 标为 Stable，Wasm Web 仍为 Beta，因此相同 common API 的实现成熟度也不能视为完全一致。[Kotlin Multiplatform FAQ](https://kotlinlang.org/docs/multiplatform/faq.html)

| 版本线 | 截止调研日的定位 | 本评估用法 |
|---|---|---|
| CMP 1.11.1 + JB Material3 1.11.0-alpha07 | 当前仓库生产基线；Material3 基于 AndroidX alpha17 | 立即迁移只能使用这条线已有 API |
| CMP 1.12.0-beta03 + JB Material3 1.12.0-alpha03 | 预发布；Material3 基于 AndroidX alpha22 | 只做 demo/兼容性观察，不建议切生产 |
| AndroidX Material3 1.4.0 | AndroidX 稳定版 | 用于判断 API 稳定趋势，不能直接替代 `commonMain` 的 JetBrains 坐标 |
| AndroidX Material3 1.5.0-alpha25 | 截止日最新 alpha；alpha23-alpha25 连续完善 `ScrollField`/TimePicker scroll variant 的可访问性与键盘行为 | 前瞻项；等待 JetBrains 后续映射 |

来源：[AndroidX Material3 发布说明](https://developer.android.com/jetpack/androidx/releases/compose-material3)、[AndroidX 全渠道版本表](https://developer.android.com/jetpack/androidx/versions/all-channel)、[CMP 1.12.0-beta03](https://kotlinlang.org/docs/multiplatform/whats-new-compose-112.html)。

另一个需要单独决策的版本事实是：JetBrains Material3 最新稳定线为 1.9.0，并在 Android target 映射到稳定 AndroidX Material3 1.4.0；本项目因使用后续 `BottomSheet + Scrim` 等能力选择了 1.11.0-alpha07。不能只因 Compose 插件稳定就忽略 Material3 alpha 风险，也不能直接降级而破坏已经采用的 Sheet API。应把“稳定 Material3”与“新 Sheet API”作为显式发布策略权衡。[JetBrains Material3 Maven metadata](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/maven-metadata.xml)、[JetBrains Material3 1.9.0 module metadata](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.9.0/material3-1.9.0.module)

官方 Material/Foundation 控件通常自带角色、状态、可点击/可切换、焦点和最小交互尺寸语义；低级自绘组件必须自行提供。Compose 官方说明 `Switch` 会暴露 `Role.Switch`、`ToggleableState` 和点击动作，`clickable`/`toggleable` 与 `ListItem` 会合并后代语义；提醒、窗格、错误、进度和集合还分别需要 `liveRegion`、`paneTitle`、`error`、`progressBarRangeInfo`、`collectionInfo` 等语义。[Compose semantics 官方说明](https://developer.android.com/develop/ui/compose/accessibility/semantics)

## 逐组件结论

### 设计系统与基础修饰符

| 本库 API | 官方覆盖 | 结论 | 差异与迁移建议 |
|---|---|---|---|
| `AppColor` / `toColorScheme()` | `MaterialTheme.colorScheme`、`ColorScheme` | **薄封装** | `toColorScheme()` 已桥接官方主题，但 `AppColor` 是全局可变对象，不会像 CompositionLocal 一样按 UI 子树更新。将标准色全部改读 `MaterialTheme.colorScheme`；仅保留 `success`、`warning/assist` 等 Material `ColorScheme` 没有的品牌扩展，并通过不可变 theme tokens/CompositionLocal 提供。Android 动态色是平台能力，不能成为 common 默认值。 |
| `AppText` / `BasicTextFontWeight` / `BasicTextFontSize` | `MaterialTheme.typography`、`Typography` | **薄封装** | 用官方 type scale 承担 display/headline/title/body/label；品牌字号只做命名扩展。不要继续让组件直接依赖全局可变颜色。 |
| `AppShape` | `MaterialTheme.shapes`、`Shapes` | **薄封装** | small/medium/large 迁到官方；2/4/6/8/10dp 等额外半径保留为设计 token。 |
| `Modifier.dashBorder` | Foundation drawing、`PathEffect.dashPathEffect` | **保留** | 没有直接的官方 `dashedBorder`。现实现正是用官方绘图原语，保留即可；增加 RTL/极小尺寸/超大 stroke 快照测试。 |
| `Modifier.debounceClick` | `Modifier.clickable` | **保留但收窄** | 官方没有通用 debounce。继续基于 `clickable` 才能保留角色、焦点、键盘和 indication；不要把所有按钮都防抖，业务异步状态优先用 `enabled/loading`。建议补 `enabled`、`role`、`onClickLabel`、`interactionSource` 参数并用 `rememberUpdatedState(onClick)`。 |
| `BackHandler` | `NavigationBackHandler` / Navigation Event | **迁移到官方** | 当前函数只是无损转发且没有额外契约，调用点可直接使用官方 API；若为兼容旧调用保留，则标记 deprecated。Navigation Event 能表达开始/进度/取消/完成，本包装只暴露完成事件，会丢失预测性返回进度。 |

官方来源：[Material3 package API](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary)、[Compose drawing API](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/drawscope/package-summary)、[Navigation Event](https://developer.android.com/jetpack/androidx/releases/navigationevent)。

### 按钮、列表与选择控件

| 本库 API | 官方替代 | 结论 | 差异与迁移建议 |
|---|---|---|---|
| `Button`、`ButtonType`、`ButtonSize` | `Button`、`OutlinedButton`、`TextButton`、`FilledTonalButton`、`ElevatedButton` | **薄封装** | 当前已经调用 Material3 `Button`。把 `plain/type` 映射成官方变体而不是只改同一 filled button 的颜色；保留 SUCCESS/WARNING/DANGER 和 loading 插槽。当前 `loading=true` 时官方仍收到 `enabled=true`，语义上仍是可用按钮但点击被内部吞掉，应改为 `enabled = !disabled && !loading`，并给进度语义。 |
| `CellTitle` / `CellGroup` / `Cell` / `CellRow` | `ListItem`、`Surface`/`Card`、`HorizontalDivider` | **薄封装** | 官方 `ListItem` 提供 leading/headline/supporting/trailing slots、最小高度和后代语义合并；外层点击用 `Modifier.clickable`。保留 required、箭头方向、inset group 等产品语义。 |
| 两个 `ListItem` 重载 | Material3 `ListItem` | **迁移到官方** | composable-slot 重载几乎完全重复官方槽位；字符串便利重载可保留为薄封装。注意官方 `ListItem` 本身不替业务决定点击动作，需要明确 `clickable`、role 与禁用态。 |
| `ListInputItem` | `ListItem` + `OutlinedTextField`/`TextField` | **薄封装** | 保留“表单行”组合契约，输入与错误语义交给官方 TextField。 |
| `ListMenuItem` | `ExposedDropdownMenuBox` + `DropdownMenuItem` | **迁移到官方** | 官方 exposed menu 处理 anchor、展开状态、键盘/焦点和菜单语义；当前 disabled `Input` 外层再 clickable 容易产生不一致焦点/点击节点。 |
| `Checkbox` / `CheckboxType` | `Checkbox`、`TriStateCheckbox` | **迁移到官方** | 当前是 `Box + clickable + Icon`，没有 checkbox role/toggle state，也没有 Material 最小触摸目标。方形直接用官方；圆形若确属产品要求，保留一个明确带 `Modifier.toggleable(role = Role.Checkbox)` 的视觉变体，不应继续以普通 clickable 伪装 checkbox。 |
| `RadioCell` | `RadioButton` + `ListItem` + `selectable` | **薄封装** | 内部已用官方 RadioButton，但当前 `disabled` 只禁用圆点，整行仍可点击，这是行为与无障碍 bug。整行改用 `selectable(selected, enabled, role = Role.RadioButton)`，组容器加 `selectableGroup()`。 |
| `Switch` | Material3 `Switch` | **薄封装** | 当前已正确基于官方 Switch，loading、图标和配色有保留价值。避免用 `scale()` 改交互尺寸；把视觉 size 与至少 48dp 交互目标分开。loading 时维持 disabled 语义并描述进度。 |
| `CheckButton` / 两个 `CheckButtonGroup` | `FilterChip`、`InputChip`、`SingleChoiceSegmentedButtonRow`、`MultiChoiceSegmentedButtonRow` | **迁移到官方** | 单选连续选项优先 segmented button；可换行多选优先 FilterChip + FlowRow。官方提供 selected/toggle semantics、focus、touch target、interaction。虚线 inactive 外观不是 Material 规范，只在明确设计要求时薄封装。 |
| `Tag` / `TagType` / `TagStyle` | 非交互 `Surface + Text`；交互时 `AssistChip`/`FilterChip`/`InputChip` | **薄封装** | 纯状态标签不是“可点击 chip”，应保留轻量 Surface；有 onClick 时委托官方 chip。当前无 onClick 时仍挂了 disabled clickable semantics，迁移时移除无意义动作节点。 |
| `BadgeBox`、两个 `Badge` 重载 | `BadgedBox`、`Badge` | **迁移到官方** | 直接替换布局与基础 badge；保留 `num/max` 的 `99+` 格式化便利层。给数字含义提供合适 content description，避免屏幕阅读器只读孤立数字。 |
| `HorizontalDivider` / `VerticalDivider` | 同名 Material3 组件 | **迁移到官方** | 官方组件已有 thickness/color 契约。本库 padding 参数可通过外层 Modifier 实现；当前横线先设 `height(thickness)` 再加垂直无关 padding，没必要维护自绘版本。 |

### 输入、搜索与数值控件

| 本库 API | 官方替代 | 结论 | 差异与迁移建议 |
|---|---|---|---|
| `Input` | Foundation `BasicTextField(TextFieldState)`；Material3 `TextField`/`OutlinedTextField` | **薄封装** | 若它是设计系统裸输入原语，迁到 state-based BasicTextField 并保留装饰槽；普通表单直接用 Material3 TextField。当前 `SideEffect` 每次重组请求焦点会抢回焦点，改为事件驱动；错误、label、supporting text 和 prefix/suffix 优先交给官方装饰。CMP 1.11 的 iOS native text input 仍为实验特性，默认实现才是稳定跨平台选择。 |
| `MenuInput` | `ExposedDropdownMenuBox` + TextField + `DropdownMenuItem` | **迁移到官方** | 保留 20 项过滤和业务对象映射；菜单 anchor、焦点、键盘、展开/收起交给官方。密码输入与 suggestions 应拆开，避免一个 API 同时承担 autocomplete 和 secure field。 |
| `Field` | `OutlinedTextField`/`TextField` | **薄封装** | 保留左/上 label 布局、required、尺寸回调等表单行策略；输入、clear/password icon、readOnly/enabled、supporting error 迁到官方。官方 error semantics、line limits、IME 行为比当前手工拼装完整。 |
| `Search` | `SearchBar` / `DockedSearchBar`，或 `SearchBarDefaults.InputField` | **迁移到官方** | 官方提供 active/expanded 状态、suggestions content、键盘、焦点、预测性返回和 Material 动画；本库 loading/cancel 文案可做薄封装。只需要一行过滤输入时可用官方 InputField，不必强制完整 SearchBar。 |
| `Stepper` / `StepperDefault` / `StepperRound` | 无直接 Material3 等价物；可组合 `IconButton + TextField` | **保留** | 业务价值明确。用官方 IconButton/TextField 作为内部积木，增加 `RangeInfo`、自定义 increment/decrement accessibility actions、键盘 Up/Down、长按策略和小数步长测试。不要用 visual disable 代替真正 `enabled=false`。 |
| `Picker<T>` / `MultiPicker<T>` | 最新 Material3 `ScrollField` / 多个 ScrollField | **待定** | 官方 ScrollField 是滚轮数值/时间/单位选择，支持 item index、selected slot、accessibility descriptions；它无限循环。当前 Picker 是有限 `Set<T>`、固定 5 行、手写 snap。立即保留，并把公开输入改为稳定有序 `List<T>`；等待 JetBrains 映射 alpha23+ 后做 adapter/并排 demo，再决定是否替换。alpha23 才补上点击选中与 TalkBack 播报，alpha24/25 又继续增加 TimePicker scroll variant 和键盘/a11y 修复。 |
| `DatePicker` / `DatePickerMode` | Material3 `DatePicker`、`DatePickerDialog`、`DateRangePicker`；未来也可用 ScrollField | **待定（双轨）** | YMD 日历选择迁官方，获得 locale、可选日期、输入模式与键盘/a11y。保留 Y/YM/MD/M 滚轮模式。官方 `selectedDateMillis` 表示所选日期在 UTC 的当天开始；本库按 `currentSystemDefault()` 把 epoch 转本地日期，迁移必须用明确 LocalDate/UTC 转换，不能直接透传现有时间戳。 |
| `TimePicker` / `TimePickerMode` | Material3 `TimePicker`、`TimeInput`；未来 ScrollField | **待定（双轨）** | HM 场景优先官方；HMS/MS/M/H 与“秒”继续保留滚轮。官方状态以 hour/minute 为核心，交互是 dial/input，不等价于本库多列有限滚轮。 |

官方来源：[Text input 官方指南](https://developer.android.com/develop/ui/compose/text/user-input)、[Material3 SearchBar API](https://developer.android.com/reference/kotlin/androidx/compose/material3/SearchBar.composable)、[ScrollField API](https://developer.android.com/reference/kotlin/androidx/compose/material3/ScrollField.composable)、[DatePickerState API](https://developer.android.com/reference/kotlin/androidx/compose/material3/DatePickerState)、[TimePickerState API](https://developer.android.com/reference/kotlin/androidx/compose/material3/TimePickerState)。

### Dialog、Sheet 与反馈

| 本库 API | 官方替代 | 结论 | 差异与迁移建议 |
|---|---|---|---|
| `Overlay` | Material3 `Scrim`，或 modal 组件自带 scrim | **迁移到官方** | Dialog/Sheet 内不要重复维护全屏遮罩。独立 Overlay 若保留，应变成 Scrim 薄封装并补 pane/focus 语义。当前 pointerInput 会把内容区域点击也当成 dismiss，且 in-tree overlay 不自动建立 dialog window/focus trap。 |
| `Dialog` / `DialogBox` / `DialogColumn` | `AlertDialog`，底层 `androidx.compose.ui.window.Dialog`；未来稳定 `BasicAlertDialog` | **薄封装** | 内容容器和 builder API可保留；外壳迁到官方 modal。官方负责 dialog semantics、焦点、返回/外点关闭、窗口层和平台行为。当前映射的 alpha17 中 `BasicAlertDialog` 仍是 experimental，复杂自定义 dialog 近期优先使用稳定 UI `Dialog`；若当前“多个 in-tree dialog 叠放”是硬需求，需要先定义 stack policy，不能机械替换。 |
| `TipsDialogBuilder` / `ConfirmDialogBuilder` | `AlertDialog` | **薄封装** | title/text/confirm/dismiss slots 完全匹配；Success/Warning/Danger 是品牌主题层。异步按钮返回 Boolean 的关闭策略可保留，但执行中要禁用并显示进度。 |
| `InputDialogBuilder` | UI `Dialog` + `OutlinedTextField`；未来稳定 `BasicAlertDialog` | **薄封装** | 保留多字段验证和 suspend 回调；字段改官方 TextField，错误写入 supportingText + `isError/error semantics`，并定义初始焦点与 IME Next 顺序。当前不要为了 BasicAlertDialog 让公开 API 感染 experimental 标记。 |
| `ProgressDialogBuilder` | UI `Dialog` + `CircularProgressIndicator`/`LinearProgressIndicator` | **薄封装** | 官方没有单独 ProgressDialog；本库组合仍有价值。确定/不确定进度分别设置 progress semantics，阻塞模式要有可取消策略。 |
| `ResultDialogBuilder` | UI `Dialog` + Icon/Text/Button | **薄封装** | 属于产品模式，不是基础组件；保留模板，外壳和按钮用官方。 |
| `Sheet` / `SheetBox` / `SheetColumn` | `BottomSheet + Scrim`、`ModalBottomSheet`、`BottomSheetScaffold` | **薄封装（已迁移外壳）** | 当前 `Sheet` 已使用官方同树 BottomSheet/Scrim 状态机，方向正确。继续保留多 Sheet 管理、Builder 内容和品牌容器；持续验证强制模式、预测性返回、IME、Insets、焦点与叠放顺序。 |
| `SheetBuilder` / `TitleBar` / `ActionSheetBuilder` / `RadioSheetBuilder` | BottomSheet 内容 + `ListItem`/`RadioButton`/`DropdownMenuItem` | **薄封装** | builder 是业务组合层；内部列表项迁官方，避免自写点击/选择语义。 |
| `SinglePickerSheetBuilder` / `MultiPickerSheetBuilder` | BottomSheet + 当前 Picker；未来 ScrollField | **待定** | Sheet 外壳已官方化；内容随 Picker 的版本门控策略。 |
| `DatePickerSheetBuilder` / `TimePickerSheetBuilder` | `DatePickerDialog`/TimePicker dialog，或 BottomSheet + picker | **待定（双轨）** | 日历/时间拨盘场景直接用官方 dialog；滚轮 bottom sheet 是不同产品体验，保留并明确命名。 |
| `NormalToast` / `Toast(type=Normal)` | `SnackbarHostState` + `SnackbarHost` | **迁移到官方** | SnackbarHost 管理队列、duration、action、dismiss 与无障碍播报；当前全屏 Box 的 top/center/bottom toast 没有 live region，且并发队列由全局 ViewModel 自管。位置视觉会变化，应接受 Material 规范或明确保留 legacy 模式。 |
| `IconToast` / `LoadingToast` / Success/Fail Toast | Snackbar、UI `Dialog`、blocking progress overlay | **薄封装并拆分职责** | 成功/失败非阻塞反馈可用带图标 Snackbar；阻塞 loading 不应叫 Toast，改为 `BlockingProgressOverlay` 或 dialog。`forbidClick`/吞返回是模态行为，必须提供 pane/progress semantics 和取消策略。 |
| `Notify` | 无完整 Material3 Banner；可选 Snackbar | **保留** | 顶部持久通知条与 Snackbar 行为不同。保留时改用 Surface，提供 dismiss/action slot，并设置 polite/assertive liveRegion；不要只靠颜色表达 Primary/Success/Warning/Error。 |

官方来源：[Material3 Dialog API](https://developer.android.com/reference/kotlin/androidx/compose/material3/BasicAlertDialog.composable)、[ModalBottomSheet CMP API](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-modal-bottom-sheet.html)、[SnackbarHostState](https://developer.android.com/reference/kotlin/androidx/compose/material3/SnackbarHostState)、[预测性返回的 Material 支持](https://developer.android.com/develop/ui/compose/system/predictive-back-setup)。

### 导航、展示与领域组件

| 本库 API | 官方替代 | 结论 | 差异与迁移建议 |
|---|---|---|---|
| `NavBar` | `TopAppBar` / `CenterAlignedTopAppBar` | **迁移到官方** | 官方处理 status bar insets、title/actions 约束、滚动行为、RTL、最小交互尺寸与 app bar 语义。保留当前 title/right/back 便利 API 为 deprecated adapter，避免与底部 `NavigationBar` 名称混淆，建议改名 `AppTopBar`。 |
| `IndexBar` / `DefaultIndexBarCharText` / `DefaultIndexBarSelectText` | 无直接等价物 | **保留** | Foundation 只提供 pointer input/lazy list 积木。增加每个字符的可点击/可选择 semantics、键盘上下/首字母跳转、collection info；当前只响应 pointer Press/Move/Release，对键盘、屏幕阅读器和 Cancel 事件不完整。 |
| `Avatar` | 当前稳定 Material3 无通用 Avatar 组件 | **保留** | 继续用 official Image/Surface/clip 原语；把 decorative 与 meaningful image 的 contentDescription 交给调用方，补加载失败/placeholder。若引入 Coil 3，应保持 common API。 |
| `Popover` / `PopoverColumnItems` / `PopoverColumnItem` | 提示用途：`TooltipBox` + `PlainTooltip`/`RichTooltip`；菜单用途：`DropdownMenu`；任意内容：Foundation `Popup` | **薄封装** | 不再用一个 API 同时表示 tooltip、menu 和任意 popup。短说明迁 Tooltip（官方覆盖 long-press、mouse hover、keyboard focus）；可操作列表迁 DropdownMenu；只有带自定义箭头和任意内容的 popover 保留 Popup 实现。当前 alpha17 尚缺后续版本对小窗口/大字体边界 clamp 的修复，仍需做边缘定位回归。 |
| `Steps` / `Step` / `StepMode` | 无 Material3 step indicator | **保留** | 属于进度/流程展示。补 `progressBarRangeInfo` 或 collection/item state descriptions，不能只用颜色区分 active/running/inactive。 |
| `BDSignalBeamBox` | 无官方领域组件 | **保留** | 明确是卫星/波束领域可视化。为整图增加可理解的 summary semantics；Canvas 子元素如需逐项探索，使用 collection/item semantics 或旁边数据列表。 |
| Android `ImagePreview` | Foundation `transformable`/`detectTransformGestures`/`graphicsLayer`，无完整官方图片预览器 | **待定** | 保留 Android 实现，但底层尽量收敛到 Foundation 手势；官方积木不提供完整查看器的边界、双击缩放、关闭、图片加载和系统栏。若组件库定位为 CMP，需决定 iOS/Desktop/Web 是否提供 expect/actual 或将其移出通用组件清单。 |

官方来源：[Material3 TopAppBar API](https://developer.android.com/reference/kotlin/androidx/compose/material3/TopAppBar.composable)、[CMP TooltipBox API](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-tooltip-box.html)、[Compose 多点触控](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/multi-touch)、[CMP Popup API](https://kotlinlang.org/api/compose-multiplatform/compose-ui/androidx.compose.ui.window/-popup.html)。

## Adaptive 的关系

Material3 Adaptive 不是上述原子控件的替代品，而是 demo/app shell 目前缺少的一层。官方建议 canonical list-detail、feed、supporting-pane 模式，并通过 `WindowSizeClass` 在 compact/medium/expanded 间适配。CMP 1.11.1 对应依赖为：

```kotlin
commonMain.dependencies {
    implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.3.0-alpha07")
}
```

这里存在需要实测解决的官方资料冲突：JetBrains Adaptive 指南要求把上述依赖放入 `commonMain`，制品 metadata 也发布了 Android/Desktop/iOS/JS/Wasm variants；但 JetBrains 的 Android-only components 页面又把 `material3-adaptive` 列为 commonMain 不可用。因而 Adaptive 不能直接列为首批迁移承诺，先做独立 spike，并以本仓库五类 target 的真实编译和运行结果为准；fold posture 等能力也不能假定各平台等价。[Adaptive module metadata](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/adaptive/adaptive/1.3.0-alpha07/adaptive-1.3.0-alpha07.module)、[Android-only components](https://kotlinlang.org/docs/multiplatform/compose-android-only-components.html)

建议 demo 新增一页官方 adaptive showcase，至少展示：

- `currentWindowAdaptiveInfo().windowSizeClass` 与实时尺寸分类；
- `ListDetailPaneScaffold`/`NavigableListDetailPaneScaffold` 在单窗格和双窗格间切换；
- `NavigationSuiteScaffold` 在 navigation bar/rail 等形态间切换（使用与当前 CMP 版本匹配的 JetBrains 制品）；
- 键盘、鼠标、触摸、窗口缩放以及折叠/展开 pane 的返回行为。

来源：[CMP Adaptive layouts](https://kotlinlang.org/docs/multiplatform/compose-adaptive-layouts.html)、[Jetpack Compose Adaptive 指南](https://developer.android.com/develop/ui/compose/layouts/adaptive)。

## 建议迁移顺序

1. **先修行为与无障碍缺陷**：`RadioCell.disabled` 仍可点、Checkbox 无 toggle semantics、Picker/IndexBar/Steps 无选中与集合语义、Toast/Notify 无 liveRegion、loading Button 语义仍 enabled。
2. **删除低价值重复实现**：Divider、Badge、slot 版 ListItem、ListMenuItem、Search、NavBar 改用官方组件；保留兼容 adapter 并加弃用迁移信息。
3. **统一设计系统入口**：组件内部改读 `MaterialTheme.colorScheme/typography/shapes`；品牌扩展 token 通过 CompositionLocal 提供。
4. **收敛 modal 与反馈架构**：Dialog 迁官方 modal；NormalToast 迁 SnackbarHost；阻塞 loading 与 toast 拆开；Sheet 保持当前官方状态机。
5. **保留并强化深层组件**：Stepper、IndexBar、Steps、BDSignalBeamBox、真正的任意内容 Popover，集中补键盘/a11y/跨平台测试。
6. **版本门控实验**：在独立 demo/分支用 CMP 1.12+ 对比 `ScrollField` 与 Picker；待 JetBrains 映射包含 alpha23 的可访问性修复后再决定公开 API 迁移。

## Demo 页面矩阵

后续 Demo 先按基础、表单、反馈、展示、导航、领域和平台能力建立分类目录；目录页只负责导航，不直接渲染示例。每个组件拥有独立页面，只有无法合理拆分、共享同一状态模型的同族 API 才能合并。官方组件也按单组件建页；自有组件存在官方候选时，在自有页面提供入口和差异摘要，不把多个无关组件堆入综合对照页。

| 分类 | 单组件页面示例 | 页面重点 |
|---|---|---|
| 基础 | Material Button、自有 Button、Material ListItem、自有 Cell | 每页只覆盖目标组件的全部变体、槽位和状态 |
| 表单 | Material Checkbox、自有 Checkbox、RadioCell、Switch、Input、Field | 选择/输入语义、焦点、IME、错误、只读和禁用 |
| 反馈 | Snackbar、Toast、Notify、AlertDialog、各 Dialog Builder | 队列、关闭、返回、外部点击、阻塞与非阻塞行为 |
| 展示 | Badge、Divider、Tag、Avatar、Popover | 长文本、定位、颜色、内容插槽与无障碍描述 |
| 导航 | TopAppBar、NavBar、IndexBar | Insets、RTL、长标题、键盘和集合语义 |
| 领域 | Stepper、Steps、Picker、BDSignalBeamBox | 边界数据、所有模式、键盘与领域语义 |
| 平台 | ImagePreview、Adaptive spike | 平台可用性、窗口尺寸和目标平台差异 |

每个适用状态至少覆盖：light/dark、enabled/disabled、loading/error/empty、LTR/RTL、字体放大、键盘焦点、屏幕阅读器 semantics、compact/expanded 尺寸。官方 API 页面把上述大部分 Material3 组件标为 Common，但最终仍需用本仓库 Android/iOS/JVM/JS/Wasm 编译与 UI 测试验证。[Material3 API 索引](https://developer.android.com/reference/kotlin/androidx.compose.material3/package-summary)

## 一手资料索引

- [Compose Multiplatform 1.11.1 新特性与依赖映射](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)
- [Compose Multiplatform 版本兼容说明](https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html)
- [AndroidX Material3 发布说明](https://developer.android.com/jetpack/androidx/releases/compose-material3)
- [AndroidX Material3 API 索引](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary)
- [Material3 ScrollField API](https://developer.android.com/reference/kotlin/androidx/compose/material3/ScrollField.composable)
- [Compose accessibility semantics](https://developer.android.com/develop/ui/compose/accessibility/semantics)
- [Compose Multiplatform Adaptive layouts](https://kotlinlang.org/docs/multiplatform/compose-adaptive-layouts.html)
- [Compose Multiplatform Popup API](https://kotlinlang.org/api/compose-multiplatform/compose-ui/androidx.compose.ui.window/-popup.html)
- [Compose Multiplatform 1.11.1 GitHub release](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.11.1)
