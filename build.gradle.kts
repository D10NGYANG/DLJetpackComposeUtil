// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(Android.Classpath.gradle)
        classpath(Kotlin.Classpath.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(Maven.Url.jitpack)
    }
}

tasks.register<Delete>(name = "clean") {
    group = "build"
    delete(rootProject.buildDir)
}