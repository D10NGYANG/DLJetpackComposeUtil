# 项目依赖检查（2026-10-10）

检查根构建脚本、版本目录、三个模块、Gradle Wrapper、iOS 工程与 Pages 工作流，并实时核对 Google Maven、Maven Central、自有 Maven 仓库、插件仓库和官方发布说明。未修改依赖版本或业务代码。

## 结论与最小更新方案

优先处理 **Gradle 8.14.3 → 8.14.6**，其次 **DLAppUtil 2.7.0 → 2.7.1**。Compose 整组升级单独实施；AGP 9 和 Kotlin 大版本线迁移不与安全补丁捆绑。

相较 10 月 8 日旧报告，本次确认两项变化：Gradle 8.14.6 已发布且官方 SHA-256 可获取；Kotlin 最新稳定候选已为 2.4.21。

## 必须优先处理的安全项

当前 Wrapper 为 Gradle 8.14.3。官方 [8.14.6 发布说明](https://docs.gradle.org/8.14.6/release-notes.html)确认该补丁修复三项高危漏洞，当前版本均落在受影响范围内：

- [GHSA-j5m7-59rp-24f5](https://github.com/gradle/gradle/security/advisories/GHSA-j5m7-59rp-24f5)：TLS 建连失败后继续查询后续仓库，可能解析到攻击者控制的制品；需要攻击者控制后续仓库等条件。
- [GHSA-xwqc-3h47-hg64](https://github.com/gradle/gradle/security/advisories/GHSA-xwqc-3h47-hg64)：client/daemon 认证前反序列化风险，同机不可信用户或进程是重要前提。
- [GHSA-mvvg-497x-hmj8](https://github.com/gradle/gradle/security/advisories/GHSA-mvvg-497x-hmj8)：worker/daemon 通信认证与反序列化风险，需要同机访问和连接竞争条件。

这是构建环境风险，不意味着已发布 UI 库存在相同运行时漏洞，也没有证据证明项目已经遭到利用。

本次 [Gradle 版本服务](https://services.gradle.org/versions/all)、[官方发行记录](https://github.com/gradle/gradle-distributions/releases/tag/v8.14.6)及[官方 bin 包校验和](https://services.gradle.org/distributions/gradle-8.14.6-bin.zip.sha256)相互一致。发行记录时间为 2026-10-08 10:11:57 UTC，bin SHA-256 为：

```text
7988ed071b2a07900e2ec715fca15c6ab72bce6db433af301fa7fa7e9407bd24
```

建议更新完整 Wrapper 文件并配置 `distributionSha256Sum`；当前使用腾讯镜像且没有固定校验和，不能仅替换 URL 中版本号并假设镜像已同步。本次未下载完整发行包，也未检查镜像同步状态。

[Kotlin 官方兼容表](https://kotlinlang.org/docs/gradle-configure-project.html)列明 Kotlin 2.3.20–2.3.21 完整支持 Gradle 7.6.3–9.3.0，8.14.6 在范围内；没有必要为此跳到超出该范围的 9.8.1。文档范围兼容不代替实际构建验证。

## 值得更新的直接依赖

| 依赖 | 当前 | 建议候选 | 收益与边界 |
|---|---|---|---|
| DLAppUtil | 2.7.0 | 2.7.1 | 优先补丁：清理 Activity 销毁后的 launcherMap 引用；示例 PhotoManager 选图路径相关 |
| Compose 插件、Runtime/UI/Foundation/Resources | 1.11.1 | 1.12.1 | 值得整组更新：文本选择崩溃、iOS 输入及无障碍、Web 键盘等修复 |
| JetBrains Material3 | 1.11.0-alpha07 | 1.12.0-alpha03 | Compose 1.12.1 官方配套，仍为 alpha；重点回归同树 Sheet、Scrim、Insets、返回和关闭动画 |
| JetBrains Lifecycle | 2.10.0 | 2.11.0 | 随 Compose 配套评估 |
| JetBrains Navigation3 | 1.1.1 | 1.1.2 | 官方配套且已发布；验证页面栈及返回行为 |
| AndroidX Lifecycle | 2.10.0 | 2.11.0 | Android 示例同步评估 |
| Android Compose BOM | 2026.06.00 | 2026.09.00 | Android 示例候选；核对最终解析图，不能用其管理 JetBrains 坐标 |
| Compose Hot Reload | 1.1.1 | 1.2.0 | Compose 1.12 开发工具配套，非运行时紧急项 |
| AndroidX Core KTX | 1.18.0 | 1.19.1 | 已发布的维护候选，本次未确认必须升级的项目故障 |
| Kotlin 及 Compose compiler/serialization 插件 | 2.3.21 | 2.4.21 | 可规划；三类插件同版本，评估 Native/Xcode 和库消费者兼容性 |
| DLCommonUtil | 0.8.1 | 1.0.2 | 单独评估：1.x 加强非法范围检查，可能由静默行为变为异常 |

来源：[DLAppUtil 2.7.1](https://github.com/D10NGYANG/DLAppUtil/releases/tag/2.7.1)、[Compose 1.12.0](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0)、[Compose 1.12.1 修复与配套表](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.1)、[Kotlin 2.4.21](https://github.com/JetBrains/kotlin/releases/tag/v2.4.21)、[DLCommonUtil 1.0.0 迁移说明](https://github.com/D10NGYANG/DLCommonUtil/releases/tag/1.0.0)、[DLCommonUtil 1.0.2](https://github.com/D10NGYANG/DLCommonUtil/releases/tag/1.0.2)。

DLAppUtil 官方说明无 API 迁移要求，但仍需回归 Activity 重建、退出重进后的选图。DLCommonUtil 1.0.2 的 Apple GBK 修复不直接惠及本项目声明在 androidMain 的依赖。

Compose 1.12 将 NativeCanvas/NativePaint 等旧接口弃用等级提升为 ERROR。本次在 library/src 与 composeApp/src 搜索这些接口及 SwingPanel/ComposePanel 未命中直接使用，降低了该项直接迁移风险，但不覆盖传递依赖。项目 Cell 使用 SelectionContainer，Input 使用 BasicTextField，因此文本相关修复具有实际相关性，尚未复现旧版缺陷。

## 有条件的硬要求及不宜机械更新项

- **targetSdk 34 → 36**：如果 Android 示例作为普通手机应用向 Google Play 新提交或更新，2026-08-31 起需要 API 36，延期以 Console 批准为准；仅本地演示或发布 Maven 库不构成强制要求。[Google Play 要求](https://support.google.com/googleplay/android-developer/answer/11926878)
- **AGP 8.11.2 → 9.4.1 不作为直接升级建议**：9.4.1 已出现在 Google Maven，但现有 library 使用 KMP + com.android.library，composeApp 使用 KMP + com.android.application。正式迁移需评估 Android KMP 插件、应用模块拆分和发布配置；已有 androidApp 是另一套示例，不能直接当作等价入口。[Android KMP 插件说明](https://developer.android.com/kotlin/multiplatform/plugin)
- **Material3 不能自动降到稳定版 1.9.0**：源码使用更新的实验性 BottomSheet/Scrim。1.13 alpha 虽已发布，也不是本次 Compose 1.12.1 的配套建议。
- **material-icons-extended 保持 1.7.3**：该坐标没有与 Compose 主版本一致的 1.12.1 稳定版。
- 根项目 `dependencyUpdates` 拒绝所有非稳定候选，无法完整呈现现有 Material3 alpha 的配套更新。本次对该坐标单独核对元数据与发布表。

## 已核验暂无更新稳定版的直接坐标

Activity Compose 1.13.0、JetBrains Navigation Event 1.1.0、material-icons-extended 1.7.3、datetime 0.8.0、serialization-json 1.11.0、Dokka 2.2.0、JUnit 4.13.2、AndroidX test-junit 1.3.0、Espresso 3.7.0、Foojay resolver 1.0.0。

此处仅指同坐标稳定版本；JUnit Jupiter 等属于迁移而非同坐标更新。serialization 1.12.0-RC、Dokka 2.3.0-Beta 等不计入普通稳定升级。

## CI 与构建辅助工具

- ben-manes versions 插件 0.54.0 → 0.65.0：低优先级候选，升级前检查自身 Gradle 要求。[插件元数据](https://plugins.gradle.org/m2/com/github/ben-manes/versions/com.github.ben-manes.versions.gradle.plugin/maven-metadata.xml)
- Pages 工作流可独立评估 [checkout v4 → v7.0.1](https://github.com/actions/checkout/releases/tag/v7.0.1)、[setup-java v3 → v6.0.1](https://github.com/actions/setup-java/releases/tag/v6.0.1)、[upload-pages-artifact v3 → v5.0.0](https://github.com/actions/upload-pages-artifact/releases/tag/v5.0.0)、[deploy-pages v4 → v5.0.1](https://github.com/actions/deploy-pages/releases/tag/v5.0.1)。这些是已发布候选，不代表已验证跨大版本迁移或旧版必然失效。
- CI 显式安装 JDK17，daemon criteria 要求 Amazon JDK21，而库编译 toolchain 为11：三者用途不同，不是必然冲突；可在 CI 维护时减少额外工具链下载。

## 核验与限制

本次实时读取23组仓库元数据，包括 [Compose UI](https://repo.maven.apache.org/maven2/org/jetbrains/compose/ui/ui/maven-metadata.xml)、[Material3](https://repo.maven.apache.org/maven2/org/jetbrains/compose/material3/material3/maven-metadata.xml)、[Kotlin](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-gradle-plugin/maven-metadata.xml)、[AGP](https://dl.google.com/dl/android/maven2/com/android/tools/build/gradle/maven-metadata.xml)、[BOM](https://dl.google.com/dl/android/maven2/androidx/compose/compose-bom/maven-metadata.xml)、[DLAppUtil](https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository/com/github/D10NGYANG/DLAppUtil/maven-metadata.xml)。初次Python证书链校验失败后改用系统curl，保留TLS校验，未跳过证书检查。

AOCI 首次索引已补齐175条，Verify/Check/Guide确认aligned且complete=true。Overview两块内容已完整交付并确认，认知抽查6/10为partial；未将其描述为完整认知校验通过，依赖结论依据实际构建文件和外部一手来源。

未修改依赖、执行升级构建或UI回归；未重复运行旧报告的dependencyUpdates任务，也未执行完整传递依赖及Yarn漏洞扫描。报告是更新优先级评估，不能作为全项目无漏洞证明。后续安全补丁更新先验证Gradle配置及现有单元测试，Compose更新再执行Android/JVM/iOS/JS/Wasm编译和弹层输入回归。
