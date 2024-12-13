plugins {
  id("bazzmovies.android.library")
}

android.namespace = "com.waffiq.bazz_movies.core.common"

dependencies {
  implementation(project(":core:designsystem"))
}