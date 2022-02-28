// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Android.Plugin.application) version android_build_ver apply false
    id(Android.Plugin.library) version android_build_ver apply false
    id(Kotlin.Plugin.ID.android) version kotlin_ver apply false
}

tasks.register<Delete>(name = "clean") {
    group = "build"
    delete(rootProject.buildDir)
}