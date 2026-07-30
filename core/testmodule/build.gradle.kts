plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
  alias(libs.plugins.bazzmovies.shared.test)
}

android.namespace = "com.waffiq.bazz_movies.core.testmodule"


dependencies {
  implementation(project(":core:uihelper"))
  implementation(project(":core:user"))
  implementation(project(":core:utils"))
  implementation(project(":navigation"))

  implementation(libs.hilt.test)
  implementation(libs.mockk.android)
  implementation(libs.google.material)

  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.mockk)
}