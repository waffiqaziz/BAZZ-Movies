plugins {
  id("bazzmovies.android.library")
}

android.namespace = "com.waffiq.bazz_movies.core.uihelper"

dependencies {
  api(project(":core:designsystem"))
  api(project(":core:common"))

  api(libs.androidx.core.ktx)
  implementation(libs.androidx.paging.runtime)
}