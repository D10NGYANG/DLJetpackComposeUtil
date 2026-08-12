# Material 3 Checkbox 替代性评估

评估日期：2026-08-12  
评估对象：`library/src/commonMain/kotlin/com/d10ng/compose/ui/form/Checkbox.kt`  
项目依赖：Compose Multiplatform Material 3 `1.11.0-alpha07`（其 POM 声明 AndroidX Material 3 `1.5.0-alpha17`）

## 结论

Material 3 `Checkbox` 已覆盖仓库方形复选框的核心职责，包括二态交互、过渡动画、波纹、键盘/无障碍操作和各状态颜色；方形实现应委托官方组件。圆形不是 Material 3 Checkbox 的受支持外观，继续保留仓库自绘实现更合适。

仓库公开 `Checkbox` 不应整体改成 `Modifier`：复选框有独立视觉、状态动画和语义节点，仍是 Composable。适合下沉到 Modifier 的只是兼容层统一承载的二态交互，例如外层 `toggleable`；这样方形官方视觉可使用 `onCheckedChange = null`，圆形自绘视觉也能共用同一套点击与语义行为。

不能把现有 `size` 直接传成官方 `Modifier.size(size)` 并认为视觉会等比缩放。官方 Checkbox 的视觉 Canvas 使用 `requiredSize` 固定为 20 dp（项目当前默认行为）或 18 dp（新版 M3 token 行为），可交互版本还会保留至少 48 dp 的布局空间；仓库当前 API 则要求布局和视觉一起采用调用方给定的 `size`。兼容层需要单独处理这一差异。

## 官方 API

项目当前构件提供以下主要 API：

```kotlin
@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
)

@Composable
fun TriStateCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
)
```

同一源码还提供可定制 `checkmarkStroke` 和 `outlineStroke` 的重载。普通 `Checkbox` 只是把 Boolean 转为 `ToggleableState` 并委托 `TriStateCheckbox`；点击时请求 `!checked`，状态仍由调用方持有。

`TriStateCheckbox` 支持三种状态：

- `ToggleableState.Off`：未选中，默认透明填充并显示边框。
- `ToggleableState.On`：选中，显示实心容器和对勾。
- `ToggleableState.Indeterminate`：不确定，显示实心容器和居中横线，用于父子选择项部分选中的场景。

当前仓库 API 只有 Boolean `checked`，没有不确定态。M-007 可继续保持公开签名不变；若业务后来确实需要三态，应另增明确的三态 API，而不是给现有 Boolean API加入隐式状态。

## 尺寸与 Modifier 行为

### 官方默认尺寸

项目解析到的 `1.11.0-alpha07` 源码存在迁移特性开关 `ComposeMaterial3Flags.isCheckboxStylingFixEnabled`，默认值为 `false`：

| 行为 | 开关默认 `false` | 开关为 `true` |
| --- | --- | --- |
| 视觉方框 | 20 x 20 dp | `CheckboxTokens.ContainerSize`，18 x 18 dp |
| 内部额外 padding | 四周各 2 dp | 无 |
| 未缩放视觉的自然布局 | 24 x 24 dp | 18 x 18 dp |
| 状态层/波纹直径 | token 40 dp | token 40 dp |
| 可交互最小布局 | 默认至少 48 x 48 dp | 默认至少 48 x 48 dp |

token 同时规定 2 dp 圆角和 2 dp 未选中边框。官方源码明确说明这些迁移 flags 是临时能力，不应作为永久配置依赖。因此，在项目当前版本里把官方视觉缩放基准硬编码为 18 dp 是不正确的：默认实际绘制基准是 20 dp。即使暂时改成 20 dp，后续依赖升级或开关默认值变化仍需要截图回归。

### 最小触控区域

