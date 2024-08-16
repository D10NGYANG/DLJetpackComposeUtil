plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.d10ng.compose.demo"
    compileSdk = android_compile_sdk

    defaultConfig {
        applicationId = "com.d10ng.compose.demo"
        minSdk = android_min_sdk
        targetSdk = android_target_sdk
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        jvmToolchain(8)
    }
}

dependencies {
    // Android
    implementation("androidx.core:core-ktx:$androidx_core_ver")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation(project(mapOf("path" to ":library")))

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$jetpack_lifecycle_ver")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$jetpack_lifecycle_ver")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_ver")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_ver")

    // kotlinx.serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_serialization_json")

    // 导航路由
    implementation("io.github.raamcosta.compose-destinations:animations-core:$compose_destinations_ver")
    ksp("io.github.raamcosta.compose-destinations:ksp:$compose_destinations_ver")

    // 公共工具
    implementation("com.github.D10NGYANG:DLCommonUtil:$dl_common_ver")
    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:$dl_app_ver")
    // 时间工具
    implementation("com.github.D10NGYANG:DLDateUtil:$dl_date_ver")

    // 内存泄漏检查
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

}