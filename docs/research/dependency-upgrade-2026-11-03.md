# 依赖升级与行为验收（2026-11-03）

## 交付结果

完成本轮稳定版候选更新、Android targetSdk 36 适配检查、跨平台构建和真实 UI 验收。库公开组件 API 未变化；Android Demo 的选图入口迁移到官方 Activity Result，修复验证中发现的 Android Photo Picker 取消后永久等待问题。本报告主体记录发布前验收，随后正式切版为 3.4.1；发布与消费验证见下节，升级指南见 [3.4.1](../releases/3.4.1.md)。

| 项目 | 原版本 | 最终版本 |
|---|---|---|
| Compose 插件与 Runtime/UI/Foundation/Resources/Tooling | 1.12.1 | 1.12.2 |
| Gradle Wrapper | 9.8.1 | 9.8.2 |
| Kotlin serialization JSON | 1.11.0 | 1.12.0 |
| JetBrains Navigation3 | 1.1.2 | 1.2.0 |
| AndroidX Core KTX | 1.18.0 | 1.19.1 |
| DLCommonUtil | 0.8.1 | 1.0.2 |
| ben-manes versions | 0.54.0 / com.github ID | 0.67.0 / io.github ID |
| Android targetSdk | 34 | 36 |
| checkout | v4 | v7.0.1 |
| setup-java | v3 / Temurin 17 | v6.1.0 / Corretto 21 |
| upload-pages-artifact | v3 | v5.0.0 |
| deploy-pages | v4 | v5.0.1 |

保留 Kotlin 2.4.21、AGP 9.1.1、Material3 1.12.0-alpha03、minSdk 26、compileSdk 37 及其余已核对的稳定版本。AGP 9.5.0 超出当前 Kotlin 官方完整支持范围，不机械升级；Material3 维持 Compose 1.12.2 官方配套 alpha。Gradle 9.8.2 仍高于 Kotlin 官方完整测试上限 9.7.0，本次以实际构建验证提供项目证据，不等于官方矩阵覆盖。

## API 与实际故障适配

1. **依赖检查插件 ID 迁移**：旧 `com.github.ben-manes.versions` 已弃用，改为 `io.github.ben-manes.versions`。原 Task 类型和稳定版本过滤继续有效；新版 `dependencyUpdates` 配置缓存模式执行成功。部分远程坐标查询失败仍保留在报告中，不将任务成功等同全部查询成功。
2. **选图取消根因与修复**：实际 DLAppUtil 2.7.1 源码在 `PickVisualMedia` 回调中仅处理非 null URI；取消时不向等待中的 Flow 发值。华为 Android 12 走旧选图分支未复现，Android 16 系统 Photo Picker 取消后超时。Demo 改用 `rememberLauncherForActivityResult(PickVisualMedia)`，读取 URI 到独立缓存文件供既有 `ImagePreview(path)` 使用；取消无需启动/等待协程，读取失败提示错误，关闭预览或离开组合时删除缓存。没有修改库 ImagePreview 的文件路径 API，也未修改或发布外部 DLAppUtil。
3. **设备回归跟随真实入口**：选图测试改为点击 Demo 的“选择图片”按钮，验证 Activity 重建后仍可选图、绿色测试图片确实被绘制、单击关闭后缓存删除、系统选择器可连续打开/取消，以及无效 URI 显示错误并清理缓存。保留 Activity 引用清理与强制 Sheet 返回拦截测试。
4. **Lint 精确处理**：DLAppUtil 的未使用 `NotificationController` 触发 NotificationPermission 错误。已核实 Demo 无系统通知调用，新增模块级 `lint.xml` 仅按该依赖类名匹配排除，不申请无用途权限，不关闭项目自己的通知权限检查。其余非阻塞警告包括 targetSdk 未追 37、资源目录冗余、单色图标等，未扩大本轮修改。
5. serialization 1.12 的异常包装变化、DLCommonUtil 1.x 的范围检查变化已纳入审查；当前调用没有需要改签名的编译失败。AreaStore 内置 JSON 通过 IndexBar 页面真实加载验证。Navigation3 通过 23 页前进/返回、Sheet 关闭后保留底层页面验证。

## 构建与测试证据

| 检查 | 结果 |
|---|---|
| library JVM 单元测试 | 5/5 |
| library Android Host 单元测试 | 5/5 |
| library iOS Simulator Arm64 单元测试 | 5/5 |
| 两个 Android 应用 Debug 打包 | 通过 |
| Demo JVM、JS、Wasm、iOS Simulator 编译 | 通过 |
| library iOS Arm64 编译 | 通过 |
| 完整 Xcode iOS 模拟器应用构建及启动 | 通过 |
| 华为 GOA-AL80U / Android 12 仪器测试 | 5/5，12.592 秒 |
| Android 16 / API 36 arm64 模拟器仪器测试 | 5/5，10.698 秒 |
| 华为与 API 36 真实输入/Sheet/Dialog 操作 | 各 22 项断言通过 |
| Web 开发包升级前与升级后 | 各 23 页及关键操作通过，无未捕获异常 |
| Web 生产优化包 | 23 页及相同关键操作通过 |
| iOS 27 / iPhone 18 Pro XCTest UI | 输入、Sheet 720P 选择确认、输入弹窗填写确认通过，1 场景 / 26.344 秒 |
| 两个 Android 应用 lintDebug | 通过；非阻塞警告保留 |
| Dokka HTML、Android POM、KMP metadata | 生成成功，POM Compose 依赖为 1.12.2 |
| CI YAML、Pages 输入目录 | 解析与产物存在性检查通过，未运行远程部署 |

