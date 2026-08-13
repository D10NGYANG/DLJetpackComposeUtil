# Material 3 / Compose UI Dialog 替代性评估

评估日期：2026-08-13
评估对象：`Dialog`、`Overlay`、`DialogBox`、`DialogColumn`、`DialogBuilder` 及其 Builder 子类
项目依赖：Compose Multiplatform Material 3 `1.11.0-alpha07`、Compose Multiplatform UI `1.11.1`

## 结论

现有 `Dialog` 外壳应迁移到稳定的 `androidx.compose.ui.window.Dialog`，不应继续用应用内容树内的 `Overlay` 模拟模态窗口。官方 UI Dialog 已负责平台弹层、可聚焦层、返回键 / Escape、内容外点击、scrim、系统与输入法 Insets、dialog 语义，以及不同平台的输入隔离。现有 `DialogBuilder`、`DialogBox`、`DialogColumn` 和提示 / 确认 / 输入 / 结果 / 进度 Builder 仍有组合价值，应保留公开 API 并只替换外壳。

现阶段不推荐用 `BasicAlertDialog` 作为兼容外壳：

- 项目精确版本的 `BasicAlertDialog` 仍标记 `ExperimentalMaterial3Api`，会让调用点或兼容包装承担实验性 API。
- 它的默认实现本身就是 UI `Dialog`，额外只增加 280--560 dp 内容约束与本地化 `paneTitle`；焦点、返回、外点、窗口层级和 Insets 都来自底层 UI Dialog。
- 当前 `DialogBox` 已约定 22 dp 屏幕边距、最大 560 dp、12 dp 圆角、25 dp 内边距和白色背景。直接叠加 BasicAlertDialog 的最小 / 最大宽度会改变窄屏测量，但不会替代这些品牌样式。

推荐第一步保持 `Dialog(builder, id)`、`UiViewModelManager.showDialog/updateDialog/hideDialog` 和所有 Builder 签名不变，内部改用 UI Dialog：

```kotlin
androidx.compose.ui.window.Dialog(
    onDismissRequest = { DialogBuilder.dismiss(id) },
    properties = DialogProperties(
        dismissOnBackPress = builder.clickOutsideDismiss,
        dismissOnClickOutside = builder.clickOutsideDismiss,
        usePlatformDefaultWidth = false,
    ),
) {
    // 兼容旧 contentAlignment 与 DialogBox 的 22 dp / 560 dp 宽度契约
}
```

这里把返回和外点都映射到 `clickOutsideDismiss` 不是新设计，而是保持旧行为：旧 `Overlay` 的外点与 `BackHandler` 都调用同一个 `onDismiss`，`Dialog` 再在该回调里检查这个开关。后续若要分别控制两种关闭方式，应新增明确 API，并经过弃用周期，不能在本次迁移中把默认 Back 行为静默改成可关闭。

`Dialog`、`Overlay` 和 Builder 不适合改成 Modifier。它们创建独立内容、状态 / 生命周期、语义树和平台弹层。可以抽成 Modifier 的只有容器装饰，例如 shape、背景、padding 或测试 tag；官方弹窗层本身必须由 Composable 表达。`DialogBox` 也不应整体变成 Modifier，因为它仍负责生成容器并根据可用宽度实施 margin / maxWidth 约束；可以让它内部复用 `Surface` 或私有容器 Modifier，但保留 Composable API。

## 当前实现与调用面

CodeGraph 定位到以下关系：

| 符号 | 当前职责 | 主要问题 / 兼容要求 |
| --- | --- | --- |
| `Overlay` | 在当前 Compose 树绘制全屏 scrim，手工拦截外点和 `BackHandler` | 没有建立 dialog window / scene layer、独立焦点所有者或 dialog 语义；仍适合独立视觉遮罩场景，不应再作为 Dialog 基础设施 |
| `Dialog(builder, id)` | 把 Builder 放进 Overlay，并把 id 传给 `Build` | 公开签名可保持；外壳改用 UI Dialog |
| `DialogBox` | 22 dp 水平 margin、最大 560 dp、12 dp 圆角、25 dp padding、白色背景 | 保留尺寸与视觉契约；后续主题化应单独评审 |
| `DialogColumn` | 在 DialogBox 内增加居中 Column | 保留组合便利性 |
| `DialogBuilder` | `clickOutsideDismiss`、任意 `contentAlignment`、`Build(id)`、全局 dismiss | 保持兼容；首步把同一开关映射到 Back 与外点 |
| `UiViewModel.dialogBuilderMapFlow` | 以随机 id 保存多个 Builder，Map 顺序逐个渲染；支持按 id 更新 / 删除 | 必须显式验证多层顺序、只关闭顶层、移除中间层和更新后层级稳定性 |

