# UI 组件现代化计划

> 建立日期：2026-08-10  
> 目标：判断自有组件的继续维护价值，优先复用 Compose Multiplatform / Material 3 官方能力，并把自有与官方组件纳入同一套可交互 Demo 和回归流程。

## 决策原则

组件不按“官方有同名 API”直接删除，而按以下五类处理：

| 策略 | 适用条件 | 公开 API 处理 |
|---|---|---|
| 迁移到官方 | 官方功能、跨平台行为、无障碍语义均覆盖，项目没有额外业务语义 | 先提供迁移示例，再弃用自有 API |
| 薄封装 | 官方能力覆盖底层行为，但本库仍提供统一主题、文案或便捷参数 | 保留小型 API，内部委托官方组件 |
| 保留自研 | 官方没有对应组件，或本库具有明确领域能力 | 继续维护，并补测试、语义和 Demo |
| 行为下沉 | 能力只装饰或增强已有 UI，不拥有独立内容结构 | 改为内部或公开 Modifier；外层 Composable 只保留结构与状态 |
| 待定 | 官方 API 仍为 alpha/experimental，或平台能力差异较大 | 保持现状，建立观察项和验证场景 |

## 当前基线

| 项目 | 当前值 | 结论 |
|---|---:|---|
| Kotlin | 2.3.21 | 满足 Compose Multiplatform 1.11 要求 |
| Compose Multiplatform | 1.11.1 | 当前生产稳定线 |
| JetBrains Material 3 | 1.11.0-alpha07 | 与 Compose 1.11 配套，需关注实验 API 变化 |
| Navigation 3 | 1.1.1 | Demo 已使用官方 Navigation 3 |
| 平台 | Android、iOS、JVM、JS、Wasm | 所有新增 Demo 必须位于 `commonMain`，平台专属能力单列 |

详细版本依据与 Sheet 迁移记录见 [Compose UI 与 Material3 更新调研](research/compose-ui-updates-2026-08.md)。

## 组件处置初判

最终差异与一手资料引用见 `official-ui-alternatives.md`。下表用于安排实施批次。

| 自有能力 | 官方候选 | 初判 | 主要差异 / 风险 | Demo 状态 |
|---|---|---|---|---|
| Button | Material 3 Button 系列 | 薄封装或迁移 | 自有 success/warning/danger、size、hairline、loading 不属于官方 Button 单一 API | 自有完整；官方已新增 |
| Cell / ListItem | Material 3 ListItem | 薄封装 | 自有 group/inset/border/link 与 Vant 风格需组合实现 | 自有完整；官方已新增 |
| Checkbox / Switch / RadioCell | Material 3 selection controls | 薄封装或迁移 | 自有尺寸、形状、禁用行为与语义需要回归 | 自有已覆盖；官方已新增 |
| Field / Input / MenuInput | TextField、OutlinedTextField、ExposedDropdownMenu | 薄封装 | 自有行布局、错误文案、自动高度、菜单过滤和焦点策略较多 | 自有已补；官方已新增 |
| Search | Material 3 SearchBar | 待定 | 官方 SearchBar API 仍在演进，自有布局更轻 | 自有已有；官方后续补专项页 |
| CheckButton / Tag | FilterChip、InputChip、AssistChip | 薄封装或迁移 | 选择语义与纯展示标签不可混用 | 自有已有；官方已展示 Filter/Assist |
| Badge | Material 3 Badge / BadgedBox | 迁移候选 | 自有最大值、定位与颜色参数需要适配 | 自有已有；官方已新增 |
| Divider | Material 3 HorizontalDivider / VerticalDivider | 迁移候选 | 自有 padding 参数可由 Modifier 替代 | 自有已补；官方已新增 |
| Toast | Snackbar / SnackbarHost | 保留或薄封装 | Toast 的全局、位置、图标和 loading 语义不同于 Snackbar | 自有已有；官方 Snackbar 已新增 |
| Notify | Snackbar 或自定义 banner | 保留 | Material 3 没有等价顶部通知 banner | 自有已有 |
| Dialog builders | AlertDialog / BasicAlertDialog | 薄封装 | 自有全局队列、挂起回调、输入/结果/进度 Builder 不是单个官方组件能覆盖 | 自有已有；官方 AlertDialog 已新增 |
| Sheet builders | BottomSheet / ModalBottomSheet | 已薄封装 | 官方外壳已接入；保留多 Sheet 管理和 picker/action 内容 | 自有已有；官方 ModalBottomSheet 已新增 |
| Picker / MultiPicker | 无等价滚轮 Picker | 保留 | 官方 DatePicker/TimePicker 是日历/表盘交互，不等价于滚轮 | 自有已补 |
| DatePicker / TimePicker | Material 3 DatePicker / TimePicker | 待定或并存 | 交互模型、输入模式、日期类型与跨平台可用性不同 | 自有已补；官方待专项页 |
| Stepper / Steps | 无通用 Material 3 对应组件 | 保留 | 数值步进器与流程步骤条均为产品层组件 | 自有已有 |
| Popover | Tooltip / DropdownMenu / Popup | 保留或拆分 | 自有 placement、箭头和任意内容组合更通用 | 自有已有 |
| NavBar | TopAppBar | 薄封装或迁移 | 自有标题/返回布局固定；官方支持 scroll behavior、Insets 和语义 | 自有已有；官方页顶栏已使用 |
| IndexBar | 无 Material 3 对应组件 | 保留 | 属于通讯录/长列表索引交互 | 自有已有 |
| Avatar | Image + clip | 保留轻量封装 | 官方无 Avatar 组件，设计系统通常自行组合 | 自有已有 |
| Overlay | Scrim / Dialog / Popup 内部能力 | 限制公开使用 | 单独全屏遮罩容易遗漏焦点、语义和窗口行为 | 自有已补 |
| BDSignalBeamBox | 无官方对应组件 | 保留领域组件 | 北斗信号展示是明确领域能力 | 自有已补 |
| ImagePreview | 无通用 Material 3 对应组件 | 保留平台实现 | 当前仅 Android，需单列平台能力与回归 | Android 页面存在但无 common 导航 |