跨平台主构建共 205 个任务通过。CI 同款 `:composeApp:wasmJsBrowserDistribution :DLJetpackComposeUtil:dokkaGenerateHtml` 在本地成功。Gradle Wrapper 执行两轮生成，脚本/JAR 与原版相同，仅发行地址与 SHA-256 有实质变化：`21bd5da2de49b642904077f38c0efdc7778e92b3ed0889392dca61b97e389607`。

## UI 效果

Web 固定 1200×900、同一隔离 Chromium 比较 30 组截图；每像素 RGB 任一通道差异大于 16 计为显著变化。28 组完全一致，Button 与 Switch 各有 30 / 6 个显著变化像素（0.0028% / 0.0006%），来自持续加载动画。首页、表单、输入弹窗、确认弹窗、单选 Sheet、长列表 Sheet 等未发现布局回退。

真实交互覆盖：输入文本保留；键盘返回先收起 IME；单选选择并确认；普通 Sheet 系统返回、遮罩关闭后底层页面保留；连续开关无残留遮罩；长 Sheet 顶部留白；输入弹窗空值报错与有效号码提交。已人工查看 Web Sheet、华为与 API36 输入弹窗/键盘、iOS 首页及验收截图，主要控件与确认按钮可见。

iOS 自动化前几轮因 Compose 的 TextView 类型、滚动后的可访问节点命中位置与首次点击未导航而失败；最终从更新后的父按钮重新定位并确认页面切换，通过真实交互，未为通过测试修改 iOS 生产代码。Android 16 原 PhotoManager 失败则是实际依赖缺陷，已由上述代码迁移修复，不能混为测试误报。

## 证据与环境边界

证据目录：`output/playwright/dependency-upgrade-1103/`（Git 忽略），包含 before/after/production 截图、`visual-comparison.json`、Android 断言 JSON、两设备 5 项测试日志、Web 23 页结果、iOS XCTest 截图/日志和可复查脚本。临时 Xcode UI 工程与完整结果包在 `/tmp/dlcompose-ios-ui/`，最终成功结果为 `result8.xcresult`。

华为安装时临时 applicationId 覆盖未生效，实际通过 `adb install -r` 更新了原 `com.d10ng.compose.demo`，没有清除其数据；已即时告知用户。保留更新后的 Demo，清理本次测试 APK、截图输出与自有测试数据。Android 16 使用本任务临时 AVD，iOS 设备原为关闭；浏览器使用 Playwright 自带 Chrome for Testing，未控制日常 Chrome。

用户任务前的 `kotlin-js-store/yarn.lock` 修改逐字节保留；其变更不是本次升级产生。没有执行全量供应链漏洞扫描、桌面原生 UI 验收或所有设备/字体/横竖屏穷举。Web 控制台仍有 Skiko WebGL 提示及既有 Wasm memory 弃用提示，交互无未捕获异常。构建保留 Gradle 10 不兼容的弃用警告；本轮不升级 Gradle 10。

## 官方依据

- [Compose 1.12.2 修复和配套表](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.2)
- [Gradle 9.8.2](https://docs.gradle.org/9.8.2/release-notes.html)
- [Kotlin 兼容表](https://kotlinlang.org/docs/multiplatform/multiplatform-compatibility-guide.html)
- [serialization 1.12.0](https://github.com/Kotlin/kotlinx.serialization/releases/tag/v1.12.0)
- [DLCommonUtil 1.x 迁移](https://github.com/D10NGYANG/DLCommonUtil/releases/tag/1.0.0)
- [版本检查插件 0.67.0](https://plugins.gradle.org/plugin/com.github.ben-manes.versions/0.67.0)
- [Google Play target API 要求](https://developer.android.com/google/play/requirements/target-sdk)

## 3.4.1 发布验证

正式切版后，七个 Maven 模块的本地发布与 JVM/Android Host 测试成功，共 133 个任务。196 份制品/仓库元数据摘要与 24 个模块制品链接校验通过；独立 JVM 消费工程解析 53 个制品，使用真实 AGP 的独立 Android 工程成功解析根坐标到 3.4.1 Android AAR。初始手工 Android 属性配置缺少 Kotlin 兼容规则，以及按外部文件名断言的验证错误已修正，没有改动发布制品。

Nexus releases 发布成功，42 份远程 SHA-256 与本地制品一致。GitHub Maven 仓库以独立制品提交交付，源仓库以 3.4.1 标签和同名 GitHub Release 提供源码及升级说明。