仓库当前有 17 处 `showDialog` 文本调用，其中 13 个 Demo 场景通过公开管理器展示 Builder；库内没有直接业务调用。Builder 子类覆盖：

- `TipsDialogBuilder`：标题、文本、自定义内容和单按钮；
- `ConfirmDialogBuilder`：品牌语义类型、确认 / 取消与挂起回调；
- `InputDialogBuilder`：一个或多个输入、校验、错误反馈和 IME；
- `ResultDialogBuilder`：成功 / 失败图标、内容和按钮；
- `ProgressDialogBuilder`：确定 / 不确定进度及 `updateDialog` 原位更新。

官方 `AlertDialog` 只能直接覆盖提示 / 确认的常规布局，不能替代全局 id 管理、挂起回调返回 Boolean、多字段校验、结果模板、进度更新或任意内容对齐。正确边界是“官方 modal 外壳 + 自有 Builder 组合”，不是删除 Builder。

## 精确版本的官方实现

### BasicAlertDialog 做了什么

Material 3 `1.11.0-alpha07` 中：

```kotlin
@ExperimentalMaterial3Api
@Composable
fun BasicAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
)
```

默认 override 直接调用 UI `Dialog`，然后用 Box 添加：

- `sizeIn(minWidth = 280.dp, maxWidth = 560.dp)`；
- `paneTitle = getString(Strings.Dialog)`；
- `propagateMinConstraints = true`。

它不提供 Surface、shape、背景、padding 或按钮；调用方仍需自行定义样式。Material 3 `AlertDialog` 才进一步组合 Surface、标题、正文、图标和自适应按钮 FlowRow。项目的 Input / Progress / Result Builder 属于 BasicAlertDialog 的“任意 content”用例，但使用 UI Dialog 可以获得相同的核心 modal 行为，同时保持稳定 API 和现有测量契约。

### commonMain 可用的 DialogProperties

项目的 `commonMain` 能稳定使用三个公共属性：

| 属性 | 官方语义 | 兼容映射 |
| --- | --- | --- |
| `dismissOnBackPress` | Android Back / Escape、Desktop Escape 等触发 `onDismissRequest` | 首步映射 `clickOutsideDismiss` |
| `dismissOnClickOutside` | 主指针在内容 bounds 外完成点击时触发 `onDismissRequest` | 首步映射 `clickOutsideDismiss`；若为兼容任意对齐而使用全屏内容根节点，则需由根节点自行区分内容内外 |
| `usePlatformDefaultWidth` | 限制为平台默认的较窄内容宽度 | 设为 `false`，让现有 `DialogBox` 继续以窗口宽度减 44 dp、最大 560 dp 测量 |

平台 actual 还有额外能力，但不能直接从当前 commonMain 构造器统一配置：

- Android：`securePolicy`、`decorFitsSystemWindows`、`windowTitle`、`windowType`、`windowToken`；
- Skiko（Desktop / iOS / JS / Wasm）：`usePlatformInsets`、`useSoftwareKeyboardInset`、`scrimColor`、`animateTransition`。

如果库未来要公开安全窗口、透明 scrim、动画或平台 Insets 控制，应设计 expect / actual 配置适配器或平台扩展，而不是把某一 actual 的参数误写进 commonMain API。

### 平台弹层并不都等于原生 OS 窗口

| 目标 | 精确版本实现 | 可依赖的行为 | 不能过度承诺的部分 |
| --- | --- | --- | --- |
| Android | `ComponentDialog` + 独立 ComposeView / Android Window | 独立窗口焦点、Back / Escape、外点监听、Window Insets、dialog semantics、生命周期与保存状态 owner | 预测性返回动画仍受官方实现限制；源码注明 Dialog 的即时关闭回调使其无法使用 system-controlled predictive back animation |
| JVM Desktop | common UI Dialog 使用 Skiko ComposeSceneLayer；不要与顶层 `DialogWindow` / AWT 模态窗口混同 | 独立 focusable layer、scrim、Escape、外点阻断、窗口内居中 | 它不是新的桌面 OS 窗口；系统标题栏、AWT modality 等属于另一套 `DialogWindow` API |
| iOS | ComposeSceneLayer / UIKit 平台 layer | 独立焦点 owner、Insets / IME 约束、输入路由和 accessibility owner 接入 | 不是 UIKit `UIAlertController`；VoiceOver 的焦点进入 / 恢复必须实测 |
| JS / Wasm | Canvas / 平台 ComposeSceneLayer | layer 级键盘和指针隔离、scrim、布局 bounds | 不是 DOM `<dialog>`；浏览器 Tab、Escape 与辅助技术取决于 Canvas 桥接，不能只凭 common 源码宣称完整 |

