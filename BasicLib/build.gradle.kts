plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("maven-publish")
}

group = "com.github.D10NG"
version = "1.2.0"

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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // Android
    api("androidx.core:core-ktx:1.8.0")
    api("androidx.appcompat:appcompat:1.5.0")
    api("com.google.android.material:material:1.6.1")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // Compose
    api("androidx.compose.ui:ui:$compose_ver")
    androidTestApi("androidx.compose.ui:ui-test-junit4:$compose_ver")
    api("androidx.compose.ui:ui-tooling:$compose_ver")
    api("androidx.compose.foundation:foundation:$compose_ver")
    api("androidx.compose.animation:animation:$compose_ver")
    api("androidx.compose.material:material:$compose_ver")
    api("androidx.compose.material:material-icons-core:$compose_ver")
    api("androidx.compose.material:material-icons-extended:$compose_ver")
    api("androidx.compose.runtime:runtime-livedata:$compose_ver")
    api("androidx.activity:activity-compose:1.5.1")
    api("androidx.navigation:navigation-compose:2.5.1")
    api("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // Compose 拓展
    api("com.google.accompanist:accompanist-systemuicontroller:$accompanist_ver")
    api("com.google.accompanist:accompanist-appcompat-theme:$accompanist_ver")
    api("com.google.accompanist:accompanist-pager:$accompanist_ver")
    api("com.google.accompanist:accompanist-pager-indicators:$accompanist_ver")
    api("com.google.accompanist:accompanist-swiperefresh:$accompanist_ver")
    api("com.google.accompanist:accompanist-placeholder:$accompanist_ver")
    api("com.google.accompanist:accompanist-placeholder-material:$accompanist_ver")
    api("com.google.accompanist:accompanist-drawablepainter:$accompanist_ver")
    api("com.google.accompanist:accompanist-flowlayout:$accompanist_ver")
    api("com.google.accompanist:accompanist-permissions:$accompanist_ver")
    api("com.google.accompanist:accompanist-navigation-animation:$accompanist_ver")
    api("com.google.accompanist:accompanist-navigation-material:$accompanist_ver")
    api("com.google.accompanist:accompanist-webview:$accompanist_ver")

    // Lifecycle
    api("androidx.lifecycle:lifecycle-runtime-ktx:$jetpack_lifecycle_ver")
    api("androidx.lifecycle:lifecycle-common-java8:$jetpack_lifecycle_ver")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$jetpack_lifecycle_ver")
    api("androidx.lifecycle:lifecycle-viewmodel-compose:$jetpack_lifecycle_ver")

    // Coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_ver")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_ver")

    // 协程封装工具
    api("com.github.D10NGYANG:DLCoroutinesUtil:0.3")
    // APP通用工具
    api("com.github.D10NGYANG:DLAppUtil:2.1")
    // 时间工具
    api("com.github.D10NGYANG:DLDateUtil:1.6")
    // 字符串字节数据工具
    api("com.github.D10NGYANG:DLTextUtil:1.4.0")
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