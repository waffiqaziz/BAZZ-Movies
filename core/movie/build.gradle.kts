plugins {
  id("bazzmovies.android.library")
  id("bazzmovies.hilt")
  id("bazzmovies.glide")
}

android.namespace = "com.waffiq.bazz_movies.core.movie"

dependencies {
  api(project(":core:common"))
  api(project(":core:domain"))
  api(project(":core:mappers"))
  api(project(":core:network"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.paging.runtime)
}
