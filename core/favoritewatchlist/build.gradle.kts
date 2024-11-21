import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.library")
  id("bazzmovies.glide")
  id("bazzmovies.hilt")
}

android {
  namespace = "com.waffiq.bazz_movies.core.favoritewatchlist"

  defaultConfig {
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
    }

    create("staging") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  api(project(":core:common"))
  api(project(":core:database"))
  api(project(":core:data"))
  api(project(":navigation"))
  api(project(":core:uihelper"))
  api(project(":core:network"))
  implementation(project(":core:designsystem"))

  implementation(libs.androidx.recyclerview)
  api(libs.androidx.paging.runtime)
}