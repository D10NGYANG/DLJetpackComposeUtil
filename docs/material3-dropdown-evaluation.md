# Material 3 Exposed Dropdown Menu 替代性评估

评估日期：2026-08-12  
评估对象：`ListMenuItem`、`MenuInput`、`ListInputItem`  
项目依赖：Compose Multiplatform Material 3 `1.11.0-alpha07`（其 POM 声明 AndroidX Material 3 `1.5.0-alpha17`）

## 结论

Material 3 `ExposedDropdownMenuBox` 适合接管两类组件共同的下拉菜单基础设施：锚点点击与语义、菜单宽度约束、窗口内上下定位、可用高度计算、弹层关闭、返回键处理，以及可编辑输入时避免抢走软键盘焦点。仓库不应继续用普通 `DropdownMenu` 手工拼接这些行为。

它不是 `ListMenuItem` 或 `MenuInput` 的完整替代品。官方 API 是受控的容器和作用域，不负责业务值映射、选项过滤、20 项上限、空结果提示、前后缀、密码显隐、错误文案或列表行布局。这些仍应由仓库兼容层组合实现。

- `ListMenuItem`：适合在保持现有公开参数和内部展开状态的前提下，委托 `ExposedDropdownMenuBox`、`menuAnchor(PrimaryNotEditable)` 和作用域内的 `ExposedDropdownMenu`。这是低风险迁移。
- `MenuInput` / `ListInputItem`：也适合委托官方容器，但不能只机械替换 `DropdownMenu`。应把“输入是否聚焦”和“菜单是否允许展开”拆成两个状态，并保留现有过滤、空结果和表单能力。这是中等风险迁移，建议在 `ListMenuItem` 稳定后单独实施。
- 三者都不适合改成 `Modifier`。它们拥有布局、视觉内容、业务数据映射和弹层内容；Modifier 无法替代这些组合职责。适合使用 Modifier 的部分已经由官方提供，即作用域限定的 `Modifier.menuAnchor(...)` 和菜单尺寸约束。

## 当前实现与调用面

CodeGraph 定位到的生产定义如下：

| API | 位置 | 职责 | 生产调用关系 |
| --- | --- | --- | --- |
| `ListMenuItem` | `library/.../view/ListItem.kt` | 列表行内只读单选菜单；按 `value == item.data` 映射显示值；内部持有展开状态 | 由 Demo 页面直接调用，未发现其他生产调用方 |
| `MenuInput` | `library/.../view/Input.kt` | 可编辑输入、过滤建议、空结果、前后缀、密码、错误和 IME Next | 被 `ListInputItem` 组合；也由 Demo 页面直接调用 |
| `ListInputItem` | `library/.../view/ListItem.kt` | 将 `MenuInput` 放入列表行 | 由 Demo 页面直接调用 |

当前 Demo 已分别有 `LibraryListMenuItemScreen`、`MenuInputScreen` 和 `LibraryListInputItemScreen`，因此后续可以按组件独立页面做迁移回归，不需要重新把所有场景堆到一个页面。

### `ListMenuItem` 的既有契约

- 选择值由调用方持有，组件仅用 `menus.find { it.data == value }` 推导显示文本。
- 展开状态由组件内部持有；点击文本或箭头切换，选择后收起。
- `inputWidth == null` 时占满列表行剩余空间，否则使用固定宽度。
- 可定制标题、说明、图标、必填星号、文本样式、背景颜色和形状。
- 空菜单目前没有公开的空态文案参数；旧实现会展开一个没有选项的空弹层。
- 没有 `enabled` / `disabled` 参数。

### `MenuInput` 的既有契约

- `value` 和 `onValueChange` 由调用方控制；`isFocus` 同时被用作“输入聚焦”和“菜单展开”。
- 对 `item.show` 做区分大小写的 `contains(value)` 过滤，最多显示前 20 项。
- 原始 `menus` 非空而过滤结果为空时，显示 `noMatchMenuTips`。
- `PopupProperties` 显式关闭外部点击和返回键消失，依赖调用方通过焦点回调结束展示。
- 同时承担前后缀、密码显隐、错误文案、键盘类型、IME Next、多行和强制聚焦等表单能力。

这些都是公开行为。特别是 `isFocus` 虽然状态建模不理想，但迁移时不能把它静默解释成纯展开状态，或令现有调用方失去焦点回调。

## 官方 API 能力

项目精确版本提供的核心结构是：

