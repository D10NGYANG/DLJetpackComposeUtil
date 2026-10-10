# 依赖升级与 UI 验证（2026-10-10）

## 实施结果

已完成 Gradle 安全更新、DLAppUtil 修复版及 Compose 配套依赖升级。源码编译、Android 打包、三平台单元测试、iOS 模拟器整包启动、Web 页面交互和截图对比通过。本报告记录发布前验收；正式切版为3.4.0，发布与升级说明见[3.4.0](../releases/3.4.0.md)。

| 项目 | 原版本 | 最终版本 |
|---|---|---|
| Gradle | 8.14.3 | 9.8.1，完整 Wrapper 与官方 SHA-256 |
| AGP | 8.11.2 | 9.1.1 |
| Kotlin / Compose compiler / serialization 插件 | 2.3.21 | 2.4.21 |
| Android compileSdk | 36 | 37 |
| DLAppUtil | 2.7.0 | 2.7.1 |
| Compose 插件、Runtime、UI、Foundation、Resources | 1.11.1 | 1.12.1 |
| JetBrains Material3 | 1.11.0-alpha07 | 1.12.0-alpha03 |
| JetBrains / AndroidX Lifecycle | 2.10.0 | 2.11.0 |
| JetBrains Navigation3 | 1.1.1 | 1.1.2 |
| Android Compose BOM | 2026.06.00 | 2026.09.00 |
| Compose Hot Reload | 1.1.1 | 1.2.0 |

保留 minSdk 26、targetSdk 34、JVM 库 toolchain 11、原应用 ID及其余依赖。验收阶段暂用库版本3.3.5，随后切版为3.4.0；历史3.3.5制品不覆盖。

## 为什么扩大了工具链迁移范围

先将 Gradle 升至8.14.6并更新所选依赖后，Android的 `checkDebugAarMetadata` 明确失败：Compose UI/Foundation 1.12.1、Material3 Android 1.5.0-alpha22、Lifecycle 2.11.0要求 compileSdk 37和AGP至少9.1.0。这项硬约束在此前仅查版本元数据的审计中未被验证，不能沿用“直接修改版本即可”的预期。

