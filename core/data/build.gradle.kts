plugins {
  id("bazzmovies.android.library")
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.core.model"

dependencies {
  api(project(":core:common"))
}