```kotlin
@ExperimentalMaterial3Api
@Composable
fun ExposedDropdownMenuBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ExposedDropdownMenuBoxScope.() -> Unit,
)
```

作用域提供：

```kotlin
fun Modifier.menuAnchor(
    type: ExposedDropdownMenuAnchorType,
    enabled: Boolean = true,
): Modifier

fun Modifier.exposedDropdownSize(matchAnchorWidth: Boolean = true): Modifier

@Composable
fun ExposedDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    matchAnchorWidth: Boolean = true,
    // shape / color / elevation / border / content
)
```

三种锚点类型具有不同焦点策略：

| 类型 | 用途 | 打开菜单时的策略 |
| --- | --- | --- |
| `PrimaryNotEditable` | 只读 TextField 或选择器主体 | 菜单可获取焦点，适合 `ListMenuItem` |
| `PrimaryEditable` | 接受输入的主要 TextField | 菜单默认不抢焦点，以保留输入法，适合 `MenuInput` |
| `SecondaryEditable` | 可编辑输入旁的下拉箭头 | 无障碍服务启用时允许菜单获焦，否则保留输入法焦点 |

旧的无参数 `menuAnchor()` 已被标记弃用；迁移必须明确传锚点类型。`focusable` 版 `ExposedDropdownMenu` 也已弃用，焦点性由锚点类型决定。

## 锚点、测量与弹层定位

官方容器通过 `onGloballyPositioned` 保存容器坐标和宽度，并以可见窗口、状态栏 inset、48 dp 垂直边距及锚点上下空间计算弹层位置和最大高度。`ExposedDropdownMenu` 自动应用 `exposedDropdownSize`：默认强制菜单宽度匹配锚点，并限制高度不越过可用窗口或软键盘区域。

这比当前固定 `heightIn(max = 200.dp)` 的普通 `DropdownMenu` 更完整，但迁移时要注意结构：

- `ExposedDropdownMenuBox` 应只包住实际输入/选择器锚点和对应菜单。
- 不要把错误文案、整条 `ListItem` 或额外说明一起放入用于测量的 Box，否则匹配宽度和上下可用空间会以过大的组合区域计算。
- 可以继续用 `heightIn(max = 200.dp)` 保持旧菜单密度；官方约束会进一步把高度压到当前窗口真正可用的范围。
- `matchAnchorWidth = true` 会把菜单宽度改为锚点宽度。普通 `DropdownMenu` 的旧宽度由菜单内容与约束决定，因此长/短选项的弹层宽度可能发生可见变化。这是应在 390 px Demo 中确认的预期迁移差异。

Android 实现监听软键盘引起的窗口布局变化并重算位置。Desktop、iOS、JS 和 Wasm 使用共同的 Skiko 实现，只监听 `WindowInfo.containerSize`；官方源码仍以“不会覆盖文本框或软键盘”为 API 目标，但非 Android 平台是否能感知 IME 取决于该平台窗口信息是否随输入法更新，必须分别实测，不能仅凭 commonMain 实现宣称完全一致。

## 展开、焦点与过滤状态

官方 API 刻意不保存展开状态，也不实现过滤。官方可编辑示例使用两个概念：调用方保存 `allowExpanded`，实际 `expanded` 再结合过滤结果计算。选项过滤同样在组件外完成。

这与当前两个仓库 API 的映射方式不同：

### `ListMenuItem`

可以继续内部 `remember` 展开状态，并将 `onExpandedChange` 直接回写。选中项后调用现有 `onMenuSelect(item)` 并收起，不改变 `value` 受控契约。`menus` 或 `value` 更新时，显示项仍按现有相等判断重新推导。

### `MenuInput`

不能继续把 `expanded = isFocus` 原样交给官方容器。推荐在兼容层内部维护“允许展开”状态：

- `isFocus` 继续只代表调用方请求/报告的输入焦点，保留 `onFocus`、`onNoFocus` 行为。
- 锚点交互和输入变化决定是否允许展开；失焦、选择、外部点击和返回键关闭菜单。
- 过滤仍保留 `contains` 与最多 20 项的旧规则，除非另立任务明确改变搜索算法。
- 是否在过滤为空时保持弹层并展示 `noMatchMenuTips` 是仓库产品决策，不由官方组件决定。若要保持现有反馈，应让菜单继续展开并放置不可点击的空态内容。

官方最新示例在过滤结果为空时直接令 `expanded = false`，不会展示“无匹配选项”。照抄示例会丢失仓库现有特性。

## 空列表与空结果

