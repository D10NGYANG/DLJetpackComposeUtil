plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.github.D10NGYANG"
version = "2.0.30"

android {
    namespace = "com.d10ng.compose"
    compileSdk = android_compile_sdk

    defaultConfig {
        minSdk = android_min_sdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // Android
    implementation("androidx.core:core-ktx:$androidx_core_ver")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // compose
    val composeBom = platform(compose_ver)
    api(composeBom)
    androidTestApi(composeBom)
    // Material Design 3
    api("androidx.compose.material3:material3")
    // Android Studio Preview support
    api("androidx.compose.ui:ui-tooling-preview")
    debugApi("androidx.compose.ui:ui-tooling")
    // UI Tests
    androidTestApi("androidx.compose.ui:ui-test-junit4")
    debugApi("androidx.compose.ui:ui-test-manifest")
    // Integration with activities
    api("androidx.activity:activity-compose:1.9.2")
    // constraintlayout
    api("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha13")

    // startup
    implementation("androidx.startup:startup-runtime:1.1.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$jetpack_lifecycle_ver")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$jetpack_lifecycle_ver")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_ver")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_ver")

    // 导航路由
    implementation("io.github.raamcosta.compose-destinations:animations-core:$compose_destinations_ver")

    // 时间工具
    implementation("com.github.D10NGYANG:DLDateUtil:$dl_date_ver")
    // 公共工具
    implementation("com.github.D10NGYANG:DLCommonUtil:$dl_common_ver")
}

val bds100MavenUsername: String by project
val bds100MavenPassword: String by project

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                artifactId = "DLJetpackComposeUtil"
                from(components.getByName("release"))
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