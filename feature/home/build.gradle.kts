plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
  id("kotlin-parcelize")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.home"

  testOptions {
    unitTests.apply {
      isIncludeAndroidResources = false
    }
  }
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))
  implementation(project(":core:mappers"))
  implementation(project(":core:user"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.viewpager2)
  implementation(libs.expandable.textview)

  implementation(libs.google.material)
  implementation(libs.facebook.shimmer)
}
