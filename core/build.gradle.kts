import com.vanniktech.maven.publish.SonatypeHost

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
    id("com.android.library") version "8.7.0" apply false
    id("com.android.application") version "8.7.0" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "11.2.3" apply false
    id("com.vanniktech.maven.publish") version "0.29.0"
}