当 `onCheckedChange` / `onClick` 非 null 时，官方组件在调用方 `modifier` 后追加 `minimumInteractiveComponentSize()`。`LocalMinimumInteractiveComponentSize` 默认是 48 dp，因此小视觉仍至少占用 48 x 48 dp 的布局空间。系统输入层还可能按 `ViewConfiguration` 扩展触控目标；Material 的 48 dp Modifier 主要负责预留布局空间，避免相邻目标的扩展区域重叠。

当回调为 null 时，官方实现既不追加该最小尺寸，也不安装 toggleable。此时自然布局仅由视觉 Canvas 和当前样式 padding 决定。

### `Modifier.size` 不缩放视觉

官方视觉最终由 `Canvas(...requiredSize(containerSize))` 绘制。`requiredSize` 会让 Canvas 使用规定尺寸，即使父约束更小；外部 `Modifier.size(...)` 改变的是约束或组件报告的布局尺寸，不会把 20/18 dp 的 Canvas 自动缩放成目标值。因此：

- `MaterialCheckbox(..., modifier = Modifier.size(50.dp))` 不会得到 50 dp 的对勾和方框，只会改变周围布局约束/空间。
- 小于官方视觉尺寸的 `size` 可能让绘制越过报告边界，不能作为可靠的缩小手段。
- `graphicsLayer` / `scale` 可以缩放绘制，但不会同步改变测量结果；必须由固定尺寸的外层容器负责目标布局和命中区域，并基于当前真实视觉基准居中缩放。

仓库兼容层若保留 `size` 的原有含义，建议由外层 `Box(modifier.size(size))` 确定布局，由外层 toggleable 负责交互，再让只负责绘制的官方 Checkbox 居中缩放。实现时必须验证 16/22/30/50 dp，并把样式 flag 的 20/18 dp 差异视为升级风险。另一种更稳但有视觉变化的选择是停止等比缩放，让 `size` 只控制布局/命中空间；这属于公开行为变更，不适合在 M-007 静默采用。

## 颜色与状态

默认 Material 3 token/主题映射如下：

- 选中与不确定：容器和边框使用 `primary`，图标使用 `onPrimary`。
- 未选中：容器透明，边框使用 `onSurfaceVariant`。
- 禁用选中与禁用不确定：容器/边框使用 `onSurface` 的 38% alpha；禁用图标使用 `surface`（新版样式修复开启时生效）。
- 禁用未选中：容器透明，边框使用 `onSurface` 的 38% alpha。
- 启用状态间颜色会动画；切到禁用状态时 box 和 border 直接采用禁用目标色，不做启用/禁用过渡动画。

`CheckboxDefaults.colors(...)` 有简化和完整两个重载。仓库要复现旧颜色契约，应使用完整重载，分别指定 checkmark、box 和 border。简化重载会强制未选中与禁用未选中的 box 为透明，不能复现仓库当前禁用未选中的浅色实心背景。

仓库旧实现和官方默认的主要差异：

| 状态 | 仓库当前行为 | Material 3 默认 | 兼容建议 |
| --- | --- | --- | --- |
| 启用选中 | `activeColor` 实心 + 白色对勾 | `primary` + `onPrimary` | box/border 传 `activeColor`，checkmark 传白色以保持旧契约 |
| 启用未选中 | 透明 + 1 dp `inactiveColor` 边框 | 透明 + 2 dp `onSurfaceVariant` 边框 | 颜色可保持；官方边框宽度会变为 2 dp |
| 禁用选中 | `disabledColor` 实心 + `disabledColor.next(0.5)` 对勾 | `onSurface` 38% + `surface` 图标 | 用完整颜色重载保持旧颜色 |
| 禁用未选中 | `disabledColor.next(0.5)` 实心 + 1 dp `disabledColor` 边框 | 透明 + `onSurface` 38% 边框 | 用完整颜色重载设置 disabled unchecked box/border |

注意：仓库参数 KDoc 将禁用未选中描述为“半透明”，实际 `Color.next(0.5)` 是与白色做 50% RGB 混合并保持原 alpha，并不是调整透明度。迁移时应以现有实际渲染为兼容依据，同时修正文档措辞。

