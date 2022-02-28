plugins {
    id(Android.Plugin.library)
    id(Kotlin.Plugin.ID.android)
    id(Kotlin.Plugin.ID.kapt)
    id(Kotlin.Plugin.ID.parcelize)
    id(Maven.Plugin.public)
}

group = "com.github.D10NG"
version = "1.1.1"

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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = compose_ver
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(Kotlin.stdlib(kotlin_ver))
     Android
    api(AndroidX.core_ktx("1.7.0"))
    api(AndroidX.appcompat("1.4.1"))
    api(Android.Google.material("1.5.0"))

    // 单元测试（可选）
    testApi(Test.junit("4.13.2"))
    androidTestApi(AndroidX.Test.junit("1.1.3"))
    androidTestApi(AndroidX.Test.espresso_core("3.4.0"))

    // Compose
    api(AndroidX.Compose.ui(compose_ver))
    androidTestApi(AndroidX.Compose.ui_test(compose_ver))
    api(AndroidX.Compose.ui_tooling(compose_ver))
    api(AndroidX.Compose.foundation(compose_ver))
    api(AndroidX.Compose.animation(compose_ver))
    api(AndroidX.Compose.material(compose_ver))
    api(AndroidX.Compose.material_icons_core(compose_ver))
    api(AndroidX.Compose.material_icons_extended(compose_ver))
    api(AndroidX.Compose.runtime_livedata(compose_ver))
    api(AndroidX.activity_compose("1.4.0"))
    api(AndroidX.navigation_compose("2.4.1"))

    // Compose 拓展
    api(Accompanist.insets(accompanist_ver))
    api(Accompanist.insets_ui(accompanist_ver))
    api(Accompanist.systemuicontroller(accompanist_ver))
    api(Accompanist.appcompat_theme(accompanist_ver))
    api(Accompanist.pager(accompanist_ver))
    api(Accompanist.pager_indicators(accompanist_ver))
    api(Accompanist.swiperefresh(accompanist_ver))
    api(Accompanist.placeholder(accompanist_ver))
    api(Accompanist.placeholder_material(accompanist_ver))
    api(Accompanist.drawablepainter(accompanist_ver))
    api(Accompanist.flowlayout(accompanist_ver))
    api(Accompanist.permissions(accompanist_ver))
    api(Accompanist.navigation_animation(accompanist_ver))
    api(Accompanist.navigation_material(accompanist_ver))

    // Lifecycle
    api(AndroidX.Lifecycle.runtime_ktx(jetpack_lifecycle_ver))
    api(AndroidX.Lifecycle.common_java8((jetpack_lifecycle_ver)))
    api(AndroidX.Lifecycle.viewmodel_compose_support(jetpack_lifecycle_ver))
    api(AndroidX.Lifecycle.livedata_ktx_support(jetpack_lifecycle_ver))

    // Coroutines
    api(Kotlin.Coroutines.core(kotlin_coroutines_ver))
    api(Kotlin.Coroutines.android(kotlin_coroutines_ver))

    // 协程封装工具
    api(D10NG.DLCoroutinesUtil("0.3"))
    // APP通用工具
    api(D10NG.DLAppUtil("2.0"))
    // 时间工具
    api(D10NG.DLDateUtil("1.5"))
    coreLibraryDesugaring(Android.Tools.desugar_jdk_libs_coreLibraryDesugaring())
    // 字符串字节数据工具
    api(D10NG.DLTextUtil("1.3.0"))
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