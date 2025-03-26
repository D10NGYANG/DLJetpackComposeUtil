import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
}

group = "com.github.D10NGYANG"
version = "3.0.0-RC08"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishLibraryVariants("release")
    }

    iosArm64()
    iosSimulatorArm64()
    iosX64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.library()
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.constraintlayout.compose.multiplatform)
            // Lifecycle
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // 时间工具
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
            //implementation(libs.dl.date)
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
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

val bds100MavenUsername: String by project
val bds100MavenPassword: String by project

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

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
                setUrl("https://nexus.bds100.com/repository/maven-releases/")
            }
        }
    }
}

