# 项目依赖检查（2026-10-08）

## 范围与结论

检查版本目录、根/三个模块构建脚本、Gradle Wrapper、GitHub Pages 工作流，以及 Maven Central、Google Maven、自有 Maven 仓库元数据和官方发布说明。未修改依赖或业务代码。CodeGraph 已重新索引并用于源码探索。

最小处理方案：优先解决 Gradle 安全补丁发布状态，并将 DLAppUtil 升到 2.7.1；Compose 整组升级作为独立变更。无需为了追新同时迁移 AGP 9、Kotlin 和所有工具。

## 优先处理

### Gradle 8.14.3：安全更新优先，但修复版可用性存在冲突

2026-10-07 官方发布三条高危公告，受影响范围均包含 8.14.3，标注开源修复版本为 8.14.6 / 9.8.1：

- [GHSA-j5m7-59rp-24f5](https://github.com/gradle/gradle/security/advisories/GHSA-j5m7-59rp-24f5)：仓库 TLS 连接失败后仍向后续仓库查找，可能接受攻击者提供的制品。需要攻击者控制后续仓库等条件。
- [GHSA-xwqc-3h47-hg64](https://github.com/gradle/gradle/security/advisories/GHSA-xwqc-3h47-hg64)：客户端/daemon 在认证前反序列化不可信对象；共享构建主机风险更高，不是公网可直接利用的接口。
- [GHSA-mvvg-497x-hmj8](https://github.com/gradle/gradle/security/advisories/GHSA-mvvg-497x-hmj8)：worker/daemon 本地通信的认证与反序列化问题，需要同机攻击者及连接竞争条件。

**核验冲突：** 本次读取 [Gradle 版本服务](https://services.gradle.org/versions/all)，8.14 分支仅列到 8.14.5；8.14.6 的官方发行包 SHA-256 地址及 GitHub release API 均返回 404。因此不能声称 8.14.6 已可下载，也不应直接把 Wrapper 改为不可解析版本。先核实官方发布状态，发布可用后优先采用 8.14.6。8.14.5 可作为已发布补丁候选，但不能宣称解决上述三项新公告。不要为此直接跳 Gradle 9.8.1：它超出当前 Kotlin 2.3.21 的官方完整支持上限 9.3.0。

暂不能升级时，按公告限制构建主机上的不可信本地用户/进程；仓库问题可通过严格仓库限制或依赖校验缓解。当前 settings 中的 includeGroup 过滤并非 exclusiveContent，Maven Central/JitPack 仍可能成为回退来源；仓库没有 gradle/verification-metadata.xml。

来源：[Kotlin/Gradle/AGP 兼容表](https://kotlinlang.org/docs/gradle-configure-project.html)、[Gradle 8.14.5 发布说明](https://docs.gradle.org/8.14.5/release-notes.html)。

### targetSdk 34：仅 Google Play 发布场景属于硬要求

androidApp 与 composeApp 的 Android 应用均使用 targetSdk 34，compileSdk 已为 36。自 2026-08-31 起，普通手机应用的新提交/更新需 targetSdk 36（延期以 Play Console 批准为准）。仅用于本地演示或发布本库 Maven 制品，不构成必须升级的理由。升级需检查系统栏、返回手势和窗口行为。

来源：[Google Play Target API 要求](https://support.google.com/googleplay/android-developer/answer/11926878)。

## 值得升级的依赖

下表为已核验发布的候选，不表示已经通过本项目编译或运行验证。

| 项目 | 当前 | 候选 | 建议 |
|---|---|---|---|
| DLAppUtil | 2.7.0 | 2.7.1 | 优先。修复 Activity 销毁后 launcherMap 残留引用，无 API 迁移要求；项目图片预览 Demo 使用 PhotoManager.pick，应回归旋转/销毁重建后的选图 |
| Compose 插件与 Runtime/UI/Foundation/Resources | 1.11.1 | 1.12.1 | 值得整组升级，包含 iOS 输入/返回手势、Web 滚动/资源与文本选择崩溃修复 |
| JetBrains Material3 | 1.11.0-alpha07 | 1.12.0-alpha03 | 与 Compose 1.12.1 官方组件表配套；仍为 alpha，需要重点回归 Sheet、Scrim、Insets、关闭策略 |
| JetBrains Lifecycle | 2.10.0 | 2.11.0 | 随 Compose 整组评估 |
| AndroidX Lifecycle | 2.10.0 | 2.11.0 | Android Demo 同步评估 |
| JetBrains Navigation3 | 1.1.1 | 1.1.2 | 已确认 Maven Central 发布，上一份 8 月记录的“尚未发布”已过时；随导航回归更新 |
| Android Compose BOM | 2026.06.00 | 2026.09.00 | 配合整组 Compose 更新；只管理 AndroidX 坐标，不管理 JetBrains 坐标；需检查最终解析版本 |
| Compose Hot Reload | 1.1.1 | 1.2.0 | 开发工具更新，Compose 1.12 官方配套版本，非运行时紧急项 |
| AndroidX Core KTX | 1.18.0 | 1.19.1 | 可更新但当前收益较小；1.19 将 KTX API 合入 core，旧坐标作为兼容制品保留 |
| Kotlin/Compose compiler/serialization plugin | 2.3.21 | 2.4.20 | 可规划，不是安全更新；编译插件保持同版本，并检查 Xcode 与库消费者的 Kotlin 兼容性 |
| DLCommonUtil | 0.8.1 | 1.0.2 | 单独评估。1.x 更严格处理非法范围，部分历史静默行为变为异常；1.0.2 Apple 编码修复不直接惠及本项目 androidMain 的依赖范围 |

Compose 升级收益来源：[1.12.0](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0)、[1.12.1 与官方组件版本表](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.1)。

自有库来源：[DLAppUtil 2.7.1](https://github.com/D10NGYANG/DLAppUtil/releases/tag/2.7.1)、[DLCommonUtil 1.0.0 行为变化](https://github.com/D10NGYANG/DLCommonUtil/releases/tag/1.0.0)、[DLCommonUtil 1.0.2](https://github.com/D10NGYANG/DLCommonUtil/releases/tag/1.0.2)。

其他来源：[Core 发布说明](https://developer.android.com/jetpack/androidx/releases/core)、[Kotlin 2.4.20](https://kotlinlang.org/docs/whatsnew2420.html)、[KMP 兼容表](https://kotlinlang.org/docs/multiplatform/multiplatform-compatibility-guide.html)。

## 不宜机械更新

- **AGP 8.11.2 → 9.4.1：不作为本次普通更新建议。** Google Maven 同时存在 8.13.2，可作为保持 AGP 8 的候选，但未发现必须因此升级的项目证据。当前 library 使用 KMP + com.android.library，composeApp 使用 KMP + com.android.application。AGP 9 的正式迁移需要 Android KMP library 插件、Android 应用模块拆分及发布配置复核。已有 androidApp 是另一套 Demo，不能未经确认直接替代 composeApp 的 Android 入口。[官方迁移说明](https://developer.android.com/kotlin/multiplatform/plugin)
- **Material3 不要自动降为“最新稳定 1.9.0”。** 当前源码已经使用新 BottomSheet/Scrim API；不能假定旧稳定线能无损替代。根项目 dependencyUpdates 会拒绝所有非稳定候选，因此它无法完整报告当前 alpha 依赖的后续更新，需要对 Material3 单独查版本与配套表。
- **material-icons-extended 1.7.3 不要按 Compose 主版本升级。** 本次 Maven 元数据仍以 1.7.3 为最新稳定发布；没有可直接替换的 1.12.1 同名版本。

## 暂无稳定版更新的直接依赖

本次仓库元数据核对：Activity Compose 1.13.0、JetBrains Navigation Event 1.1.0、datetime 0.8.0、serialization-json 1.11.0、Dokka 2.2.0、JUnit 4.13.2、AndroidX test-junit 1.3.0、Espresso 3.7.0、Foojay resolver 1.0.0。

JUnit 指的是当前 junit:junit 坐标；迁移 JUnit Jupiter 不是同坐标版本更新。serialization 1.12.0-RC、Dokka 2.3.0-Beta 等预发布版不列入普通稳定升级。

## 构建与 CI 工具

- ben-manes versions 插件：0.54.0 → 官方插件仓库最新 0.65.0，低优先级候选，需核对其 Gradle 要求后再更新。来源：[插件元数据](https://plugins.gradle.org/m2/com/github/ben-manes/versions/com.github.ben-manes.versions.gradle.plugin/maven-metadata.xml)。
- Pages 工作流使用 checkout@v4、setup-java@v3、upload-pages-artifact@v3、deploy-pages@v4；官方最新发布分别为 [checkout v7.0.1](https://github.com/actions/checkout/releases/tag/v7.0.1)、[setup-java v6.0.1](https://github.com/actions/setup-java/releases/tag/v6.0.1)、[upload-pages-artifact v5.0.0](https://github.com/actions/upload-pages-artifact/releases/tag/v5.0.0)、[deploy-pages v5.0.1](https://github.com/actions/deploy-pages/releases/tag/v5.0.1)。作为独立 CI 维护批次评估，检查 runner/Node 要求后更新，不据此断言当前工作流必然失败。
- CI 配置 JDK 17，而 daemon criteria 指定 Amazon JDK 21；并非必然不兼容，但实际可能额外下载 JDK，后续可统一工具链。

## 核验方法与限制

直接读取 Google Maven、Maven Central、自有仓库的 maven-metadata.xml，核对稳定与预发布版本；GitHub release API 和官网发布说明用于判断升级价值。示例：[Compose UI 元数据](https://repo.maven.apache.org/maven2/org/jetbrains/compose/ui/ui/maven-metadata.xml)、[Material3 元数据](https://repo.maven.apache.org/maven2/org/jetbrains/compose/material3/material3/maven-metadata.xml)、[AGP 元数据](https://dl.google.com/dl/android/maven2/com/android/tools/build/gradle/maven-metadata.xml)、[自有 DLAppUtil 元数据](https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository/com/github/D10NGYANG/DLAppUtil/maven-metadata.xml)。

现有 dependencyUpdates 任务执行成功（4 分 24 秒），原始 JSON 输出为 `/private/tmp/dlcompose-dependency-audit/report.json`。但任务成功不等于全部条目成功：部分 KMP 依赖与 Google testing platform 的最新版本判定失败；KMP 直接依赖已通过上述 Maven 元数据和官方发布表补查。任务还报告现有弃用功能不兼容 Gradle 9.0，进一步支持不直接跳 Gradle 9 的建议。内部 UTP/Skiko 等版本应随 AGP/Compose 配套调整，不单独按扫描结果强制覆盖。

本报告不是完整传递依赖漏洞扫描，未审计 Yarn 锁文件内全部工具依赖，未执行新版本的全平台构建或 UI 回归；不能把“未发现其他必须更新项”解释为项目没有其他漏洞。