`ExposedDropdownMenuBox` 不提供 loading、empty 或 no-match 槽位；作用域内容完全由调用方决定。

建议区分两种语义：

- `ListMenuItem(menus = emptyList())`：现有公开 API 没有空态参数。兼容优先可禁用锚点或保持空弹层；若产品希望展示“暂无可选项”，应新增可配置且可本地化的参数，不能在底层写死中文后视为无行为变化。
- `MenuInput` 过滤为空：已有 `noMatchMenuTips`，应继续展示。空态行应使用 `enabled = false` 的 `DropdownMenuItem` 或无点击语义的展示内容；旧 `onClick = {}` 仍会暴露一个可点击但无效果的动作，不应照搬。

原始 `menus` 为空时，当前 `MenuInput` 根本不创建菜单；官方迁移也应先明确这是“没有建议功能”还是“应显示空结果”，不要混同过滤后的无匹配状态。

## 键盘、返回键与语义

`menuAnchor` 不只是点击 Modifier。精确版本源码还实现了：

- 非可编辑主锚点响应 Enter、数字键盘 Enter、方向键中心和 Space 展开/收起。
- 可编辑主锚点不使用 Space 切换，避免影响正常输入；Enter 可切换菜单。
- 可编辑菜单展开时，Tab、方向上和方向下会把弹层切换为可聚焦，以便键盘进入选项。
- 可编辑主锚点默认让弹层不可聚焦，避免打断软键盘；辅助锚点会结合无障碍服务状态选择焦点策略。
- `ExposedDropdownMenuBox` 在展开时安装 `BackHandler`；可聚焦 Popup 由 Popup 层处理返回，非聚焦 Popup 由 Box 收起。
- 主锚点暴露 `Role.DropdownList`；可编辑辅助锚点暴露 `Role.Button`、展开/收起状态描述和切换动作；菜单项使用官方点击/禁用语义。

因此 `ListMenuItem` 不应再在内部 Text 和 Icon 分别安装 `clickable`，否则会形成重复点击处理和碎片化语义；让整个可见选择器 Row 使用一个 `PrimaryNotEditable` 锚点更合适。

此外，`ListMenuItem` 复用的 `ListItem` 基础实现当前无条件安装 `clickable`，即使传入的只是默认空回调。迁移内部选择器后，父级仍可能留下一个无效果的 click 语义，和子级 `Role.DropdownList` 形成嵌套交互节点。官方容器不会自动修复这个既有问题；实现阶段应检查语义树，并让无实际行点击动作的 `ListItem` 不安装 clickable，或显式重构这一组合层，避免空父动作吞掉键盘/读屏操作。

`MenuInput` 应把 `PrimaryEditable` 锚点附着到实际输入区域，并在存在独立下拉箭头时给箭头使用 `SecondaryEditable`。如果把锚点放在同时包含错误文案的外层 Column，焦点、点击范围和测量都会偏离实际输入框。

与旧行为相比，官方外部点击和 Back 收起菜单是有意的行为改进。当前 `MenuInput` 明确设置 `dismissOnBackPress = false`、`dismissOnClickOutside = false`，所以这是兼容差异，Demo 应验证并在发布说明中记录。Esc、方向键进入菜单、Tab、Enter、Back 及读屏角色仍需在各目标实际验证；源码能力不等于浏览器 Canvas 或每个平台辅助技术已正确桥接。

## 公开 API 与特性差异

| 方面 | 仓库 API | 官方能力 | 迁移处理 |
| --- | --- | --- | --- |
| 业务值 | `Any?` 与 `ListItemMenu.data` 映射 | 不认识业务值 | 保留仓库映射层 |
| 展开状态 | `ListMenuItem` 内部持有；`MenuInput` 复用 `isFocus` | `expanded` 完全受控 | 前者直接映射；后者拆分焦点与展开 |
| 锚点 | 文本和图标各自 clickable | 统一 `menuAnchor(type)` | 委托官方，移除重复 clickable |
| 过滤 | `contains`，最多 20 项 | 不提供；示例自行实现 | 保持仓库规则 |
| 空反馈 | `MenuInput` 有 `noMatchMenuTips` | 不提供 | 由兼容层继续渲染且禁用点击 |
| 宽度 | 输入可固定或占剩余；旧菜单按内容测量 | 默认匹配锚点宽度 | 保持输入宽度，接受/验证菜单宽度变化 |
| 高度 | 固定最大 200 dp | 按窗口/锚点动态限制 | 叠加 200 dp 上限以保持密度 |
| 关闭 | 普通菜单；`MenuInput` 禁止外点/Back 关闭 | 外点、Popup 或 BackHandler 关闭 | 作为明确的行为改进验证 |
| 输入法 | 手工焦点状态 | editable anchor 避免 Popup 抢 IME | 使用正确锚点类型，跨平台实测 |
| 键盘 | 无专门锚点键盘逻辑 | Enter/Space/Tab/方向键策略 | 采用官方行为并做目标端回归 |
| 语义 | 分散 clickable，缺少 dropdown role | DropdownList / Button 与状态描述 | 委托官方语义 |
| 样式 | 自定义 BasicTextField 外壳 | 推荐 Material `TextField`，但不是强制 | 首步保留旧外观，只替换基础设施 |
| 表单能力 | 前后缀、密码、错误、Next、多行 | Box 不提供 | 继续由 `MenuInput` 组合 |

