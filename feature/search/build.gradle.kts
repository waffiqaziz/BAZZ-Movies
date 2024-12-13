import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
  id("bazzmovies.glide")
}

android.namespace = "com.waffiq.bazz_movies.feature.search"

dependencies {
  implementation(project(":core:uihelper"))
  implementation(project(":core:movie"))
  implementation(project(":navigation"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)

  implementation(libs.google.material)
  implementation(libs.facebook.shimmer)
}
