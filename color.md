# **Material 3 颜色分类及应用**

![Material 3 颜色分类](https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fly2ms4t2-1.png?alt=media&token=722d8f55-45a4-4340-98ad-9ae1aa71b7ae)

| 颜色类别                       | 变量名                       | 作用                               | 适用场景                    |
|----------------------------|---------------------------|----------------------------------|-------------------------|
| **主色（Primary Colors）**     | `primary`                 | 主要品牌色                            | 主要按钮、AppBar、选中状态        |
|                            | `primaryContainer`        | `primary` 变体，适用于背景               | 卡片背景、大面积背景              |
|                            | `onPrimary`               | `primary` 背景上的文本/图标颜色            | 按钮文字、图标                 |
|                            | `onPrimaryContainer`      | `primaryContainer` 背景上的文本/图标颜色   | 卡片内文字、图标                |
| **次要色（Secondary Colors）**  | `secondary`               | 辅助色，次要按钮、标签等                     | `OutlinedButton`、`Chip` |
|                            | `secondaryContainer`      | `secondary` 变体                   | 次要 UI 背景                |
|                            | `onSecondary`             | `secondary` 背景上的文本/图标颜色          | 次要按钮文字                  |
|                            | `onSecondaryContainer`    | `secondaryContainer` 背景上的文本/图标颜色 | 卡片内次要文本                 |
| **三级色（Tertiary Colors）**   | `tertiary`                | 额外强调色                            | 特殊强调文本或组件               |
|                            | `tertiaryContainer`       | `tertiary` 变体                    | 特殊 UI 组件的背景             |
|                            | `onTertiary`              | `tertiary` 背景上的文本/图标颜色           | 强调文字                    |
|                            | `onTertiaryContainer`     | `tertiaryContainer` 背景上的文本/图标颜色  | 特殊 UI 内文本               |
| **错误状态（Error Colors）**     | `error`                   | 错误状态颜色                           | 表单验证错误、失败按钮             |
|                            | `errorContainer`          | `error` 变体                       | 错误消息背景                  |
|                            | `onError`                 | `error` 背景上的文本/图标颜色              | 错误文本、图标                 |
|                            | `onErrorContainer`        | `errorContainer` 背景上的文本/图标颜色     | 错误提示框内文字                |
| **背景（Background Colors）**  | `background`              | 整个应用的默认背景色                       | 页面背景                    |
|                            | `onBackground`            | `background` 背景上的文本/图标颜色         | 页面主文本                   |
| **表面（Surface Colors）**     | `surface`                 | 组件表面颜色                           | `Card`、`Dialog`         |
|                            | `surfaceVariant`          | `surface` 变体                     | 多层级表面背景                 |
|                            | `onSurface`               | `surface` 背景上的文本/图标颜色            | `Card` 内文字              |
|                            | `onSurfaceVariant`        | `surfaceVariant` 背景上的文本/图标颜色     | 多层背景文本                  |
|                            | `surfaceBright`           | 比 `surface` 亮的表面色                | 提供更明亮的层次区分              |
|                            | `surfaceDim`              | 比 `surface` 暗的表面色                | 深色模式下使用                 |
|                            | `surfaceContainer`        | 标准表面容器颜色                         | 普通层级 UI                 |
|                            | `surfaceContainerLow`     | 较低层级的表面颜色                        | 底部 UI 容器                |
|                            | `surfaceContainerLowest`  | 最低层级表面颜色                         | 应用最底层背景                 |
|                            | `surfaceContainerHigh`    | 高层级表面颜色                          | 重要层级 UI                 |
|                            | `surfaceContainerHighest` | 最高层级表面颜色                         | 最高层级 UI                 |
|                            | `surfaceTint`             | `surface` 颜色的叠加色                 | 轻微色调调整                  |
| **轮廓与分割线（Outline Colors）** | `outline`                 | 轮廓颜色                             | `TextField`、`Divider`   |
|                            | `outlineVariant`          | 变体轮廓颜色                           | 适用于不同边框风格               |
| **反色（Inverse Colors）**     | `inverseSurface`          | 反向 `surface` 颜色                  | `Snackbar` 背景           |
|                            | `inverseOnSurface`        | `inverseSurface` 上的文本/图标颜色       | `Snackbar` 文本           |
|                            | `inversePrimary`          | 反向 `primary` 颜色                  | 深色模式下的对比色               |
| **杂项（Others）**             | `scrim`                   | 半透明遮罩层颜色                         | `Modal`、`Drawer` 背景     |

### **总结**
- **`primary`、`secondary`、`tertiary`** 负责 **主要颜色**，用于不同层级的 UI 元素。
- **`background`、`surface`** 负责 **整体布局和卡片** 的背景色。
- **`error`** 处理 **错误状态** 的视觉表现。
- **`outline`** 负责 **边框、分割线** 等轮廓部分。
- **`inverse*` 颜色** 适用于 **深色模式或 Snackbar**。
- **`scrim`** 用于 **模态背景遮罩**。