plugins {
  id("bazzmovies.android.library")
}

android.namespace = "com.waffiq.bazz_movies.core.utils"

dependencies {
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.paging.runtime)
  implementation(project(":core:domain"))
}
