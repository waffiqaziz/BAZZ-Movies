plugins {
  id("bazzmovies.android.library")
  id("bazzmovies.android.room")
  id("bazzmovies.hilt")
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.core.database"

dependencies {
  api(project(":core:data"))
  api(project(":core:common"))
}