官方 Skiko 源码用 `rememberComposeSceneLayer(focusable = true)` 创建 Dialog 层。focusable layer 接管按键和 IME，层外指针不会继续传给下层；它还使用独立 `RootNodeOwner`，并明确让 Popup / Dialog 不把焦点委托给父 FocusManager。关闭顶层 layer 时，scene 会把 focused layer 恢复为剩余的最后一个 focusable layer，否则回到 main owner。

## 焦点进入、约束和恢复

迁到 UI Dialog 后可以依赖“焦点边界 / 输入隔离”，但不能把它误解成“第一个输入框必然自动获得编辑焦点”。官方 Dialog 创建可聚焦窗口或 layer，却不替任意 content 选择初始 FocusTarget。

建议：

1. 普通 Tips / Confirm / Result Dialog：让首个主要操作可通过 Tab / 方向导航到达，不强制抢焦点；Android、Desktop、Wasm 检查 Enter / Space 激活。
2. Input Dialog：为第一个可编辑字段使用 `FocusRequester`，在 Dialog 进入组合后显式 `requestFocus()`；多字段设置 IME Next / Done 顺序。是否自动弹出软键盘应作为独立产品决定并逐平台验证。
3. 打开前记录触发控件的可恢复焦点引用；关闭后确认它重新获得焦点。Android 原生窗口关闭通常会把窗口焦点交还 Activity，Skiko 会把输入路由交回之前的 layer / main owner，但“具体回到哪个 Compose 节点”没有由 Dialog API 契约保证。
4. 多层 Dialog 打开时，焦点只进入顶层；关闭顶层后应回到下一层原先焦点，而不是穿透到底页。

验收不能只看光标：至少验证键盘事件不作用于底页、IME 不遮挡最后一个输入、关闭后触发按钮可再次用键盘激活、焦点指示器可见。Wasm Canvas 与 iOS VoiceOver 的焦点桥必须单独记录结果。

## 语义隔离

当前 Overlay 只有 Box 与 pointer input，没有 `dialog()`、`paneTitle` 或底层语义隐藏，因此读屏仍可能枚举背景内容。UI Dialog 会在根布局添加 `semantics { dialog() }`，BasicAlertDialog 再添加本地化 pane title；Android 还把内容放到独立 Dialog Window 的 ComposeView，Skiko 为 layer 创建独立 semantics owner。

这些源码事实足以证明官方实现建立了 dialog 语义边界，但不足以证明所有平台辅助技术都会自动隐藏底层树。特别是 JS / Wasm Canvas 与 iOS accessibility mediator 的实际呈现仍需端到端检查。若测试发现底层仍可遍历，应在宿主的“存在 Dialog”状态上显式隐藏底页语义，而不是在 Dialog content 上继续叠加 pointerInput。

建议语义验收：

- 弹窗打开时能识别为 dialog / pane，标题只朗读一次；
- Tab / 读屏遍历不进入底页；
- 外点区域不暴露无意义“可点击”节点；
- Progress Dialog 暴露进度或不确定进度语义；
- Input 错误关联到对应字段；
- 关闭后底页语义和焦点恢复。

## Back、Escape 与外点关闭

官方 UI Dialog 的 `onDismissRequest` 只是请求，组件不会自行从 composition 移除；继续调用 `DialogBuilder.dismiss(id)` 符合当前受控状态模型。

当前兼容规则应为：

| `clickOutsideDismiss` | 外点 | Android Back / Escape | Desktop / Wasm Escape | 说明 |
| --- | --- | --- | --- | --- |
| `false`（默认） | 不关闭 | 不关闭 | 不关闭 | 保持旧 Overlay 的同一开关行为；按钮仍可主动 dismiss |
| `true` | 关闭 | 关闭 | 关闭 | `onDismissRequest` 统一按 id 删除 |

若内容根节点只包住视觉 DialogBox，应直接使用官方 `dismissOnClickOutside`。若为兼容 `contentAlignment` 而在 Dialog 内创建 `fillMaxSize()` 对齐容器，官方会把整个容器视为 dialog bounds，无法识别视觉内容外点击；此时必须把官方外点关闭设为 false，并在全屏容器上识别外点、内容容器消费点击。这个兼容实现要额外验证：

