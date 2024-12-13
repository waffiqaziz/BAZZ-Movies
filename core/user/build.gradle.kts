import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.library")
  id("kotlin-parcelize")
  id("bazzmovies.hilt")
}

android.namespace = "com.waffiq.bazz_movies.core.user"

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:movie"))
  api(project(":core:network"))
  api(project(":core:data"))

  implementation(libs.androidx.lifecycle.livedata.core)
  implementation(libs.androidx.lifecycle.viewmodel)

  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.preferences)
}
