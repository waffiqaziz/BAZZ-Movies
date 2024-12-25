import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
  id("bazzmovies.glide")
}

android.namespace = "com.waffiq.bazz_movies.feature.detail"

dependencies {
  implementation(project(":core:domain"))
  implementation(project(":core:movie"))
  implementation(project(":core:user"))
  implementation(project(":core:uihelper"))
  implementation(project(":navigation"))

  implementation(libs.androidx.activity)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)

  implementation(libs.expandable.textview)
}
