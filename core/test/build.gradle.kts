plugins {
  id("bazzmovies.android.library")
}

android.namespace = "com.waffiq.bazz_movies.core.test"

dependencies {
  implementation(libs.junit)
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.recyclerview)
}
