plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.d10ng.compose.demo"
    compileSdk = Project.compile_sdk

    defaultConfig {
        applicationId = "com.d10ng.compose.demo"
        minSdk = Project.min_sdk
        targetSdk = Project.target_sdk
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        jvmToolchain(8)
    }
    composeOptions {
        kotlinCompilerExtensionVersion = compose_compiler_ver
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Android
    implementation("androidx.core:core-ktx:1.10.1")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(mapOf("path" to ":library")))

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$jetpack_lifecycle_ver")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$jetpack_lifecycle_ver")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_ver")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_ver")

    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:2.3.2")
    // 时间工具
    implementation("com.github.D10NGYANG:DLDateUtil-jvm:1.8.3")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // 调试工具
    debugImplementation("com.github.simplepeng.SpiderMan:spiderman:v1.1.9") {
        exclude(group = "androidx.appcompat")
    }
    // 内存泄漏检查
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")

}