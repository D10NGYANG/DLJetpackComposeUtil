# Material 3 Snackbar / Toast 替代性评估

评估日期：2026-08-13
评估对象：`NormalToast`、`IconToast`、`LoadingToast` 与 `SnackbarHostState`
项目依赖：Compose Multiplatform Material 3 `1.11.0-alpha07`

## 结论

普通文字 `NormalToast` 适合迁移到 Material 3 `SnackbarHostState` / `SnackbarHost`：官方实现提供公平队列、单条展示、超时、动作结果、手动关闭、无障碍推荐时长和取消清理。`UiViewModelManager.showToast` 的业务入口可以保留，由 `UiViewModel` 将消息投递到官方 HostState。

这不是完全等价替换：

| 能力 | 旧 NormalToast | Material 3 Snackbar | 本步处理 |
|---|---|---|---|
| 位置 | Top / Center / Bottom | 宿主底部 | 保留 `ToastPosition` 参数以兼容调用，普通消息统一在 Snackbar 宿主底部展示 |
| 队列 | 新消息覆盖旧消息 | `Mutex` 公平排队 | 采用官方队列，连续调用不会丢消息 |
| 时长 | 任意毫秒 | Short / Long / Indefinite | 映射为 `duration <= 4000ms` 使用 Short，否则 Long；动作 API 另行使用 Indefinite |
| 语义 | 普通文本 | live region、pane title、dismiss action | 采用官方播报与关闭语义 |
| 图标提示 | Success / Fail 居中图标面板 | Snackbar 不提供图标模板 | 保留 `IconToast`，不强行迁移 |
| Loading | 阻塞触摸与返回 | Snackbar 可被关闭且不阻塞 | 保留独立 `LoadingToast`，拆分为阻塞反馈 |

## 官方行为

精确版本的 `SnackbarHostState` 使用 `Mutex` 保证同一时刻只有一条消息展示；后续 `showSnackbar` 挂起排队，调用协程取消时，当前或排队消息会移除。`SnackbarHost` 负责按 `SnackbarDuration` 自动 dismiss，并在语义节点中设置 polite live region、pane title 和 dismiss 动作。

Material 3 Snackbar 默认贴近宿主底部，适合“非阻塞、可稍后继续”的操作反馈。它不应替代需要锁定外部操作、显示持续进度或居中图标状态的 Loading / Success / Fail 反馈。

## Loading 拆分边界

`LoadingToast` 保留为阻塞层，继续消费外部触摸并拦截返回；业务必须主动 `hideLoading()`。Demo 中的自动隐藏任务使用页面 `rememberCoroutineScope`，离开页面时由结构化并发取消，避免遗留不可更新的阻塞层。

后续如果需要 Material 3 官方进度反馈，应单独评估 `LinearProgressIndicator` / `CircularProgressIndicator` 与非阻塞 Snackbar 的组合，不应把 Loading 的关闭语义隐式改成 Snackbar timeout。

## 官方来源

- Gradle 缓存中的 Material 3 `1.11.0-alpha07` `SnackbarHost.kt` sources JAR：`SnackbarHostState` 的公平 `Mutex` 队列、挂起取消、`SnackbarHost` 超时与语义实现。
- [Android Developers：Snackbar](https://developer.android.com/develop/ui/compose/components/snackbar)：官方 Snackbar 用途、操作和 `SnackbarHostState` 使用方式。
