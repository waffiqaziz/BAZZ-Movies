plugins {
  id("bazzmovies.android.library")
}

android.namespace = "com.waffiq.bazz_movies.core.uihelper"

dependencies {
  api(project(":core:common"))
  implementation(project(":core:designsystem"))

  api(libs.androidx.core.ktx)
  implementation(libs.androidx.paging.runtime)
}