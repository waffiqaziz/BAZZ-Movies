plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.detail"

dependencies {
  implementation(project(":core:database"))
  implementation(project(":core:movie"))
  implementation(project(":core:user"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.activity)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.swiperefreshlayout)
  implementation(libs.androidx.paging.runtime)
  implementation(libs.expandable.textview)

  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.android.kotlin)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)
}
