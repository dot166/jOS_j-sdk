import com.vanniktech.maven.publish.SonatypeHost

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "8.5.1" apply false
    id("com.android.application") version "8.5.1" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "11.2.2" apply false
    id("com.vanniktech.maven.publish") version "0.29.0"
}

