plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.shared.test)
}

android.namespace = "com.waffiq.bazz_movies.core.test"

dependencies {
  implementation(project(":core:domain"))
  implementation(project(":core:network"))

  implementation(libs.androidx.appcompat.resources)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.recyclerview)
  implementation(libs.junit)
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.mockk)
}
