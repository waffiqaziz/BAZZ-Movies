import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
  alias(libs.plugins.bazzmovies.hilt.test)
  id("kotlin-parcelize")
}

android{
  namespace = "com.waffiq.bazz_movies.feature.login"
  defaultConfig{
    testInstrumentationRunner = "com.waffiq.bazz_movies.feature.login.testrunner.CustomTestRunner"
  }
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:domain"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:user"))

  implementation(libs.androidx.appcompat)
  implementation(libs.google.material)
  implementation(libs.androidx.activity)

  // testing
  testImplementation(project(":core:test"))
  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockk)
  testImplementation(libs.truth)

  androidTestImplementation(project(":core:test"))
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)

  androidTestImplementation(libs.mockk.android)
  androidTestImplementation(libs.mockk.agent)
}
