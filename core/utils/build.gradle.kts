plugins {
  alias(libs.plugins.bazzmovies.android.library)
}

android.namespace = "com.waffiq.bazz_movies.core.utils"

dependencies {
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.paging.runtime)
  implementation(project(":core:domain"))

  testImplementation(libs.junit)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(project(":core:test"))
}
