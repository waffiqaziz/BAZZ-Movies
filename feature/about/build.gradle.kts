plugins {
  alias(libs.plugins.bazzmovies.android.feature)
}

android.namespace = "com.waffiq.bazz_movies.feature.about"

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:uihelper"))

  implementation(libs.androidx.appcompat)
  implementation(libs.google.material)
  implementation(libs.androidx.constraintlayout)
}
