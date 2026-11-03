# 项目依赖检查（2026-11-03）

## 结论

本次未发现已核实、要求本库立即升级的安全项。最值得先更新的是 Compose Multiplatform 1.12.1 → 1.12.2；Gradle 9.8.1 → 9.8.2 可作为独立维护补丁。Google Play 发布条件下，两个 Android 示例的 targetSdk 34 必须提高到至少 36。未改动依赖、源码、工作流或用户已有的 yarn.lock 修改。

这不是完整漏洞扫描；结论基于直接依赖声明、官方版本元数据、发布说明和 Gradle 已知安全公告，不代表全部传递依赖均无漏洞。

## 建议更新

| 优先级 | 依赖 | 当前 → 候选 | 判断与影响 |
|---|---|---|---|
| 高 | Compose 插件、Runtime/UI/Foundation/Resources/Tooling | 1.12.1 → 1.12.2 | 修复 iOS TextField 因 Dialog 出现而失焦时崩溃、iOS Safari 27 输入焦点行为、非矩形阴影动画闪烁。覆盖 library 与 Demo；统一 compose 版本引用，回归输入框、弹窗和 Web 输入 |
| 中 | Gradle Wrapper | 9.8.1 → 9.8.2 | 修复虚拟平台版本对齐的误报警；安全修复已包含在当前 9.8.1，不能把此补丁误称新增紧急安全修复。更新时同步完整 Wrapper 与官方 SHA-256 |
| 中低 | kotlinx-serialization-json | 1.11.0 → 1.12.0 | 稳定更多 API、优化解析、限制 JSON 嵌套深度；反序列化异常统一包装为 JsonException，需关注异常处理变化。基于 Kotlin 2.4.10，项目已为 2.4.21。当前显式 JSON 读取是 AreaStore 的内置资源，未据此发现外部不可信 JSON 的紧急风险 |
| 中低 | JetBrains Navigation3 | 1.1.2 → 1.2.0 | 同坐标稳定版已在 Maven Central 发布。仅 Demo 显式使用；建议单独验证页面栈、状态恢复、返回与弹层竞争。版本发布本身不足以证明项目必须升级 |
| 低 | AndroidX Core KTX | 1.18.0 → 1.19.1 | Android 示例的维护候选。1.19 起 KTX API 已并入 core，core-ktx 保留兼容；无需为了升级同时重构全部引用。1.19.1 的 lint 依赖修复主要针对更新 AGP，当前收益有限 |
| 低 | ben-manes versions 插件 | 0.54.0 → 0.67.0 | 仅依赖检查工具；先核对新版兼容要求，再单独更新 |
| 单独评估 | DLCommonUtil | 0.8.1 → 1.0.2 | 跨 1.x 存在非法范围检查和异常语义变化。仅在 composeApp androidMain 显式依赖；1.0.2 Apple GBK 修复不直接惠及该使用范围，不建议与 Compose 补丁捆绑 |

