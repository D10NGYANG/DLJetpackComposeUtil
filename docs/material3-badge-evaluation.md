# Material 3 Badge 替代性评估

评估日期：2026-08-12  
评估对象：`library/src/commonMain/kotlin/com/d10ng/compose/ui/show/Badge.kt`

## 结论

Material 3 的 `BadgedBox` 与 `Badge` 已覆盖仓库组件的核心职责，建议迁移到官方组件。仓库应只保留薄兼容层处理现有的业务规则（`num <= 0` 隐藏、`max+` 格式化）和过渡期 API，避免继续维护自定义布局。

`Badge` 不适合改成 `Modifier`：它是有自身尺寸、背景和可选内容的视觉节点；`BadgedBox` 还需要同时测量锚点与徽标并将二者叠放，仍应是 Composable。

## 官方 API 与行为

```kotlin
@Composable
fun BadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
)

@Composable
fun Badge(
    modifier: Modifier = Modifier,
    containerColor: Color = BadgeDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    content: (@Composable RowScope.() -> Unit)? = null,
)
```

- `BadgedBox` 把 `content` 作为锚点，输出尺寸只取锚点尺寸；徽标可以越过该区域绘制，不会给主体增加内边距。布局保留锚点的首末基线，并用相对坐标放置，因此适配 RTL。
- 无内容徽标以锚点末端 6 dp、顶部 6 dp 的规则定位；有内容徽标使用水平 12 dp、垂直 14 dp 的偏移规则。实现还可通过内部 ruler 限制徽标不超出父级指定边界。
- `Badge(content = null)` 是 6 x 6 dp 的圆点。只要 `content` 非 null（即使插槽为空），就采用最小 16 x 16 dp、全圆角、左右各 4 dp 内边距的文本徽标布局。
- 默认容器色取 `MaterialTheme.colorScheme.error`；默认内容色经 `contentColorFor(containerColor)` 推导，默认容器对应 `onError`。有内容时默认文字样式是 Material 3 `labelSmall`。
- 文本内容是任意 `RowScope` Composable。官方组件没有最大宽度、裁剪策略、行数限制或数字专用 API；内容较长时徽标会横向扩展。
- 官方 API 没有内置 `num`、隐藏零值、最大值或 `99+` 格式化。官方示例直接由调用方传入 `"8"` 和 `"999+"`，证实格式化属于业务层。
- `Badge` 和 `BadgedBox` 的实现均未添加 `SemanticsModifier`。官方示例为圆点 `Badge` 显式设置 `contentDescription = "New notification"`，为数字 `Text` 显式设置通知数量描述；语义需要调用方根据业务补充。

## 与仓库实现的差异

| 方面 | 仓库 `Badge` / `BadgeBox` | Material 3 | 迁移影响 |
| --- | --- | --- | --- |
| 容器 API | 无 `Modifier` 参数；插槽不是 `BoxScope` | 两个组件均支持 `Modifier`，插槽有 scope | 官方 API 扩展性更好 |
| 定位 | 主体四周固定增加 4 dp，再把徽标对齐外层右上角 | 测量锚点和徽标并按 Material token 偏移，支持 RTL 与基线 | 迁移后徽标位置和主体占位会变化，应做截图回归 |
| 空点尺寸 | 空字符串为最小 12 dp | `content = null` 为 6 dp | 明显视觉差异；直接强制为 12 dp 会被官方布局误判成有内容徽标，不能这样保留旧尺寸 |
| 文本尺寸 | 最小 18 dp，垂直 1 dp、水平 4 dp 内边距 | 最小 16 dp，仅水平 4 dp 内边距 | 迁移后高度和文字样式略有变化 |
| 形状 | 空点圆形；文本 10 dp 圆角 | 两种均为全圆角 | 长文本胶囊轮廓会不同 |
| 颜色 | 背景默认 `AppColor.Func.error`；文本色由 `AppText` 隐式决定 | 默认 `colorScheme.error` / `onError`，可分别覆盖 | 当前主题已将 error 映射到 `AppColor.Func.error`，默认背景可保持一致；内容色应显式验证 |
| 文本溢出 | 最大宽 32 dp，单行裁剪 | 不限制宽度，也不自动裁剪 | 长文本视觉和布局会变化，应选择保留旧裁剪或接受官方扩展行为 |
| 数字规则 | `num <= 0` 不显示；默认上限 99；超限显示 `99+` | 无数字规则 | 兼容层或调用方必须保留格式化 |
| 空字符串 | `Badge("")` 是 12 dp 圆点 | `Badge { Text("") }` 仍是最小 16 dp 文本徽标；圆点应调用 `Badge()` | 调用点需要区分“无插槽”和“空文本” |
| 语义 | 未内置 | 未内置，官方示例要求调用方添加 | 迁移不会自动改善无障碍；Demo 和业务调用点需补描述 |

## 迁移建议

1. 将 `BadgeBox` 的内部实现替换为 Material 3 `BadgedBox`，但在当前兼容迁移中保持公开签名和普通插槽类型不变。接受官方定位作为默认行为；若未来破坏性版本需要 `Modifier` 或 `BoxScope` 插槽，再通过新 API 或主版本升级提供。
2. 字符串重载改为 Material 3 `Badge(containerColor = color) { Text(content) }`；空字符串分支调用无内容的官方 `Badge()`。若必须保持当前视觉规格，可通过 `Modifier.defaultMinSize(...)` 等局部覆盖，但不再复制整个布局算法。
3. 数字重载保留为薄业务适配器：先计算可空标签，再调用官方 `Badge`。同时约束或校验 `max`；当前实现允许负 `max`，例如 `num = 1, max = -1` 会生成 `-1+`。
4. 迁移时明确决定是否保留旧版 32 dp 裁剪。通知数量建议继续格式化为 `99+`，一般无需裁剪；任意文本若确实需要裁剪，应由传入的 `Text(maxLines = 1, overflow = ...)` 自己负责。
5. 为圆点和数字分别添加业务化语义。不要只读出 `"99+"`，应提供类似“99 条以上未读消息”的本地化描述。
6. 当前保留现有公开函数作为薄兼容层，不直接删除或弃用；若下一个破坏性版本决定让调用方直接使用官方 API，再提供迁移周期。

本次实施保留了旧 `BadgeBox` 对锚点内容四周 4 dp 的留白，以维持现有调用点的外部占位；徽标测量、相对定位、基线和视觉 token 均委托官方组件。空点不再强制旧 12 dp，避免被官方 `BadgedBox` 误判为有内容徽标。

## 官方来源

- [AndroidX Material 3 `Badge.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/Badge.kt)：公开签名、测量与定位算法、空/有内容分支、默认颜色推导。
- [AndroidX Material 3 `BadgeTokens.kt` 官方源码](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/BadgeTokens.kt)：6 dp / 16 dp 尺寸、全圆角、`error` / `onError` 颜色角色及 `labelSmall` 字体 token。
- [AndroidX 官方 `BadgeSamples.kt`](https://github.com/androidx/androidx/blob/androidx-main/compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/BadgeSamples.kt)：圆点、`8`、`999+` 与显式 `contentDescription` 的推荐示例。
- [Android Developers Material 3 Kotlin API 索引](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#Badge(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function1))：AndroidX 公开 API 入口。

以上结论以 AndroidX `androidx-main` 在评估日的官方源码为准。项目当前使用的 Compose Multiplatform Material 3 `1.11.0-alpha07` 已在 Demo 中成功引用 `androidx.compose.material3.Badge` / `BadgedBox`；实施时仍需用项目五目标编译确认各 target 的实际可用性。
