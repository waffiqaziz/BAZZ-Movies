import org.gradle.kotlin.dsl.testImplementation

plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
  alias(libs.plugins.bazzmovies.kotest)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.list"

dependencies {
  implementation(project(":core:movie"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.activity)
  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.swiperefreshlayout)

  testImplementation(libs.androidx.test.core)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.android.kotlin)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.turbine)
  testImplementation(libs.truth)
  testRuntimeOnly(libs.junit.vintage)

  androidTestImplementation(libs.androidx.espresso.intents)
  androidTestImplementation(libs.androidx.espresso.contrib)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)
  androidTestImplementation(libs.androidx.junit.ktx)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.mockk.android)
}