plugins { id("bazzmovies.android.library") }

android.namespace = "com.waffiq.bazz_movies.core.utils"

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(project(":core:domain"))
}
