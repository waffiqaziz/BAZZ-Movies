import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.person"

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:domain"))
  implementation(project(":core:mappers"))
  implementation(project(":core:network"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.activity)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.swiperefreshlayout)
  implementation(libs.expandable.textview)
  implementation(libs.google.material)

  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)

  androidTestImplementation(libs.androidx.espresso.contrib)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.mockk.android)
}
