import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
  id("bazzmovies.glide")
}

android.namespace = "com.waffiq.bazz_movies.feature.person"

dependencies {
  implementation(project(":core:uihelper"))
  implementation(project(":navigation"))
  implementation(project(":core:movie"))

  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.google.material)

  implementation(libs.expandable.textview)

  testImplementation(project(":core:test"))
  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.android.kotlin)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)
}
