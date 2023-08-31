// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version android_build_ver apply false
    id("com.android.library") version android_build_ver apply false
    id("org.jetbrains.kotlin.android") version kotlin_ver apply false
    id("com.github.ben-manes.versions") version "0.47.0"
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}