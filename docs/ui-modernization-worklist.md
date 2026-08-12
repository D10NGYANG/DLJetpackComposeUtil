# UI 现代化工作清单

> 建立日期：2026-08-12  
> 状态只允许：待办、进行中、审查中、已完成、阻塞。  
> 完成定义：实现完成、相关构建/测试通过、OCR 委托审查覆盖率 100%、高/中优先级问题修复并复审、产生独立 Git 提交。

## 执行规则

1. 每次只处理一个可独立验收的任务或紧密相关的小批次。
2. 开始任务时将状态改为“进行中”，进入审查时改为“审查中”。
3. 使用 `ocr delegate preview` 获取完整文件集合，再用 `ocr delegate rule` 获取规则；每个文件必须标记 reviewed 或说明 skipped 原因。
4. 发现问题后继续修复和复审，直到没有未处理的高/中优先级问题。
5. 构建、测试和审查通过后创建一次提交；任务提交内的提交栏记为“本提交”，实际短哈希在下一次更新清单时回填，避免因修改哈希而循环 amend。
6. Demo 分类目录页只导航，不展示组件；单组件页面必须充分覆盖该组件的状态、交互、布局和自定义能力。
7. OCR 不支持的文件仍要出现在 `preview` 排除清单中，并由人工逐文件审查；审查栏记录排除原因和人工覆盖率。

## Demo 页面标准

每个单组件页面至少评估以下内容，不适用项在页面任务说明中注明：

- 默认与常用场景；
- enabled、disabled、loading、error、empty/readOnly 等状态；
- 所有公开样式枚举、尺寸、颜色和内容插槽；
- 点击、输入、选择、拖动、返回、外部关闭等交互；
- 长文本、窄屏、字体放大及必要的 RTL 场景；
- 官方候选组件及与自有实现的关键差异；
- 可由辅助功能树识别的角色、状态和操作。

## 任务清单

| ID | 任务 | 状态 | 验证 | OCR 审查 | 提交 |
|---|---|---|---|---|---|
| P-001 | 更新现代化计划，加入行为下沉、单组件 Demo 和逐步提交规则 | 已完成 | 三份文档交叉检查、`git diff --check` | OCR `unsupported_ext`；人工 3/3，100% | `92544d6` |
| R-001 | 审查并整理当前第一阶段未提交改动 | 已完成 | 五目标编译、JVM 测试通过 | 12/12，100%；聚合页问题已转 D-001 修复 | `0378fa0` |
| D-001 | 建立 Demo 分类目录和可扩展的单组件路由骨架 | 已完成 | 五目标编译；Playwright 桌面/390px 导航 | 6/6，100%；无高/中问题 | `37c4df8` |
| D-002 | 拆分官方 Button 页面并完整展示按钮族特性 | 已完成 | 五目标编译；Playwright 桌面/390px/交互 | 4/4，100%；无高/中问题 | `fc5fad1` |
| D-003A | 建立官方 Checkbox 单组件页面 | 已完成 | 五目标编译；Playwright 390px/三态交互 | 4/4，100%；无高/中问题 | `2add73a` |
| D-003B | 建立官方 RadioButton 单组件页面 | 已完成 | 五目标编译；Playwright 390px/选择组/禁用交互 | 4/4，100%；无高/中问题 | `8f7d69b` |
| D-003C | 建立官方 Switch 单组件页面 | 已完成 | 五目标编译；Playwright 390px/状态/禁用交互 | 4/4，100%；无高/中问题 | 本提交 |
| D-004 | 拆分官方 TextField、ListItem、Badge、Chip 单组件页面 | 待办 | 输入/长文本/窄屏 | 待执行 | 待提交 |
| D-005 | 拆分官方 Slider、Progress、Snackbar、AlertDialog、ModalBottomSheet 页面 | 待办 | 浮层/返回/状态交互 | 待执行 | 待提交 |
| D-006 | 为自有 Input 与 MenuInput 建立独立页面 | 待办 | 输入/过滤/焦点/错误 | 待执行 | 待提交 |
| D-007 | 为 ListItem、ListInputItem、ListMenuItem 建立同族页面 | 待办 | 槽位/禁用/长文本 | 待执行 | 待提交 |
| D-008 | 为 RadioCell、Picker、DatePicker、TimePicker 建立单组件页面 | 待办 | 模式/边界/时区/语义 | 待执行 | 待提交 |
| D-009 | 为 Divider、Overlay、BDSignalBeamBox 建立单组件页面 | 待办 | Modifier/模态/领域数据 | 待执行 | 待提交 |
| M-001 | Divider 内部委托官方 Material3 实现 | 已完成 | 五目标编译、JVM 测试通过 | 1/1，100%；无高/中问题 | `f096153` |
| M-002 | 修复 Checkbox、RadioCell、loading Button 的状态与选择语义 | 已完成 | 五目标编译、JVM 测试、语义检查 | 4/4，100%；修复 2 项中优先级问题 | `9d9d151` |
| M-003 | 评估 Cell 底部分割线下沉为 Modifier | 待办 | 布局截图、调用影响 | 待执行 | 待提交 |
| M-004 | 评估 ImagePreview 手势下沉为内部 Modifier | 已完成 | 库/Demo Android 编译、手势逻辑复审 | 1/1，100%；无高/中问题 | `a462ce0` |
| M-005 | 评估 IndexBar 拖动选择下沉为内部 Modifier | 待办 | 指针/取消/键盘回归 | 待执行 | 待提交 |
| M-006 | Badge 迁移官方 BadgedBox/Badge 并保留数字格式化适配 | 待办 | 状态矩阵、语义快照 | 待执行 | 待提交 |
| M-007 | Checkbox 方形样式迁官方，保留圆形语义化变体 | 待办 | 视觉与选择语义 | 待执行 | 待提交 |
| M-008 | Search 与 ListMenuItem 迁移官方 SearchBar/ExposedDropdown | 待办 | 焦点、键盘、返回 | 待执行 | 待提交 |
| M-009 | NavBar 迁移 TopAppBar 并提供兼容适配 | 待办 | Insets、RTL、长标题 | 待执行 | 待提交 |
| M-010 | Dialog 外壳迁官方 modal，保留 Builder 组合能力 | 待办 | 焦点、返回、外点关闭 | 待执行 | 待提交 |
| M-011 | NormalToast 迁 Snackbar，拆分阻塞 Loading 行为 | 待办 | 队列、播报、取消 | 待执行 | 待提交 |
| M-012 | 设计 token 迁 MaterialTheme + CompositionLocal | 待办 | light/dark、子树覆盖 | 待执行 | 待提交 |
| A-001 | 为 Stepper、IndexBar、Steps、BDSignalBeamBox 补齐键盘和语义 | 待办 | UI 测试、语义快照 | 待执行 | 待提交 |
| A-002 | 建立 Adaptive 五目标验证 spike | 待办 | Android/iOS/JVM/JS/Wasm | 待执行 | 待提交 |
| T-001 | 建立关键单组件页面的 Compose UI 与截图回归基线 | 待办 | Android/JVM 自动化 | 待执行 | 待提交 |

