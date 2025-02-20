import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.glide)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
}

android.namespace = "com.waffiq.bazz_movies.core.favoritewatchlist"

dependencies {
  api(project(":core:database"))
  api(project(":core:domain"))
  api(project(":core:uihelper"))
  api(project(":core:mappers"))
  api(project(":core:movie"))
  api(project(":core:user"))
  api(project(":core:utils"))
  implementation(project(":core:designsystem"))
  implementation(project(":navigation"))

  implementation(libs.androidx.recyclerview)
  api(libs.androidx.paging.runtime)
}