[AGP 9.1.1官方兼容表](https://developer.android.com/build/releases/agp-9-1-0-release-notes)支持API37，最低Gradle为9.3.1。为同时满足此前三项Gradle安全修复，最终使用开源修复版9.8.1，而没有保留仍受影响的9.3.1。[安全公告](https://github.com/gradle/gradle/security/advisories/GHSA-j5m7-59rp-24f5)

Kotlin2.4.21用于配套AGP9.1；[Kotlin兼容表](https://kotlinlang.org/docs/gradle-configure-project.html)列出的Gradle完整测试上限仍是9.7.0。因此9.8.1组合有本项目实测证据，但不宣称处于官方完整测试矩阵内。

Wrapper使用官方发行地址并固定校验和：

```text
dce76f55f8e251a3a1f130eb120f30b3d271de2b76c9b0729d316b5a1b6dc01f
```

来源：[Gradle9.8.1官方校验和](https://services.gradle.org/distributions/gradle-9.8.1-bin.zip.sha256)。

## API 与工程适配

1. **Sheet状态工厂迁移。** `rememberModalBottomSheetState(skipPartiallyExpanded=true)`改为`rememberBottomSheetState(initialValue=Hidden, enabledValues=setOf(Hidden, Expanded))`，采用新版API并保留不半展开的行为。Builder公开参数、关闭策略、遮罩、Insets、顶部限高及等待退出动画后移除的逻辑保持原契约。
2. **KMP Android插件迁移。** `library`与`composeApp`改用`com.android.kotlin.multiplatform.library`，Android配置移入`kotlin.android`；启用资源处理，库显式启用Host测试。纯Android应用移除与AGP内置Kotlin冲突的`kotlin.android`插件。
3. **必需的Android应用拆分。** 新增`composeAndroidApp`承载原`composeApp`的Manifest、MainActivity、图标和assets。19个迁移文件与Git HEAD的原文件逐字节一致；应用ID仍为`com.d10ng.compose.demo`。共享Compose代码及iOS框架保持在`composeApp`，原`androidApp`主题Demo保持独立。[官方迁移说明](https://developer.android.com/kotlin/multiplatform/plugin)
4. **任务入口变化。** 跨平台Demo的Android构建改为`:composeAndroidApp:assembleDebug`；库Android单元测试改为`:DLJetpackComposeUtil:testAndroidHostTest`。
5. **构建DSL适配。** Javadoc Jar改用`tasks.register<Jar>`，移除Gradle9.6起弃用的委托注册写法。调试UI tooling位于Android应用模块，未作为库正式运行依赖引入。

本库公开组件函数和Builder签名没有修改。下游采用新源码/新制品时仍需满足新的Android构建工具要求；“组件调用代码不变”不等于“构建环境无需升级”。版本切换前生成的Android POM及KMP根元数据指向`com.github.D10NGYANG:DLJetpackComposeUtil-android:3.3.5`；正式切版后仅版本提升为3.4.0，Android坐标保持兼容且变为单变体。

## 构建和测试证据

| 验证 | 结果 |
|---|---|
| 升级前Wasm开发包和JVM测试基线 | 成功 |
| library JVM单元测试 | 5/5，无失败、错误或跳过 |
| library Android Host单元测试 | 5/5，无失败、错误或跳过 |
| library iOS Simulator Arm64单元测试 | 5/5，无失败、错误或跳过 |
| Android 12真机仪器测试（追加验证） | 4/4，Activity重建、选图结果/预览、真实选图器取消、强制Sheet |
| `androidApp`与`composeAndroidApp` Debug APK | 均成功 |
| Demo JVM、JS、Wasm、iOS Simulator Arm64编译 | 均成功 |
| library iOS Arm64编译 | 成功 |
| Wasm开发可执行包 | 成功 |
| Xcode iOS模拟器完整应用构建 | BUILD SUCCEEDED |
| iPhone18Pro / iOS27模拟器启动 | 成功，已查看首页真实截图 |
| Dokka HTML与KMP发布元数据生成 | 成功 |

主构建189个任务成功，补充检查54个任务成功。首次下载发生Yarn和Maven TLS握手中断，保持证书校验重试后完成，未关闭依赖校验或修改全局网络设置。

核心命令：

```shell
./gradlew :DLJetpackComposeUtil:jvmTest :DLJetpackComposeUtil:testAndroidHostTest \
  :androidApp:assembleDebug :composeAndroidApp:assembleDebug \
  :composeApp:compileKotlinJvm :composeApp:compileKotlinIosSimulatorArm64 \
  :DLJetpackComposeUtil:compileKotlinIosArm64 :composeApp:compileKotlinJs \
  :composeApp:wasmJsBrowserDevelopmentExecutableDistribution \
  :DLJetpackComposeUtil:generatePomFileForAndroidPublication

./gradlew :DLJetpackComposeUtil:iosSimulatorArm64Test \
  :DLJetpackComposeUtil:dokkaGenerateHtml \
  :DLJetpackComposeUtil:generateMetadataFileForKotlinMultiplatformPublication
```

## UI 回归

使用独立Chrome for Testing headless shell，通过Playwright操作本地升级前/后Wasm Demo；未使用日常Chrome。Web验证覆盖23个组件页面及首页，另检查390×844窄屏首页与按钮布局。

- 输入框实际键入并验证保留`UI regression 123`。
- 单选Sheet选择720P并确认关闭；日期Sheet点击遮罩关闭。
- Sheet连续打开/取消5轮，无残留遮罩；下拉拖动可关闭；长选项Sheet保持顶部留白且能从遮罩关闭。
- 对话框取消、Popover选择及页面导航可用。
- 其余17个组件页冒烟检查通过，无未捕获页面异常。

11组同尺寸截图比较（每个RGB通道差异超过16计为显著差异）：

| 截图 | 结果 |
|---|---|
| 首页、输入框、确认弹窗、Sheet页面、单选Sheet、日期Sheet、窄屏首页 | 逐像素完全一致 |
| 按钮、开关 | 分别12和11个显著差异像素，约0.001%；位于持续旋转的加载指示器 |
| 窄屏按钮、Popover | 无显著差异，仅加载/按压反馈的少量低幅差异 |

已人工查看升级后Sheet、输入框、窄屏按钮和iOS首页，未发现本次升级引入的可见布局回退。截图相同是所覆盖状态的证据，不代表所有设备和交互状态都已穷尽。

本地证据位于被Git忽略的`output/playwright/dependency-upgrade/`：`before-*.png`、`after-*.png`、`ios-after-home.png`、`visual-comparison.json`及回归脚本。命令日志保留在`/tmp/dlcompose-*.log`，属于本次机器的临时证据。

## Android 真机追加验证

设备为HUAWEI GOA-AL80U，Android12/API31，1200×2652。此前黑屏截图问题在手机解锁后不再出现，本次截图及UIAutomator均可正常读取。

手机原有Demo与本次源码不是同一界面版本，因此使用临时Gradle init脚本将测试构建包名设为`com.d10ng.compose.demo.validation`，不覆盖原`com.d10ng.compose.demo`。测试后移除验证APK、仪器APK及临时UI dump，恢复原Demo；正常构建产物重新生成默认包名。没有修改手机导航模式、字体、动画倍率或开发者全局设置。

新增`composeAndroidApp/src/androidTest/kotlin/com/d10ng/compose/demo/ActivityResultRegressionTest.kt`，最终设备运行结果为`OK (4 tests)`，耗时9.282秒：

1. `ActivityScenario.recreate()`后旧Activity从公开列表和私有`launcherMap`移除，新Activity成为栈顶且持有新launcher，直接验证DLAppUtil2.7.1的引用清理修复。
2. 创建并最终删除一张自有32×32绿色JPEG，通过Espresso Intents注入成功选图结果；首次启动及连续两次Activity重建后，`PhotoManager.pick()`均返回可解码文件，尺寸与像素正确。进一步通过真实屏幕像素确认`ImagePreview`已绘制该图片，单击触发关闭回调。该测试不选择个人照片；MediaStore夹具仅在API29+运行。
3. 重建Activity后实际启动系统选图器，检测外部窗口，发出系统返回，验证`PhotoManager.pick()`返回null。此项不使用Intent结果注入。
4. 强制Sheet在系统返回和原生遮罩点击后仍可见，`dismiss()`主动关闭后回到原页面。

补充ADB触摸、UIAutomator断言及人工截图检查：

- 首页、按钮、输入框、单选Sheet、长列表Sheet、日期滚轮和输入弹窗显示正常。
- 输入框可输入测试文本；键盘返回先收起IME，保留输入页。
- 普通Sheet通过返回键、完整边缘返回、下拉拖动或遮罩关闭时，不会额外弹出底层页面；连续开关5轮无残留遮罩。
- 输入弹窗空值确认显示校验错误，输入合成手机号后确认可关闭；IME展开时弹窗和按钮没有被键盘遮挡。

初轮测试中的两处失败属于测试驱动问题，修正后复验通过：Compose虚拟节点需遍历无障碍树而非依赖`findAccessibilityNodeInfosByText`；图片预览同时支持双击，单击回调需等待双击判定窗口结束。没有为通过测试修改生产组件逻辑。

设备证据存于`output/playwright/dependency-upgrade/android-device/`，含页面截图/XML、`photo-preview.png`、`instrumentation-result.log`、原生交互日志及独立包名init脚本。最终测试通过命令为：

```shell
adb -s GDM9K23410002823 shell am instrument -w \
  com.d10ng.compose.demo.validation.test/androidx.test.runner.AndroidJUnitRunner
```

API31不支持Android13+预测性返回协议。本机短边缘侧滑也会触发完整返回，不能将其解释为预测性返回取消测试。成功选图回调使用受控测试图片注入，系统图库中手动选中照片的交互未执行；真实选择器打开和取消已经实测。

## 明确的限制与遗留提示

- Web控制台有一条非致命`wasmExports.memory`弃用提示，定位至Compose Resources生成的`copyArrayBufferToWasmMemory`；Kotlin2.4.21通过兼容Proxy记录`console.error`，未抛出页面异常。未修改生成文件、关闭日志或单独覆盖Compose传递依赖。[Kotlin迁移说明入口](https://kotl.in/vr3szr)
- 仍有第三方构建插件的Gradle10/11弃用提示和Wasm配置期解析性能提示；本次构建已成功，不把这些提示误报为组件API编译失败。
- Android12实机视觉、IME、传统返回/侧滑和Activity重建结果链路已补测。Android13+预测性返回动画与取消仍需相应版本设备，不能由本机结果替代。
- iOS已完成整包启动截图与策略单元测试，没有完成全部页面手势和键盘的原生交互回归；Web结果不能代替这些平台专项。
- Material3仍为alpha；本报告不构成完整传递依赖漏洞扫描。发布使用新的3.4.0版本，不覆盖历史3.3.5制品。

## 3.4.0 发布前检查

版本切换后，全部平台本地Maven制品生成、JVM/Android Host测试及Web生产打包成功，共167个任务。检查7个模块的制品存在性、168份制品校验和、28份Maven元数据校验和及Gradle元数据中的制品链接，均通过。

独立消费配置从发布目录解析Android AAR（89个运行时制品）和JVM JAR（53个运行时制品），没有用库项目自身的构建输出替代消费验证。生产压缩Wasm包重复执行输入、Sheet、Dialog、Popover及390px布局等7组交互检查，通过。

日志保留在`/tmp/dlcompose-release-340-build.log`、`/tmp/dlcompose-release-consumers.log`和`/tmp/dlcompose-release-ui-result.log`；生产包截图前缀为`release-340-`，位于前述本地证据目录。