## Modifier 判断标准

只有同时满足以下条件时，才把能力改为 Modifier：

- 不创建必须独立存在的内容节点；
- 不拥有独立控件角色、选中状态或焦点模型；
- 不负责 Popup、Dialog、Sheet 等窗口或模态层级；
- Modifier 调用顺序不会把关键实现约束泄漏给所有调用方。

当前优先候选是 Cell 底部分割线、ImagePreview 缩放手势、IndexBar 拖动选择和无文字通知红点。Checkbox、Switch、Field、Badge、Overlay、Popover、Dialog、Sheet 等仍应由 Composable 表达结构与语义。优先采用“公开 Composable + 内部 Modifier”的方式，避免调用方重复拼装正确顺序。

## Demo 信息架构

| 层级 | 目的 | 当前实现 |
|---|---|---|
| 自有组件分类页 | 检查既有视觉、参数和交互 | 首页原有 23 个页面 |
| 分类目录页 | 只负责按基础、表单、反馈、展示、导航、Modifier 等类别导航 | 不直接渲染组件示例 |
| 单组件页面 | 一个页面只展示一个组件或一组不可拆分的同族 API | 覆盖默认、禁用、加载、错误、空值、长文本、交互与自定义参数 |
| 官方组件页面 | 按官方组件逐页验证当前依赖版本的真实 API | 与自有页面使用相同状态矩阵，便于对照 |
| 对照信息 | 在单组件页面标记官方候选与行为差异 | 不把多个不同组件堆入一个综合页面 |
| 平台能力页 | 检查 Android/iOS/Desktop/Web 专属能力 | 待建立平台路由与可见性模型 |

## 分阶段任务表

| 阶段 | 工作项 | 状态 | 验收条件 |
|---|---|---|---|
| 0 | 初始化 CodeGraph 并确认索引可同步 | 完成 | `codegraph status` 显示索引最新 |
| 1 | 官方能力与组件替代调研 | 完成 | 每项结论有官方一手资料和特性差异 |
| 1 | 建立分类目录与单组件页面规范 | 已完成 | 目录页不展示组件；每个组件有独立路由和完整状态矩阵 |
| 1 | 拆分官方 Material 3 综合预览 | 待办 | 官方组件逐页展示，综合页删除 |
| 1 | 拆分自有组件覆盖补充页 | 待办 | Input/List/Picker/Date/Time/Radio/Divider/Overlay/BDSignal 各有独立页面 |
| 1 | 多平台编译与桌面/移动视觉检查 | 待重验 | Android、JVM、iOS、JS、Wasm 编译通过；单组件页面无溢出或遮挡 |
| 1 | 首批官方迁移与语义修复 | 进行中 | 已有 Divider、Checkbox、Radio、Button 实现，待按独立任务完成审查与提交 |
| 2 | Button、selection、Badge、Divider 对照页 | 待办 | 同屏状态矩阵、键盘操作、语义说明齐全 |
| 2 | Field/Search/Picker 对照页 | 待办 | 输入法、焦点、错误、只读、RTL、长文本场景齐全 |
| 2 | Dialog/Sheet/Toast/Notify 对照页 | 待办 | dismiss、返回、队列、旋转/尺寸变化场景齐全 |
| 3 | 第一批内部实现迁移与弃用策略 | 待办 | 无公开行为回归，迁移文档和 deprecated replacement 齐全 |
| 3 | Compose UI 自动化与截图基线 | 待办 | 关键组件状态有语义测试和至少 Android/JVM 截图回归 |
| 4 | 删除达到弃用周期的冗余 API | 待办 | 发布说明、版本策略、下游迁移完成 |

## 验证矩阵

每个迁移候选至少检查：默认、禁用、加载/错误、长文本、窄屏、深色主题、RTL、键盘焦点、读屏语义、返回/外部点击、状态保存和快速重复操作。跨平台组件至少编译 Android、JVM、iOS、JS、Wasm；输入与弹层需要补充真机行为检查。

具体任务、审查覆盖率和提交记录维护在 [UI 现代化工作清单](ui-modernization-worklist.md)。每个任务必须经过实现、构建、OCR 委托审查、问题修复、复审和独立提交后，才能标记完成。

## 本轮不直接删除组件的原因

这是已发布的 UI 库，名称相同不代表行为契约相同。Button 的语义色与 loading、Toast 的位置与全局队列、滚轮 Picker、Dialog/Sheet Builder 管理方式都超过官方单个 Composable 的职责。第一轮先把官方基线和对照环境建立起来，再以小批次迁移、弃用周期和回归证据推进，能够避免一次性破坏下游调用方。