- 内容自身空白 padding 点击不能关闭；
- 按钮点击不能同时触发外层 dismiss；
- 手势从内容内开始、在外部结束时不会误关；
- 非主鼠标键不触发关闭，与官方仅接受主按钮的规则一致；
- 快速点击不会重复执行 `hideDialog` 或挂起按钮回调。

单纯用两层 `detectTapGestures` 可以保持旧行为，但它不完全等价于官方的 outside-bounds listener，尤其是鼠标键和跨边界手势。长期更好的 API 是把位置收窄为常规居中 Dialog，让官方直接负责 outside bounds；非居中浮层应另建明确的 sheet / popover API。

## 宽度、Insets 与 contentAlignment

`BasicAlertDialog` 默认限制内容为 280--560 dp；UI Dialog 的平台默认宽度则因平台和窗口尺寸而异。Skiko 精确实现对最短边：小于 480 dp 时优选 320 dp、480--599 dp 时 440 dp、600 dp 及以上 580 dp。若保持默认 `usePlatformDefaultWidth = true`，390 dp 屏幕中的 `DialogBox` 再减去左右 22 dp 后只剩约 276 dp，明显窄于旧 Overlay 下约 346 dp 的内容宽度。

因此兼容外壳应传 `usePlatformDefaultWidth = false`，再由 `DialogBox` 保持：

- 可用宽度减左右各 22 dp；
- 最大 560 dp；
- 内容 padding 25 dp。

Skiko Dialog 默认还会用 system bars 与 IME Insets 限制可测区域，并在剩余安全区域居中；Android 默认 `decorFitsSystemWindows = true`，由 Dialog window 处理系统栏和软键盘。旧 Overlay 手工使用 `statusBarsPadding()` 与 `imePadding()`，没有横向安全区。迁移后横屏 cutout、iOS safe area 和 IME 测量会有变化，应视为预期修复并截图验证。

`DialogBuilder.contentAlignment` 是任意二维 `Alignment`，官方 Dialog 只负责窗口内居中 content。项目内未发现非默认 Dialog Builder 对齐调用，但它是公开可变属性，不能无说明删除。兼容方案有两种：

1. 第一阶段在 `usePlatformDefaultWidth = false` 的 Dialog 中放全屏 Box，并用 `contentAlignment` 对齐 Builder；代价是外点需手工识别。
2. 后续主版本把 Dialog 收窄为居中契约；Bottom / Top 场景迁到 Sheet、Popover 或专门 API，然后让官方 outside-bounds listener 完整接管。

建议第一阶段采用方案 1，Demo 同时展示 Center 与一个非居中兼容场景，防止公开字段变成未验证的假兼容。

## 多个 Dialog 同时存在

`dialogBuilderMapFlow` 可以保留多个 id，当前 `DialogLayer` 按 Map 迭代顺序把所有 Dialog 组合出来。旧 Overlay 的效果是同一 Compose 树中逐层绘制，最后一个视觉上位于最上层；所有下层仍保持组合。

迁到官方后平台模型不同：

- Android 会为每个条目创建一个 `ComponentDialog` window；组合顺序通常决定首次 show 的窗口顺序，但它们不是同一布局树中的普通兄弟节点。
- Skiko 会创建多个 focusable ComposeSceneLayer；最后附加的层获得输入焦点，关闭时恢复到剩余的最后一个 focusable layer。
- 每个 Dialog 都有独立 scrim；叠加多个半透明 scrim 会让背景越来越暗，这是可见差异。

不要只依赖 Map 与平台“碰巧最后显示”的行为。长期推荐把管理模型明确为栈：

- 新增条目 push 到顶层，Back / Escape / 外点只关闭 top；
- `updateDialog(id, builder)` 原位更新，不改变层级；
- `hideDialog(id)` 可以移除指定层；移除中间层不能改变顶层焦点；
- 关闭 top 后恢复下一层及其输入状态；
- 明确是否允许同一时刻存在多个平台 Dialog。

兼容阶段可以继续为每个 id 创建官方 Dialog，以保留下层 composition 和当前“关闭顶层后露出下层”行为，但必须完成 Android / Desktop / Wasm 的双层实测。若出现窗口顺序、焦点、读屏或多 scrim 问题，再改为“Map 保存整个栈、只渲染 top”。只渲染 top 会暂停 / 销毁下层 composition，导致 Input 的 `remember` 状态或进行中的副作用重建，因此不能作为无差异机械优化。

