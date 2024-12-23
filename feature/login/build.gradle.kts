import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.login"

dependencies {
  implementation(project(":core:uihelper"))
  implementation(project(":core:user"))
  implementation(project(":core:movie"))
  implementation(project(":navigation"))

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
}