## 交互与语义

官方 `TriStateCheckbox` 在回调非 null 时使用 `triStateToggleable`，并设置：

- `Role.Checkbox`；
- `toggleableState`（On / Off / Indeterminate）；
- accessibility click action；
- enabled 时可聚焦，支持输入与无障碍操作；
- `enabled = false` 时添加 disabled 语义且不响应用户输入；
- toggle 类型的 autofill 数据；不确定态不暴露 Boolean autofill 值。

`onCheckedChange = null` 和 `enabled = false` 不是同一种状态：

| 调用方式 | 输入 | 视觉 | 语义与布局 |
| --- | --- | --- | --- |
| 回调非 null，`enabled = true` | 官方组件自行处理 | 启用色 | Checkbox role/state/click，默认至少 48 dp |
| 回调非 null，`enabled = false` | 不响应 | 禁用色 | 仍有 Checkbox role/state，并标记 disabled，默认至少 48 dp |
| 回调 null，`enabled = true` | 官方组件不处理 | 启用色 | 不安装 toggleable，没有官方 Checkbox role/state/click，也不加 48 dp 最小尺寸 |
| 回调 null，`enabled = false` | 官方组件不处理 | 禁用色 | 仍不安装 toggleable；`enabled` 此时只参与颜色计算，官方自身不会生成 disabled/Checkbox 语义 |

因此，方形兼容层若采用 `MaterialCheckbox(onCheckedChange = null)` 以避免双重点击，外层必须继续安装 `Modifier.toggleable(value = checked, enabled = !disabled, role = Role.Checkbox, ...)`。这会恢复二态、点击、键盘和 disabled 语义，也让方形与圆形保持一致。外层语义 Modifier 应包围视觉节点，避免再给内部官方视觉设置独立交互语义。

## 与仓库公开 API 的差异

| 方面 | 仓库 `Checkbox` | Material 3 | 迁移影响 |
| --- | --- | --- | --- |
| 状态 | Boolean 二态 | `Checkbox` 二态；`TriStateCheckbox` 三态 | 方形二态可直接覆盖；三态是官方额外能力 |
| 形状 | 方形和圆形 | 固定圆角方形 | 方形委托官方，圆形保留自绘 |
| 默认视觉尺寸 | 22 dp | 当前默认 20 dp；新 token 18 dp | 需要兼容缩放或接受视觉变化 |
| 默认可交互布局 | 22 dp | 至少 48 dp | 直接替换会改变列表密度；外层兼容交互可维持旧布局，但旧 22 dp 低于可访问性建议 |
| 自定义尺寸 | 布局、方框、图标一起缩放 | `Modifier.size` 不缩放固定 Canvas | 不能直接参数映射 |
| 边框 | 1 dp | 2 dp | 委托官方后会有可见视觉变化 |
| 颜色参数 | active / inactive / disabled 三色业务接口 | 13 个分状态颜色槽 | 可通过完整 `CheckboxDefaults.colors` 映射 |
| 禁用控制 | `disabled` | `enabled` | 映射为 `enabled = !disabled` |
| 回调 | 非空函数，默认空操作 | nullable；null 表示自身不交互 | 兼容层应保留现有签名，外层统一处理交互 |
| 波纹/动画 | 无波纹，颜色和对勾立即切换 | 官方波纹、颜色和路径动画 | 方形迁移获得官方特性；圆形仍保持旧动画行为 |
| 语义 | 外层 toggleable，二态 Checkbox | 回调非 null 时提供二/三态 Checkbox 语义 | 外层统一 toggleable 可保持兼容 |

仓库的默认空回调并不等价于官方的 null 回调：仓库默认仍安装 toggleable，所以节点可聚焦、可点击并具有 Checkbox 语义，只是点击后调用空函数；官方 null 则完全不安装交互与 Checkbox 语义。保持公开兼容时不要把默认空回调转成 null 后又移除外层 toggleable。