此外，`DialogLayer` 应使用 `key(id)` 保证同一 Map 位置的 Builder 更新不会把 remember 状态错误关联到另一个 id；Progress Builder 的 `updateDialog` 需确认更新不会重建平台 layer 或改变层级。

## Builder 兼容与后续演进

### 第一阶段必须保留

- `DialogBuilder.Build(id)` 与静态 `dismiss(id)`；
- `UiViewModelManager.showDialog` 返回 id；
- `updateDialog` 原位更新进度；
- `clickOutsideDismiss` 同时控制 Back / Escape 与外点；
- `contentAlignment`；
- DialogBox / DialogColumn 的尺寸、shape、padding 与默认颜色；
- 按钮挂起回调返回 true 后关闭、false 后保留的语义。

### 后续可单独设计

- 把 `clickOutsideDismiss` 拆为 `dismissOnBackPress` 与 `dismissOnClickOutside`；
- 新增 `DialogProperties` 或库级跨平台 properties，但需避免直接暴露平台 actual 参数；
- 用密封 DialogEntry / stack 替代可变 Builder + Map，明确 top、更新与结果；
- 为 Input Builder 加初始焦点、IME 顺序和焦点恢复句柄；
- Tips / Confirm 可选择委托 Material `AlertDialog` 内容布局，但保留品牌 Type 和挂起关闭策略；
- DialogBox 默认色从硬编码 `Color.White` 迁到主题 Surface，这会改变暗色主题，必须独立提交和截图审查。

`DialogBuilder` 当前字段是普通可变 var，不是 snapshot state。直接修改已展示 Builder 的 `clickOutsideDismiss` / `contentAlignment` 不保证触发重组；调用方应继续通过 `updateDialog(id, builder)` 更新。若后续把 properties 纳入 Builder，应同步明确不可变 Builder 或 observable state 约束。

## 差异矩阵

| 方面 | 当前 Overlay Dialog | UI `Dialog` | `BasicAlertDialog` | 推荐处理 |
| --- | --- | --- | --- | --- |
| API 稳定性 | 自有稳定 API | 稳定 common API | `ExperimentalMaterial3Api` | 外壳用 UI Dialog |
| 平台层级 | 应用内容树内 Box | Android Window；其他平台 ComposeSceneLayer | 委托 UI Dialog | 采用官方 modal 层 |
| 焦点边界 | 无独立 focus owner | 可聚焦 window / layer，阻断底层输入 | 同 UI Dialog | 官方负责边界；Input 显式请求初始焦点 |
| 焦点恢复 | 未定义 | 恢复平台 / scene 焦点，具体节点不由 API 保证 | 同 UI Dialog | 记录触发节点并逐平台测试 |
| dialog 语义 | 无 | 根节点 `dialog()` | 另加本地化 `paneTitle` | UI Dialog 足够作为首步；端到端检查底层是否隐藏 |
| Back / Escape | 自有 BackHandler，经同一开关间接控制 | properties 控制并请求 dismiss | 同 UI Dialog | 映射旧开关，后续再拆分 |
| 外点 | 全屏 pointerInput + 内容吞点击 | 官方按 content bounds 识别主按钮外点 | 同 UI Dialog | 居中内容优先官方；全屏兼容层需手工处理 |
| Scrim | 主题 scrim 50% | Android 平台 dim；Skiko 默认黑色 60% | 同 UI Dialog | 首步接受并截图记录；commonMain 无统一 scrim 参数 |
| Insets / IME | status bar + ime padding | 平台窗口 / layer 处理 system + IME | 同 UI Dialog | 移除重复 padding，跨平台验证 |
| 宽度 | 窗口减 44 dp，最大 560 dp | 默认平台窄宽或全宽约束 | 额外 280--560 dp | `usePlatformDefaultWidth=false` + 保留 DialogBox |
| 任意对齐 | 支持任意 `Alignment` | 默认居中 | 默认居中 | 首步全屏对齐容器兼容；后续收窄 API |
| 多层 | 同树兄弟 Overlay | 多 Window / 多 focusable layer | 同 UI Dialog | 明确栈并做双层验收；不要假定平台顺序 |
| Builder | 全局 id、更新、多模板 | 不提供业务管理 | 不提供业务管理 | 保留 Builder 与 Manager |
| Modifier 可替代性 | 不适合 | 不适合 | 不适合 | 只抽取纯装饰 Modifier |

