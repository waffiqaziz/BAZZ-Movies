plugins {
  id("bazzmovies.android.library")
  id("bazzmovies.hilt")
}

android.namespace = "com.waffiq.bazz_movies.navigation"

dependencies {
  implementation(project(":core:data"))
}

