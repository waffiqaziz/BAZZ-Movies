plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
}

android.namespace = "com.waffiq.bazz_movies.feature.more"

dependencies {
  implementation(project(":core:uihelper"))
  implementation(project(":core:database"))
  implementation(project(":core:user"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.google.material)

  implementation(libs.country.picker.android)

  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
}
