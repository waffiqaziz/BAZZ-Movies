plugins {
  alias(libs.plugins.bazzmovies.android.library)
}

android.namespace = "com.waffiq.bazz_movies.core.test"

dependencies {
  implementation(libs.androidx.appcompat.resources)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.espresso.core)
  implementation(libs.androidx.recyclerview)
  implementation(libs.junit)
  implementation(libs.kotlinx.coroutines.test)
}
