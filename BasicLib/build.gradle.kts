plugins {
    id(Android.Plugin.library)
    id(Kotlin.Plugin.ID.android)
    id(Kotlin.Plugin.ID.kapt)
    id(Kotlin.Plugin.ID.parcelize)
    id(Maven.Plugin.public)
}

group = "com.github.D10NG"
version = "1.0.0"

android {
    compileSdk = Project.compile_sdk

    defaultConfig {
        minSdk = Project.min_sdk
        targetSdk = Project.target_sdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(Kotlin.stdlib)
    // Android
    api(Android.X.core_ktx)
    api(Android.X.appcompat)
    api(Android.Google.material)

    // 单元测试（可选）
    testImplementation(Test.junit)
    androidTestImplementation(Android.Test.junit)
    androidTestImplementation(Android.Test.espresso)

    // Compose
    api(Compose.AndroidX.ui)
    api(Compose.AndroidX.ui_tooling)
    api(Compose.AndroidX.foundation)
    api(Compose.AndroidX.material)
    api(Compose.AndroidX.activity)
    api(Compose.AndroidX.livedata)
    api(Compose.AndroidX.navigation)

    // Compose 拓展
    api(Accompanist.insets)
    api(Accompanist.insets_ui)
    api(Accompanist.systemuicontroller)
    api(Accompanist.appcompat_theme)
    api(Accompanist.pager)
    api(Accompanist.pager_indicators)
    api(Accompanist.swiperefresh)
    api(Accompanist.placeholder)
    api(Accompanist.placeholder_material)
    api(Accompanist.drawablepainter)
    api(Accompanist.flowlayout)
    api(Accompanist.permissions)
    api(Accompanist.navigation_animation)
    api(Accompanist.navigation_material)

    // Lifecycle
    api(Jetpack.Lifecycle.runtime)
    api(Jetpack.Lifecycle.compiler)
    api(Jetpack.Lifecycle.compose_viewmodel_support)
    api(Jetpack.Lifecycle.livedata_support)

    // Coroutines
    api(Kotlin.Coroutines.core)
    api(Kotlin.Coroutines.android)

    // 协程封装工具
    api(D10NG.coroutinesUtil)
    // APP通用工具
    api(D10NG.appUtil)
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components.getByName("release"))
            }
        }
    }
}