## 迁移建议

1. 保持 `Checkbox(...)`、`CheckboxType` 及全部参数不变，由一个固定 `size` 的外层容器统一安装 `toggleable`，使方形和圆形继续共享二态、disabled 和 Checkbox 语义。
2. `CheckboxType.SQUARE` 内部使用官方 `androidx.compose.material3.Checkbox`，传 `onCheckedChange = null`，避免内部再增加 48 dp 布局、第二个点击处理器和重复语义。官方 `enabled = !disabled` 仍需传入，用于选择禁用视觉颜色。
3. 方形用完整 `CheckboxDefaults.colors(...)` 映射旧三色规则。接受官方 2 dp 边框、对勾路径动画和波纹是否可见的变化，并在 390 px Demo 中做 checked/unchecked/disabled 截图回归。
4. `CheckboxType.CIRCLE` 保留为私有自绘视觉，但移除其内部 toggleable；交互只由外层负责。这样不会维护两套输入代码。
5. 继续保留 `size` 的视觉缩放语义。当前构件默认视觉基准是 20 dp，不是 token 的 18 dp；不要无条件使用 `size / 18.dp`。若实现依赖临时样式 flag，应将风险显式记录并为升级增加回归检查。
6. Demo 至少展示方形/圆形的选中与未选中、两种禁用态、16/22/30/50 dp、三组自定义颜色、单元格组合，以及点击后状态更新。官方三态能力放在 Material 3 官方 Checkbox 页面展示，不必扩进仓库兼容 API。
7. 当前 22 dp 命中区域低于 Material 建议的 48 dp。M-007 为避免布局破坏可先保持旧行为，但应在后续无障碍任务中考虑为真实业务行在父级提供 48 dp 可点击区域，或提供新的可访问性优先 API；不要通过本次内部委托悄悄把所有布局扩大到 48 dp。

## 官方来源

- [Compose Multiplatform Material 3 `1.11.0-alpha07` 源码构件](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07-sources.jar)：本次实施的精确版本来源；`Checkbox.kt` SHA-256 为 `d20673fdae6fbaa6c3e21dd4cb9c87e8e26f9d9fa8a69d3a28a2482f0ebe2d82`。
- [Compose Multiplatform Material 3 `1.11.0-alpha07` POM](https://repo1.maven.org/maven2/org/jetbrains/compose/material3/material3/1.11.0-alpha07/material3-1.11.0-alpha07.pom)：确认该 Multiplatform 构件及其 AndroidX Material 3 `1.5.0-alpha17` 依赖关系。
- [AndroidX Material 3 `Checkbox.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/Checkbox.kt)：公开 API、委托链、颜色解析、语义安装条件、固定 Canvas 尺寸与动画实现。
- [AndroidX Material 3 `CheckboxTokens.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/CheckboxTokens.kt)：18 dp 容器、2 dp 圆角、40 dp 状态层及各颜色角色。
- [AndroidX Material 3 `InteractiveComponentSize.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/InteractiveComponentSize.kt)：48 dp 默认最小交互布局、CompositionLocal 配置及 Modifier 顺序说明。
- [AndroidX Foundation `Toggleable.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/foundation/foundation/src/commonMain/kotlin/androidx/compose/foundation/selection/Toggleable.kt)：二态/三态 toggleable 的 Checkbox role、toggle state、disabled、click 与 autofill 语义。
- [Android Developers Material 3 Checkbox API](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#Checkbox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.material3.CheckboxColors,androidx.compose.foundation.interaction.MutableInteractionSource))：官方 API 索引和参数契约。
- [Material Design 3 Checkbox 指南](https://m3.material.io/components/checkbox/overview)：官方组件用途、状态和设计规范入口。

以上版本相关尺寸和开关结论以项目 Gradle 缓存中的官方 `1.11.0-alpha07` sources JAR 为准；`androidx-main` 链接用于提供可浏览的上游实现，后者可能随时间继续演进。
