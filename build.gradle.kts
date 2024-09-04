// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  val kotlin_version by extra("1.9.10")
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
  }
}

plugins {
  id("com.android.application") version "8.2.0" apply false
  id("com.android.library") version "8.2.0" apply false
  id("org.jetbrains.kotlin.android") version "1.9.10" apply false

  id("com.google.gms.google-services") version "4.4.1" apply false
  id("com.google.firebase.crashlytics") version "2.9.9" apply false
  id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}