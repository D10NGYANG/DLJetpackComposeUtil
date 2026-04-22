import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokka.javadoc)
    id("maven-publish")
}

group = "com.github.D10NGYANG"
version = "3.1.4"

kotlin {
    withSourcesJar(publish = true)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishLibraryVariants("release")
    }

    iosArm64()
    iosSimulatorArm64()
    iosX64()


    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.constraintlayout.compose.multiplatform)
            // Lifecycle
            //implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // 时间工具
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    packageOfResClass = "com.d10ng.compose.resources"
}

android {
    namespace = "com.d10ng.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

dokka {
    moduleName.set("D10NGYANG Compose Library")

    dokkaSourceSets.configureEach {
        skipDeprecated.set(false)
        reportUndocumented.set(false)
        skipEmptyPackages.set(true)
    }
}

val javadocJar by tasks.registering(Jar::class) {
    // Gradle 会自动解析依赖，只需指定新任务的 outputDirectory 即可
    from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val bds100MavenUsername = project.findProperty("bds100MavenUsername")?.toString() ?: ""
val bds100MavenPassword = project.findProperty("bds100MavenPassword")?.toString() ?: ""

afterEvaluate {
    publishing {
        publications {
            withType(MavenPublication::class) {
                //artifactId = artifactId.replace(project.name, rootProject.name)
                artifact(tasks["javadocJar"])
            }
        }
        repositories {
            maven {
                url = uri("/Users/d10ng/project/kotlin/maven-repo/repository")
            }
            maven {
                credentials {
                    username = bds100MavenUsername
                    password = bds100MavenPassword
                }
                url = uri("https://nexus.bds100.com/repository/maven-releases/")
            }
        }
    }
}