## 实施与验收建议

1. 保持公开 API，`Dialog` 外壳改为 UI Dialog；映射关闭策略，设置 `usePlatformDefaultWidth = false`，保留任意 Alignment 的兼容容器。
2. 为 `DialogLayer` 的每个 id 使用稳定 composition key；不要在本步骤重写 Builder 内容和按钮样式。
3. 扩充自有 Dialog 独立页面：Tips / Confirm / Result / Input / Progress 分组但仍在同一页面内覆盖其模板特征；另加入关闭策略、长文本、窄屏、IME、多层和进度原位更新。
4. 官方页面继续独立展示 `AlertDialog`；可新增 `BasicAlertDialog` 页面展示任意自定义 content，但明确实验性状态，不把它伪装成 Builder 已支持的新特性。
5. 五目标编译。Android / Desktop / Wasm 至少实测外点、Back / Escape、Tab、输入焦点、关闭恢复和双层栈；iOS Simulator 实测 safe area、键盘与 VoiceOver 可达性。
6. 390 x 844 检查 DialogBox 仍约为屏宽减 44 dp、长标题 / 多字段不溢出、IME 出现后可滚到确认按钮、内容 padding 点击不误关。
7. 语义检查 dialog role、底层不可遍历、按钮 / 错误 / progress 语义。浏览器 Canvas 若无法完整导出 AX 树，应记录平台限制，不能用源码审查代替端到端结论。
8. 双层回归：连续打开 A/B，只允许 B 响应键盘和外点；更新 A 不改变 B 层级；关闭 B 恢复 A 的焦点与输入；移除 A 不干扰 B；scrim 深度符合产品决定。

## 官方来源

- [Compose Multiplatform Material 3 `1.11.0-alpha07` sources](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07-sources.jar)：精确版本 `AlertDialog.kt` 与 `AlertDialog.skiko.kt`；确认 BasicAlertDialog 的实验性标记、UI Dialog 委托、280--560 dp 约束与 pane title。sources JAR SHA-256：`1577fbd82c0c31f88d992cd2ef3b14fd27d36a98e743e991dda03da5071fd4ae`。
- [Compose Multiplatform Material 3 `1.11.0-alpha07` POM](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07.pom)：确认其 AndroidX Material 3 `1.5.0-alpha17` runtime 关系。
- [Compose Multiplatform UI `1.11.1` sources](https://repo1.maven.org/maven2/org/jetbrains/compose/ui/ui/1.11.1/ui-1.11.1-sources.jar)：精确版本 `Dialog.kt`、`Dialog.skiko.kt`、`RootMeasurePolicy.skiko.kt`、`ComposeSceneLayer.skiko.kt`、`CanvasLayersComposeScene.skiko.kt`；确认公共 properties、Skiko focusable layer、输入阻断、默认宽度、Insets、scrim、动画与多层焦点恢复。sources JAR SHA-256：`48dc6b69c2b1a3ee70cd19747b92a792400ed0d99a2b319ef19034b697cdfc86`。
- [AndroidX Compose UI Android `1.11.1` sources](https://dl.google.com/dl/android/maven2/androidx/compose/ui/ui-android/1.11.1/ui-android-1.11.1-sources.jar)：精确 Android actual `AndroidDialog.android.kt`；确认 ComponentDialog Window、Back / Escape、外点、window 参数、生命周期 owner、saved state 与 dialog semantics。sources JAR SHA-256：`aafb071a46db33fc841234f58061c5bc543ddaa2c8e56247409cee8e81a7b6d2`。
- [Android Developers：Compose Dialog](https://developer.android.com/develop/ui/compose/components/dialog)：`AlertDialog` 与基础 `Dialog` 的官方用途、受控显示和自定义容器说明。
- [AndroidX Material 3 `AlertDialog.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/AlertDialog.kt)：上游 API 与实现浏览入口；版本结论仍以上述本地精确 sources JAR 为准。
- [AndroidX Compose UI `Dialog.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/ui/ui/src/commonMain/kotlin/androidx/compose/ui/window/Dialog.kt)：公共 Dialog 与 DialogProperties 契约浏览入口。
- [Material Design 3 Dialog 指南](https://m3.material.io/components/dialogs/overview)：Dialog 的用途、布局和交互规范。

网络在本次研究环境中不可用，因此没有把在线页面的当前渲染结果当成版本证据；所有版本敏感结论均来自 Gradle 缓存中的官方发布 sources JAR 和 POM。
