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
| D-003C | 建立官方 Switch 单组件页面 | 已完成 | 五目标编译；Playwright 390px/状态/禁用交互 | 4/4，100%；无高/中问题 | `6be2b9d` |
| D-004A | 建立官方 TextField 单组件页面 | 已完成 | 五目标编译；Playwright 390px/输入/错误/滚动交互 | 4/4，100%；修复 2 项中优先级问题 | `70aa4fe` |
| D-004B | 建立官方 ListItem 单组件页面 | 已完成 | 五目标编译；Playwright 390px/槽位/点击/长文本 | 4/4，100%；无高/中问题 | `612b4ab` |
| D-004C | 建立官方 Badge 单组件页面 | 已完成 | 五目标编译；Playwright 390px/计数/99+/颜色 | 4/4，100%；修复 1 项中优先级问题 | `2149c10` |
| D-004D | 建立官方 Chip 单组件页面 | 已完成 | 五目标编译；Playwright 390px/选择/删除/恢复 | 4/4，100%；无高/中问题 | `26aca39` |
| D-005A | 建立官方 Slider 单组件页面 | 已完成 | 五目标编译；Playwright 390px/连续/离散/范围/禁用/颜色 | 4/4，100%；修复 2 项低优先级问题 | `515935a` |
| D-005B | 建立官方 ProgressIndicator 单组件页面 | 已完成 | 五目标编译；Playwright 390px/确定/不确定/运行/暂停/重置 | 4/4，100%；无高/中问题 | `e87a617` |
| D-005C | 建立官方 Snackbar 单组件页面 | 已完成 | 五目标编译；Playwright 390px/队列/操作/关闭/样式 | 4/4，100%；修复 1 项中优先级问题 | `583fcab` |
| D-005D | 建立官方 AlertDialog 单组件页面 | 已完成 | 五目标编译；Playwright 390px/确认/取消/外部关闭/长文本/主题 | 4/4，100%；修复 1 项低优先级问题 | `26d3663` |
| D-005E | 建立官方 ModalBottomSheet 单组件页面 | 已完成 | 五目标编译；Playwright 390px/部分展开/完全展开/滚动/关闭/主题 | 4/4，100%；修复 2 项低优先级问题 | `2df530b` |
| D-006A | 为自有 Input 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/输入/状态/密码/滚动 | 4/4，100%；Markdown 人工 1/1；修复 1 项低优先级问题 | `0fab6e1` |
| D-006B | 为自有 MenuInput 建立独立页面 | 已完成 | 五目标编译、JVM 测试；Playwright 390px/过滤/选择/空结果/20 项上限/样式 | 5/5，100%；Markdown 人工 1/1；修复 2 项低优先级问题 | `97f7033` |
| D-007A | 为自有 ListItem 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/重载/槽位/点击/长文本 | 4/4，100%；Markdown 人工 1/1；修复 1 项低优先级问题 | `b3803a1` |
| D-007B | 为自有 ListInputItem 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/输入/过滤/空结果/选择/密码/长文本 | 4/4，100%；Markdown 人工 1/1；修复 1 项中优先级问题 | `c5acc8e` |
| D-007C | 为自有 ListMenuItem 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/选择/重置/空列表/长菜单滚动/长文本 | 4/4，100%；Markdown 人工 1/1；修复 2 项低优先级问题 | `c4b5163` |
| D-008A | 为自有 RadioCell 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/互斥选择/禁用/边框/长文本 | 4/4，100%；Markdown 人工 1/1；无高/中问题 | `1406511` |
| D-008B | 为自有 Picker 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/点击吸附/外部选择/多列同步 | 4/4，100%；Markdown 人工 1/1；修复 1 项低优先级问题 | `6b1a289` |
| D-008C | 为自有 DatePicker 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/五种模式/闰年/起止边界/本地时区/语义 | 4/4，100%；Markdown 人工 1/1；修复 2 项中、1 项低优先级问题 | `c90316e` |
| D-008D | 为自有 TimePicker 建立独立页面 | 已完成 | 五目标编译；Playwright 390px/HMS/HM/滚轮交互/起止边界/回调/语义 | 4/4，100%；Markdown 人工 1/1；修复 2 项中优先级问题 | `56317cb` |
| D-009A | 为 Divider 建立官方/自有对照单组件页面 | 已完成 | 五目标编译；Playwright 390px/双入口/横纵方向/Hairline/粗细/颜色/inset/RTL/Intrinsic | 4/4，100%；Markdown 人工 1/1；修复 1 项低优先级问题 | `ee0419c` |
| D-009B | 为 Overlay 建立单组件页面 | 已完成 | 五目标编译；Playwright 390px/居中与底部/内容留白与按钮/外点关闭/点击拦截/Escape 返回 | 5/5，100%；Markdown 人工 1/1；修复 1 项高、2 项低优先级问题 | `535b125` |
| D-009C | 为 BDSignalBeamBox 建立单组件页面 | 已完成 | 五目标编译；Playwright 390px/空数据/上下限/20 路横向滚动/越界钳制/颜色/长文案/AX 语义 | 5/5，100%；Markdown 人工 1/1；修复 2 项高、2 项中优先级问题 | `85d72ec` |
| M-001 | Divider 内部委托官方 Material3 实现 | 已完成 | 五目标编译、JVM 测试通过 | 1/1，100%；无高/中问题 | `f096153` |
| M-002 | 修复 Checkbox、RadioCell、loading Button 的状态与选择语义 | 已完成 | 五目标编译、JVM 测试、语义检查 | 4/4，100%；修复 2 项中优先级问题 | `9d9d151` |
| M-003 | 评估 Cell 底部分割线下沉为 Modifier | 已完成 | 五目标编译；Playwright 390px/LTR/RTL 非对称缩进/动态高度/无分割线 | 2/2，100%；Markdown 人工 1/1；修复 1 项中、1 项低优先级问题 | `ff1d237` |
| M-004 | 评估 ImagePreview 手势下沉为内部 Modifier | 已完成 | 库/Demo Android 编译、手势逻辑复审 | 1/1，100%；无高/中问题 | `a462ce0` |
| M-005 | 评估 IndexBar 拖动选择下沉为内部 Modifier | 已完成 | 五目标编译；Playwright 390px 顶/中/底分段、同段去重、触控取消、单项/空列表/27 项完整模式 | 2/2，100%；Markdown 人工 1/1；修复 1 项低优先级可读性问题 | `0888b03` |
| M-006 | Badge 迁移官方 BadgedBox/Badge 并保留数字格式化适配 | 已完成 | 五目标编译；Playwright 390px 空点/文本/隐藏/阈值/动态 99+/长文本/颜色/Avatar 调用页 | 2/2，100%；Markdown 人工 2/2；修复 1 项中、2 项低优先级问题 | `827df2c` |
| M-007 | Checkbox 方形样式迁官方，保留圆形语义化变体 | 已完成 | 五目标编译；Playwright 390px 方形/圆形切换、禁用零回调、16/22/30/50dp、颜色/Cell | 2/2，100%；Markdown 人工 2/2；修复 1 项低优先级文档问题 | `1401e41` |
| M-008 | 评估 SearchBar 替代边界，ListMenuItem 迁移 ExposedDropdown | 已完成 | 五目标编译；Playwright 390px 选择/空菜单/18 项滚动/返回、官方全屏与 Docked 搜索、旧 Search 动作 | 7/7，100%；Markdown 人工 3/3；修复 1 项高、2 项中、3 项低优先级问题 | `b9f06cf` |
| M-009 | NavBar 迁移 TopAppBar 并提供兼容适配 | 已完成 | 五目标编译；浏览器 390px 自有/官方页、标题避让、RTL、Insets、回调与三种滚动行为 | 8/8，100%；Markdown 人工 2/2；修复 2 项中、2 项低优先级问题 | `2cc033c` |
| M-010 | Dialog 外壳迁官方 modal，保留 Builder 组合能力 | 已完成 | 焦点进入/恢复与隔离、底层语义隐藏、返回/外点策略、窗口层级、多层管理 | 6/6，100%；Markdown 人工 1/1；修复 1 项中、2 项低优先级问题 | 本提交 |
| M-011 | NormalToast 迁 Snackbar，拆分阻塞 Loading 行为 | 已完成 | 队列、播报、取消 | 4/4，100%；Markdown 人工 2/2；修复 1 项中、1 项低优先级问题 | 本提交 |
| M-012 | 设计 token 迁 MaterialTheme + CompositionLocal | 已完成 | 五目标编译；Playwright 390x844 首页截图与主题入口检查 | 5/5，100%；Markdown 人工 1/1；修复 2 项中优先级主题继承问题；Wasm Canvas 控制台错误按 T-001 跟踪 | 本提交 |
| M-013 | 重构 Input 焦点契约并评估直接使用官方 BasicTextField | 已完成 | 五目标编译；Playwright 390x844 首页截图与输入页面入口检查 | 2/2，100%；Markdown 人工 1/1；修复 1 项中优先级焦点释放问题；Wasm Canvas 控制台错误按 T-001 跟踪 | 本提交 |
| M-014 | 评估 MenuInput 迁移官方 ExposedDropdownMenuBox/SearchBar | 已完成 | 五目标编译；Playwright 390x844 首页与菜单输入入口检查 | 1/1，100%；Markdown 人工 1/1；修复 1 项中优先级空菜单展开问题；Wasm Canvas 控制台错误按 T-001 跟踪 | 本提交 |
| M-015 | ListItem 槽位重载迁官方并补静态/禁用语义 | 已完成 | 五目标编译；Playwright 390x844 首页截图与 ListItem 入口检查 | 2/2，100%；Markdown 人工 1/1；修复 2 项中优先级问题；Wasm Canvas 控制台错误按 T-001 跟踪 | 本提交 |
| M-016 | 评估移除 ListInputItem 组合包装并迁官方 ListItem + TextField/ExposedDropdown | 已完成 | 五目标编译；Playwright 390x844 首页与 ListInputItem 入口检查 | 3/3，100%；Markdown 人工 1/1；修复 1 项中优先级禁用/只读组合问题；Wasm Canvas 控制台错误按 T-001 跟踪 | 本提交 |
| M-017 | 评估 RadioCell 并入 Cell 尾随槽并修正长文本测量 | 已完成 | 五目标编译；Playwright 390x844 首页与 RadioCell 入口检查 | 2/2，100%；Markdown 人工 1/1；无高/中问题；Wasm Canvas 控制台错误按 T-001 跟踪 | 本提交 |
| M-018 | 加固 Picker/MultiPicker 输入契约和无障碍操作 | 已完成 | 空集合、选中长度、Modifier、禁用、键盘/读屏增减、独立列/级联模型 | 3/3，100%；Markdown 人工 1/1；修复 2 项中优先级问题；Wasm 运行态连接受限，编译与 HTTP 启动检查完成 | 本提交 |
| M-019 | 评估 DatePicker 通用场景迁官方日历组件并加固滚轮差异化能力 | 待办 | 官方/滚轮兼容 API、时区/DST、非法范围、Modifier、禁用、键盘/读屏 | 待执行 | 待提交 |
| M-020 | 评估 TimePicker 普通 HM 场景迁官方并加固有限范围 HMS 滚轮 | 待办 | 参数校验、越界值、HM 秒契约、联动竞态、Modifier、禁用、键盘/读屏、官方兼容 API | 待执行 | 待提交 |
| M-021 | 迁移 Divider 调用点并评估弃用自有兼容层 | 待办 | 21 个横线调用点、纵线公开 API、0.5dp/Hairline 视觉、逻辑方向 inset、弃用周期 | 待执行 | 待提交 |
| M-022 | 加固 BDSignalBeamBox 领域契约与主题适配 | 待办 | 明确数据模型、Modifier、长编号、暗色主题、本地化、RTL | 待执行 | 待提交 |
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
| 2026-08-12 | D-004A | 新增 Material TextField 独立页面，覆盖 Filled/Outlined、错误、密码、前后内容、只读/禁用和多行计数；修复空邮箱状态与超长粘贴边界，五目标编译和 390px 输入/滚动验收通过，OCR 4/4 |
| 2026-08-12 | D-004B | 新增 Material ListItem 独立页面，覆盖一至三行文本、前导/尾随槽、点击/禁用和长文本；五目标编译与 390px 点击/滚动验收通过，OCR 4/4 |
| 2026-08-12 | D-004C | 新增 Material Badge 独立页面，覆盖空点/数字/文本、图标锚点、动态计数、99+ 格式化和自定义颜色；补充可直接检查的上限样例，五目标编译与 390px 交互验收通过，OCR 4/4 |
| 2026-08-12 | D-004D | 新增 Material Chip 独立页面，覆盖 Assist/ElevatedAssist/Filter/Input/Suggestion、禁用、图标和长文本；五目标编译与 390px 选择/删除/恢复验收通过，OCR 4/4 |
| 2026-08-12 | D-005A | 新增 Material Slider 独立页面，覆盖连续值、离散刻度、范围、禁用和自定义主题色；对齐范围初值刻度并清理无效导入，五目标编译与 390px 拖动验收通过，OCR 4/4 |
| 2026-08-12 | D-005B | 新增 Material ProgressIndicator 独立页面，覆盖确定/不确定的线性与环形指示器、动态开始/暂停/重置和自定义样式；五目标编译与 390px 状态验收通过，OCR 4/4 |
| 2026-08-12 | D-005C | 新增 Material Snackbar 独立页面，覆盖短消息、持续操作、顺序队列、手动关闭、自定义样式和结果反馈；修复跨消息共享样式状态，五目标编译与 390px 交互验收通过，OCR 4/4 |
| 2026-08-12 | D-005D | 新增 Material AlertDialog 独立页面，覆盖确认、取消、外部关闭、危险长文本、图标插槽和自定义主题；补齐明确内容色，五目标编译与 390px 交互验收通过，OCR 4/4 |
| 2026-08-12 | D-005E | 新增 Material ModalBottomSheet 独立页面，覆盖部分/完全展开、内部滚动、按钮/遮罩关闭和自定义主题；局部声明实验性 API，修复移动端标题截断并补足长内容，五目标编译与 390px 交互验收通过，OCR 4/4 |
| 2026-08-12 | D-006A | 新增 Input 独立页面，覆盖基础输入、焦点、只读/禁用、密码与 IME、多行边界、对齐和样式；补齐字段语义与密码显隐交互，五目标编译和 390px 验收通过；OCR 4/4、Markdown 人工 1/1，移除提前声明的不可达路由，现有焦点契约缺陷转 M-013 跟踪 |
| 2026-08-12 | D-006B | 新增 MenuInput 独立页面，覆盖过滤选择、空结果、20 项上限、前后缀/错误/固定宽度、密码和 Next；修正 Next 回调，五目标编译、JVM 测试和 390px 验收通过；OCR 5/5、Markdown 人工 1/1，移除无效导入并回退未经跨平台验证的行为改动；Wasm 返回/外点关闭与内部输入语义限制转 M-014 跟踪 |
| 2026-08-12 | D-007A | 新增 ListItem 独立页面，覆盖字符串/插槽重载、资源图标、必填、尾随、中心槽、排列、点击和长文本；五目标编译与 390px 验收通过；OCR 4/4、Markdown 人工 1/1，修正长文本样例说明；静态/禁用语义和长文本尾随测量转 M-015 跟踪 |
| 2026-08-12 | D-007B | 新增 ListInputItem 独立页面，覆盖基础输入、图标/必填/说明、错误/前后缀、菜单过滤选择、密码、固定/自适应宽度和长文本组合；五目标编译与 390px 输入/空结果/选择/显隐验收通过；OCR 4/4、Markdown 人工 1/1，补齐长标题边界；薄组合层的父点击语义、禁用/只读、焦点和窄屏测量转 M-016 跟踪 |
| 2026-08-12 | D-007C | 新增 ListMenuItem 独立页面，覆盖未匹配占位、受控选择/外部重置、图标/必填、空列表、18 项滚动菜单、固定/自适应宽度和长文本样式；五目标编译与 390px 展开/选择/滚动验收通过；OCR 4/4、Markdown 人工 1/1，提升静态菜单数据并修正空列表说明；空态反馈、展开语义和窄屏测量转 M-008 跟踪 |
| 2026-08-12 | D-008A | 将原 D-008 拆为四个单组件任务；新增 RadioCell 独立页面，覆盖互斥组、整行点击、禁用选中/未选中、分割线和长文本；五目标编译与 390px 选择/禁用验收通过；OCR 4/4、Markdown 人工 1/1；组件已委托官方 RadioButton，不适合改 Modifier，合并 Cell 尾随槽与长文本测量转 M-017 跟踪 |
| 2026-08-12 | D-008B | 新增 Picker/MultiPicker 组件族独立页面，覆盖单列点击吸附、外部上一/下一、自定义样式、三列选择和外部组合同步；五目标编译与 390px 验收通过；OCR 4/4、Markdown 人工 1/1，修正页面语义容器格式；组件无官方直接替代且不适合 Modifier，输入契约与无障碍操作转 M-018 跟踪 |
| 2026-08-12 | D-008C | 新增 DatePicker 独立页面，覆盖 YMD/YM/Y/MD/M、定制后缀、闰年、起止边界、本地时区时间戳和滚轮语义；五目标编译与 390px 验收通过；OCR 4/4、Markdown 人工 1/1，修正 YMD 月后缀、UTC 边界日期和滚动项重复分配；官方日历组件仅适合通用场景，当前五模式滚轮不适合 Modifier，兼容迁移与契约加固转 M-019 跟踪 |
| 2026-08-12 | D-008D | 新增 TimePicker 独立页面，覆盖 HMS/HM、定制后缀、非整点范围、起止/中间预设、真实滚轮交互、总秒数与回调分量；五目标编译与 390px 验收通过；OCR 4/4、Markdown 人工 1/1，修正 HM 隐藏秒状态和 Wasm 模式/当前值语义；官方组件仅适合普通 HM，有限范围 HMS 滚轮不适合 Modifier，参数契约、联动和双轨迁移转 M-020 跟踪 |
| 2026-08-12 | D-009A | 将原 D-009 拆为 Divider、Overlay、BDSignalBeamBox 三个单组件任务；新增自有/Material 3 双入口共享 Divider 对照页，覆盖 Hairline、0.5/1/2/3/4dp、颜色、逻辑方向 inset、RTL、宽度限制、固定高度和 IntrinsicSize.Min；五目标编译与 390px 验收通过；OCR 4/4、Markdown 人工 1/1，修正方向性文案；Divider 是布局节点不适合 Modifier，自有薄适配层迁移与弃用转 M-021 跟踪 |
| 2026-08-12 | D-009B | 新增 Overlay 独立页面，覆盖居中/底部对齐、内容按钮、内容留白、外点关闭、点击拦截与 Escape 返回；修复内容留白误关闭，遮罩色改用主题 scrim，并避免 pointerInput 捕获旧回调；五目标编译与 390px 验收通过；OCR 5/5、Markdown 人工 1/1，修正文档漂移；官方库没有等价的独立 Overlay，组件不适合 Modifier，普通弹窗应迁 Dialog/AlertDialog/BasicAlertDialog，焦点隔离、底层语义、窗口层级与多层策略转 M-010 跟踪 |
| 2026-08-12 | D-009C | 新增 BDSignalBeamBox 独立领域页面，覆盖常规、空数据、0/60 上下限、20 路超长、越界输入、颜色和长文案；修复越界强度直接进入布局、超长数据挤压、空态伪造 0 号卫星和缺少信号语义，20 路可横向访问且 AX 树完整暴露逐柱强度；五目标编译与 390px 验收通过；OCR 5/5、Markdown 人工 1/1；官方无同类领域组件，整体不适合 Modifier，数据模型、Modifier 接入、暗色/本地化/RTL 转 M-022 跟踪 |
| 2026-08-12 | M-003 | 仅将 CellRow 的底部分割线下沉为私有 drawWithContent Modifier，保留 0.5dp 空内容高度、主题色与 RTL 逻辑缩进；Cell 和 CellGroup 的 Divider 会参与公开可观察的布局高度，继续保留 Composable 节点；五目标编译与 390px LTR/RTL、动态高度、无分割线验收通过；OCR 2/2、Markdown 人工 1/1，修复 RTL 物理坐标映射和空内容高度兼容问题 |
| 2026-08-12 | M-005 | IndexBar 保留公开 Composable，仅将指针识别、分段命中和取消清理下沉为私有 Modifier；修复空列表越界、悬停误选、分段阈值偏差、取消残留、旧回调捕获和事件未消费；Demo 补充精简、单项、空列表和完整模式；五目标编译与 390px Playwright 验收通过，按下/释放/touchCancel 提示状态正确；OCR 2/2、Markdown 人工 1/1；键盘操作和完整读屏语义仍由 A-001 跟踪 |
| 2026-08-12 | M-006 | 保留 `BadgeBox`、字符串 `Badge` 和数字 `Badge` 三个公开签名，内部委托 Material 3 `BadgedBox`/`Badge`；接受官方空点 6dp、文本最小 16dp 和相对定位，保留锚点 4dp 留白、32dp 文本裁剪、非正数隐藏与 `max+` 格式化；新增 `material3-badge-evaluation.md` 一手资料评估，Demo 补全隐藏/阈值/动态/长文本/颜色场景；五目标编译与 390px、Avatar 调用页验收通过；OCR 2/2、Markdown 人工 2/2，修正文档尺寸漂移并移除无效缓存；官方 Badge 不内置业务语义，Wasm 空点语义未暴露，继续由 A-001 跟踪 |
| 2026-08-12 | M-007 | 保持 `Checkbox`、`CheckboxType` 及参数契约不变，方形视觉委托 Material 3 Checkbox，圆形保留自绘，并由外层 `toggleable` 统一二态、禁用和精确尺寸行为；新增 `material3-checkbox-evaluation.md` 一手资料评估，Demo 补全方形/圆形、禁用、16/22/30/50dp、颜色和 Cell 场景；五目标编译与 390px 点击/滚动验收通过，禁用回调保持 0；OCR 2/2、Markdown 人工 2/2，修正禁用浅色 KDoc；为兼容旧布局未采用官方 48dp 最小布局，键盘焦点未能在 Wasm Canvas 浏览器自动化中稳定定位，AX 树的 Checkbox/disabled 映射也不完整，后续由 A-001/T-001 的平台 UI 与无障碍回归继续跟踪 |
| 2026-08-12 | M-008 | 评估确认旧 `Search` 是 38dp 行内搜索输入，不等价于 56dp 且带展开结果层的官方 SearchBar，因此保留兼容 Composable 且不改 Modifier；新增官方 SearchBar 独立页，覆盖全屏展开、查询/loading/清除/空态/显式返回和 Docked 建议；`ListMenuItem` 保持公开签名和值映射，委托 `ExposedDropdownMenuBox`、单一 `PrimaryNotEditable` 锚点和官方菜单测量/返回能力，并用可配置 placeholder 补空菜单反馈；五目标编译与 390px 选择、空态、18 项滚动到底、窄屏长文本、官方搜索及旧 Search 动作回调验收通过；OCR 7/7、Markdown 人工 3/3，修复 SearchBar 无限高度 Wasm 崩溃、父级空点击语义、空态不可见、Demo 协程生命周期和禁用状态展示问题；Wasm Escape 未触发 BackHandler，页面提供显式返回按钮，Canvas AX/键盘桥差异继续由 T-001 跟踪 |
| 2026-08-12 | M-009 | 保持 `NavBar` 公开签名，Center/CenterStart 分别委托 Material 3 `CenterAlignedTopAppBar`/`TopAppBar`，显式保留 60dp、标题样式、背景、Insets 开关和底部分割线，其他 Alignment 继续旧布局兜底；返回改用 48dp `IconButton`，补中英文本地化和 RTL 镜像；自有 NavBar 独立页覆盖真实回调、长标题、不对称 action、背景、状态栏、RTL 和兜底，官方 TopAppBar 独立页覆盖 Start/Center、长标题及 pinned/enter-always/exit-until-collapsed；五目标编译与 390px 浏览器验收通过；OCR 8/8、Markdown 人工 2/2，修复返回描述不可本地化、Scaffold 水平安全区遗漏、官方 action 空回调和兼容兜底 RTL 未镜像；真实 Android/iOS 刘海安全区与字体缩放继续由 T-001 跟踪 |
| 2026-08-13 | M-010 | `Dialog` 外壳委托 Compose UI 官方 modal，保留 `DialogBuilder`、`DialogBox`、任意 `contentAlignment` 和 id 管理；新增关闭策略纯逻辑测试、稳定 `key(id)`、六组特性预览（关闭策略、底层隔离、BottomCenter、输入/IME、多层、Progress 更新）；完成五目标编译、390x844 浏览器交互和官方版本评估文档；Wasm Canvas 语义桥的焦点节点恢复不作为全平台自动保证，继续由 T-001/A-001 跟踪 | 6/6，100%；Markdown 人工 1/1；修复 1 项中、2 项低优先级问题 | 待提交 |
| 2026-08-13 | M-011 | 普通 `NormalToast` 委托 Material 3 `SnackbarHostState` 队列，保留 Success/Fail 图标提示和阻塞 `LoadingToast`；新增时长映射测试、队列/长时长/位置差异/Loading 生命周期 Demo 和官方替代性评估；完成五目标编译、390x844 队列与阻塞验证；普通提示固定底部且不再保留 Top/Center 位置语义，图标和 Loading 差异继续由兼容层维护 | 4/4，100%；Markdown 人工 2/2；修复 1 项中、1 项低优先级问题 | 待提交 |
| 2026-08-13 | M-012 | 新增 `AppBrandColors`、`AppDesignTokens`、`LocalAppColors/LocalAppTypography/LocalAppShapes` 与 `AppTheme`；默认接入 MaterialTheme，旧 `AppColor` 保持兼容 façade；颜色页增加局部品牌色覆盖开关，形状页补充官方形状与半径 token 对照；修复嵌套主题默认 colors、品牌色、typography、shapes 重置问题；Android/JVM/JS/Wasm/iOS Simulator 编译通过，390x844 首页验证通过，服务已停止 | 5/5，100%；Markdown 人工 1/1；修复 2 项中优先级问题 | 本提交 |
| 2026-08-13 | M-013 | `Input` 的焦点请求改用 `LaunchedEffect`，仅在受控状态变化时请求/释放自身焦点；`MenuInput` 补齐获得/失去焦点边沿回调并启用官方菜单外点/返回关闭；Demo 增加清除焦点操作；Android/JVM/JS/Wasm/iOS Simulator 编译通过，390x844 首页验证通过，服务已停止 | 2/2，100%；Markdown 人工 1/1；修复 1 项中优先级焦点释放问题 | 本提交 |
| 2026-08-13 | M-014 | `MenuInput` 弹出层迁移 Material 3 `ExposedDropdownMenuBox`，使用 `PrimaryEditable` 官方锚点与官方菜单测量/关闭行为；保留字符串过滤、最多 20 项、空结果、密码显隐、IME Next、前后缀、错误和兼容回调；无菜单时不展开空弹层；Android/JVM/JS/Wasm/iOS Simulator 编译通过，390x844 首页验证通过，服务已停止 | 1/1，100%；Markdown 人工 1/1；修复 1 项中优先级空菜单展开问题 | 本提交 |
| 2026-08-13 | M-015 | 自有 `ListItem` 增加 `enabled` 与可空插槽 `onClick`，静态项不再注册点击语义，可点击项统一标记 `Role.Button`；字符串和槽位重载补充禁用 Demo、计数验证、长文本与槽位对照；保持新增参数位于签名末尾兼容旧位置参数；Android/JVM/JS/Wasm/iOS Simulator 编译通过，390x844 首页验证通过，服务已停止 | 2/2，100%；Markdown 人工 1/1；修复 2 项中优先级问题 | 本提交 |
| 2026-08-13 | M-016 | `ListInputItem` 保留兼容组合层，新增末尾 `enabled/readOnly` 参数并透传到官方 `BasicTextField` 与 `ExposedDropdownMenuBox`；禁用/只读时不请求焦点、不展开菜单；Demo 补充只读与禁用场景，验证父行不拦截输入焦点；Android/JVM/JS/Wasm/iOS Simulator 编译通过，390x844 首页验证通过，服务已停止 | 3/3，100%；Markdown 人工 1/1；修复 1 项中优先级禁用/只读组合问题 | 本提交 |
| 2026-08-13 | M-017 | `RadioCell` 保持官方 `RadioButton` 与独立 `Role.RadioButton` 语义，长标题增加稳定权重避免挤压右侧控件；Demo 增加 `Cell.afterValue` + 官方 RadioButton 对照，明确父行点击与独立单选语义差异；Android/JVM/JS/Wasm/iOS Simulator 编译通过，390x844 首页验证通过，服务已停止 | 2/2，100%；Markdown 人工 1/1；无高/中问题 | 本提交 |
| 2026-08-13 | M-018 | `Picker`/`MultiPicker` 允许空集合与缺失/非法选中值安全回退，新增 `enabled`、列级描述、读屏上一项/下一项和方向键操作；滚轮吸附保护末项边界；`MultiPickerSheetBuilder` 移除空列默认 `first()`；Demo 增加禁用、非法回退、空态、空列和列级语义场景；Android/JVM/JS/Wasm/iOS Simulator 编译通过，Playwright CLI 因 npx 启动受限未取得快照，后台 Demo HTTP 启动后连接受限，服务已停止 | 3/3，100%；Markdown 人工 1/1；修复 2 项中优先级问题 | 本提交 |
