import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.hilt
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
  id("bazzmovies.glide")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.detail"

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
  buildFeatures {
    viewBinding = true
  }
}

dependencies {
  implementation(project(":core:movie"))
  implementation(project(":core:user"))
  implementation(project(":core:uihelper"))
  implementation(project(":navigation"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)

  implementation(libs.expandable.textview)
}
