plugins {
  id("bazzmovies.android.library")
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.core.domain"
dependencies { implementation(libs.androidx.core.ktx) }
