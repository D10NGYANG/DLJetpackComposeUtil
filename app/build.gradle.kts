plugins {
    id(Android.Plugin.application)
    id(Kotlin.Plugin.ID.android)
    id(Kotlin.Plugin.ID.kapt)
    id(Kotlin.Plugin.ID.parcelize)
}

android {
    compileSdk = Project.compile_sdk

    defaultConfig {
        applicationId = "com.d10ng.basicjetpackcomposeapp.demo"
        minSdk = Project.min_sdk
        targetSdk = Project.target_sdk
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Kotlin.jvm_target_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.AndroidX.version
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    // Android
    implementation(Android.X.core_ktx)
    implementation(Android.X.appcompat)
    implementation(Android.Google.material)

    implementation(project(mapOf("path" to ":BasicLib")))

    // 单元测试（可选）
    testImplementation(Test.junit)

    // Compose
    implementation(Compose.AndroidX.ui)
    implementation(Compose.AndroidX.ui_tooling)
    implementation(Compose.AndroidX.foundation)
    implementation(Compose.AndroidX.material)
    implementation(Compose.AndroidX.activity)
    implementation(Compose.AndroidX.livedata)
    implementation(Compose.AndroidX.navigation)

    // Compose 拓展
    implementation(Accompanist.insets)
    implementation(Accompanist.insets_ui)
    implementation(Accompanist.systemuicontroller)
    implementation(Accompanist.swiperefresh)
    implementation(Accompanist.navigation_animation)

    // Lifecycle
    implementation(Jetpack.Lifecycle.runtime)
    implementation(Jetpack.Lifecycle.compiler)
    implementation(Jetpack.Lifecycle.compose_viewmodel_support)
    implementation(Jetpack.Lifecycle.livedata_support)

    // Coroutines
    implementation(Kotlin.Coroutines.core)
    implementation(Kotlin.Coroutines.android)

    // 协程封装工具
    implementation(D10NG.coroutinesUtil)
    // APP通用工具
    implementation(D10NG.appUtil)
}