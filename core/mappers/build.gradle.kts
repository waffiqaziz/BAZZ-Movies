plugins {
  alias(libs.plugins.bazzmovies.android.library)
}

android.namespace = "com.waffiq.bazz_movies.core.mappers"
dependencies {
  implementation(project(":core:domain"))
  implementation(project(":core:network"))

  testImplementation(libs.junit)
}
