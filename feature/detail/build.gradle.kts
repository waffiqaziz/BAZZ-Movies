import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.detail"

dependencies {
  implementation(project(":core:database"))
  implementation(project(":core:movie"))
  implementation(project(":core:user"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.activity)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)

  implementation(libs.expandable.textview)
}
