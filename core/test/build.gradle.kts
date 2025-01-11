plugins {
  alias(libs.plugins.bazzmovies.android.library)
}

android.namespace = "com.waffiq.bazz_movies.core.test"

dependencies {
  implementation(libs.junit)
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.androidx.recyclerview)
}
