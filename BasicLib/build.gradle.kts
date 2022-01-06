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
    implementation(Android.X.core_ktx)
    implementation(Android.X.appcompat)
    implementation(Android.Google.material)

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

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components.getByName("release"))
            }
        }
    }
}