来源：[Compose 1.12.2](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.2)、[Gradle 9.8.2](https://docs.gradle.org/9.8.2/release-notes.html)、[serialization 1.12.0](https://github.com/Kotlin/kotlinx.serialization/releases/tag/v1.12.0)、[JetBrains Navigation3 元数据](https://repo.maven.apache.org/maven2/org/jetbrains/androidx/navigation3/navigation3-ui/maven-metadata.xml)、[Core 发布说明](https://developer.android.com/jetpack/androidx/releases/core)、[versions 插件元数据](https://plugins.gradle.org/m2/com/github/ben-manes/versions/com.github.ben-manes.versions.gradle.plugin/maven-metadata.xml)、[DLCommonUtil 1.0.0 迁移说明](https://github.com/D10NGYANG/DLCommonUtil/releases/tag/1.0.0)、[DLCommonUtil 1.0.2](https://github.com/D10NGYANG/DLCommonUtil/releases/tag/1.0.2)。

## 有条件必须更新

`androidApp` 和 `composeAndroidApp` 均使用 targetSdk 34。自 2026-08-31 起，普通手机应用在 Google Play 新提交或更新需 targetSdk ≥ 36，延期截止为 2026-11-01。若仅本地 Demo 或发布 Maven UI 库，则不构成本任务的强制要求。提升 targetSdk 后需验证边到边布局、Insets、系统返回及权限行为，不能只改数字。

来源：[Google Play target API 要求](https://developer.android.com/google/play/requirements/target-sdk)。

## 保留及兼容性边界

- **AGP 9.1.1 暂保留**：Google Maven 已有稳定版 9.5.0，但 Kotlin 2.4.20–2.4.21 官方表中的 AGP 范围止于 9.3.1；不是自动升级建议。项目已经完成 AGP 9 的 KMP Android 插件与应用模块拆分，无需重复迁移。
- **Kotlin 2.4.21 保留**：当前稳定版；2.5.0-Beta2 不作为常规升级目标。
- **Gradle 兼容性风险仍在**：官方 Kotlin 表中的 Gradle 范围止于 9.7.0，现有 9.8.1 和候选 9.8.2 均超出完整测试范围。项目 10 月交付记录有 9.8.1 实测证据，本次未重新验证；不应为回到矩阵而降至受安全公告影响的旧 Gradle。
- **Material3 1.12.0-alpha03 保留**：Compose 1.12.2 官方组件表仍采用该版本。不能因为检查插件只接收稳定候选而降到 1.9.0，也不建议追 1.13 beta。
- **material-icons-extended 1.7.3 保留**：独立坐标，不跟随 Compose 主版本号。
- 不手动覆盖 Skiko、AGP UTP、JaCoCo、Kotlin build-tools 等插件管理的内部依赖；检查报告中的更新提示不等于应直接添加版本约束。

来源：[Kotlin Multiplatform 兼容表](https://kotlinlang.org/docs/multiplatform/multiplatform-compatibility-guide.html)、[AGP 元数据](https://dl.google.com/dl/android/maven2/com/android/tools/build/gradle/maven-metadata.xml)、[Gradle 9.8.1 安全修复](https://docs.gradle.org/9.8.1/release-notes.html)、[Gradle 安全公告列表](https://github.com/gradle/gradle/security/advisories)。

## 已核对无需追新稳定版的项目

通过 Maven 元数据或官方发布页核对：Kotlin 2.4.21、Activity Compose 1.13.0、Android Compose BOM 2026.09.00、AndroidX/JetBrains Lifecycle 2.11.0、Navigation Event 1.1.0、datetime 0.8.0、Dokka 2.2.0、Hot Reload 1.2.0、material-icons-extended 1.7.3、JUnit 4.13.2、AndroidX test-junit 1.3.0、Espresso 3.7.0、DLAppUtil 2.7.1。

部分 Google Maven 坐标直连发生 TLS 错误，未关闭证书校验；Activity 和测试库改用[Activity 官方发布页](https://developer.android.com/jetpack/androidx/releases/activity)及[Test 官方发布页](https://developer.android.com/jetpack/androidx/releases/test)补证。

## CI 与检查结果

`.github/workflows/deploy.yml` 仍使用 checkout@v4、setup-java@v3、upload-pages-artifact@v3、deploy-pages@v4，可单独安排 Actions 维护；本次未逐项验证其最新版本及迁移要求，不提供未经核实的目标版本。工作流设置 JDK 17，而仓库 Daemon JVM criteria 指定 Amazon JDK 21，不能仅凭 setup-java 的数字判定当前构建必然失败；可后续统一显式配置与实际工具链，避免额外下载和误解。

实际执行：

```sh
./gradlew dependencyUpdates --no-configuration-cache --no-parallel --console=plain
```

结果 `BUILD SUCCESSFUL in 1m 28s`，报告位于 `build/dependencyUpdates/report.txt`。首次因沙箱限制无法写 Gradle 缓存，正常提升权限后运行；插件要求关闭并行，补上参数后成功。任务成功不等于所有坐标均查询成功：输出对多项 KMP 依赖标记无法确认，已针对直接坐标补查 Maven 元数据。插件过滤所有非稳定版本，故 Material3 额外按官方配套表判断。

本次仅新增本审计文档，未执行升级构建、设备/UI 回归或全量传递依赖/JS 锁文件漏洞扫描。未启用 AOCI，也未声称索引已对齐；本次文档的索引维护待后续集中处理。

## 最小实施选择

最小方案仅升级 Compose 到 1.12.2，保留 Material3 与工具链；另一独立小步骤是 Gradle 9.8.2。其余候选按需要分批，避免把补丁更新扩大为整套技术栈迁移。