## 进度记录

| 日期 | 任务 | 进展 |
|---|---|---|
| 2026-08-12 | P-001 | 完成 Modifier 判定、单组件页面规范和逐任务验证/审查/提交闭环；OCR 排除 Markdown，已人工审查 3/3 |
| 2026-08-12 | R-001 | 审查原始 12 个代码/配置文件；组件改动拆入 M-001/M-002/M-004，聚合页问题由 D-001 修复；五目标编译与 JVM 测试通过 |
| 2026-08-12 | D-001 | 首页收敛为来源目录，新增分类与单组件目录路由，移除聚合预览；五目标编译与桌面/390px Playwright 验收通过，OCR 6/6 |
| 2026-08-12 | M-001 | HorizontalDivider/VerticalDivider 委托 Material3 官方实现，保留原参数契约；五目标编译和 JVM 测试通过，OCR 1/1 |
| 2026-08-12 | M-002 | 修复 Checkbox/RadioCell/Button 状态语义与 RadioSheet 选择组；复审关闭 Modifier 缓存和 loading 名称问题，五目标编译与 JVM 测试通过，OCR 4/4 |
| 2026-08-12 | M-004 | ImagePreview 保留公开 Composable，将缩放、平移、复位和关闭下沉为私有 Modifier；Android 库/Demo 编译通过，OCR 1/1；真机多指回归留待平台测试 |
| 2026-08-12 | D-002 | 新增 Material Button 独立页面，覆盖五种按钮变体、禁用/加载、图标文本、全宽和长文本；五目标编译与桌面/390px/交互验收通过，OCR 4/4 |
| 2026-08-12 | D-003A | 新增 Material Checkbox 独立页面，覆盖交互/禁用、三态、整行点击和自定义颜色；五目标编译与 390px 三态循环验收通过，OCR 4/4 |
| 2026-08-12 | D-003B | 新增 Material RadioButton 独立页面，覆盖互斥选择组、禁用与长文本、紧凑排列和自定义颜色；五目标编译与 390px 选择/禁用交互验收通过，OCR 4/4 |
| 2026-08-12 | D-003C | 新增 Material Switch 独立页面，覆盖整行切换、基础与禁用、拇指图标和自定义颜色；五目标编译与 390px 状态/禁用交互验收通过，OCR 4/4 |
