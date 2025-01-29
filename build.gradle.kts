// Top-level build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.navigation.safeargs) apply false
    alias(libs.plugins.hilt) apply false
    id("com.android.library") version "8.1.1" apply false
}