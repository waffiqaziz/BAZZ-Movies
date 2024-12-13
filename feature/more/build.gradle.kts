plugins {
  id("bazzmovies.android.feature")
  id("bazzmovies.glide")
}

android.namespace = "com.waffiq.bazz_movies.feature.more"

dependencies {
  implementation(project(":core:uihelper"))
  implementation(project(":core:database"))
  implementation(project(":core:user"))
  implementation(project(":navigation"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.google.material)

  implementation(libs.country.picker.android)
}
