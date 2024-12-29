import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.library")
  id("bazzmovies.glide")
  id("bazzmovies.hilt")
}

android.namespace = "com.waffiq.bazz_movies.core.favoritewatchlist"

dependencies {
  api(project(":core:domain"))
  api(project(":core:uihelper"))
  api(project(":core:movie"))
  api(project(":core:user"))
  api(project(":core:utils"))
  implementation(project(":core:designsystem"))
  implementation(project(":navigation"))

  implementation(libs.androidx.recyclerview)
  api(libs.androidx.paging.runtime)
}