## 是否改成 Modifier

不建议将 `ListMenuItem`、`MenuInput` 或 `ListInputItem` 改成 Modifier 扩展：

1. Modifier 适合给已有节点附加行为或布局，不负责生成 TextField、箭头、Popup、菜单项和空态内容。
2. 这些 API 需要组合多个语义节点，并根据业务数据生成动态内容。
3. `ExposedDropdownMenuBoxScope.menuAnchor` 是作用域限定 API，必须位于 `ExposedDropdownMenuBox` 的组合结构内；单独包装成仓库全局 Modifier 只会隐藏必要的结构约束。

可下沉为非 UI 辅助逻辑的只有纯数据操作，例如选中项查找、过滤和数量限制；它们更适合普通函数，而不是 Modifier。官方 `menuAnchor` 本身已经是应复用的 Modifier 扩展，不需要再造同义封装。

## 建议迁移顺序

1. 先迁移 `ListMenuItem`：保持签名、值映射和 200 dp 上限，使用 `PrimaryNotEditable` 单一锚点；空菜单行为单独做产品决定。
2. 在独立 Demo 对比未匹配值、空列表、长列表滚动、长文本、固定/自适应宽度、外部重置、外点关闭、Back、Enter/Space 与 disabled 空态。
3. 再迁移 `MenuInput` / `ListInputItem`：拆分焦点和展开状态，使用 `PrimaryEditable`；保留过滤、20 项上限、空结果、表单参数与选择回调。
4. 对可编辑版本补充输入中菜单保持、选择后焦点/IME、无匹配反馈、外点/Back、Tab/方向键、密码与无菜单输入的回归。
5. Android、JVM、JS、Wasm 和 iOS Simulator 均做编译；Android/桌面/浏览器至少实测弹层定位与键盘，iOS 实测软键盘遮挡。Wasm/JS 的读屏角色和键盘桥接不能只靠 commonMain 源码推断。

首步不建议同时把 `BasicTextField` 改成官方 `TextField`。那会叠加尺寸、排版、颜色、标签和错误样式变化，扩大审查范围。先委托下拉基础设施，再把视觉 Material 化作为可独立截图评审的后续任务。

## 官方来源

- [Compose Multiplatform Material 3 `1.11.0-alpha07` 源码构件](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07-sources.jar)：本项目精确版本的 `ExposedDropdownMenu.kt`、`Menu.kt` 及 commonMain/Skiko actual；sources JAR SHA-256 为 `1577fbd82c0c31f88d992cd2ef3b14fd27d36a98e743e991dda03da5071fd4ae`。
- [Compose Multiplatform Material 3 `1.11.0-alpha07` POM](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07.pom)：确认 Multiplatform 版本及其 AndroidX Material 3 `1.5.0-alpha17` 运行时关系。
- [AndroidX Material 3 `ExposedDropdownMenu.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/ExposedDropdownMenu.kt)：API、锚点类型、测量、定位、键盘、返回键、Popup 焦点与语义实现。
- [AndroidX Material 3 菜单官方示例源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/ExposedDropdownMenuSamples.kt)：只读选择器、可编辑过滤和多段自动完成的官方状态组织方式。
- [Android Developers：Compose Menu](https://developer.android.com/develop/ui/compose/components/menu)：官方组件用法与 exposed dropdown 示例入口。
- [Material Design 3 Menus 指南](https://m3.material.io/components/menus/overview)：组件用途和交互规范入口。

以上版本相关实现结论以项目 Gradle 缓存中的官方 `1.11.0-alpha07` sources JAR 为准；`androidx-main` 链接便于浏览上游源码，但可能随时间继续演进。
