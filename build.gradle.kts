// Top-level build file, configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.test) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.gms.googleServices) apply false
  alias(libs.plugins.firebase.crashlytics) apply false
  alias(libs.plugins.firebase.perf) apply false
  alias(libs.plugins.dependencyGuard) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.room) apply false
  alias(libs.plugins.ksp) apply false
  id("org.sonarqube") version "6.0.1.5171"
}

sonar {
  properties {
    property("sonar.projectKey", "waffiqaziz_BAZZ-Movies")
    property("sonar.organization", "waffiqaziz")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

buildscript {
  dependencies {
    classpath("org.bouncycastle:bcutil-jdk18on:1.79")
  }
}
