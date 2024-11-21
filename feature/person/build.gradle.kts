import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
  id("bazzmovies.glide")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.person"

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
  implementation(project(":core:uihelper"))
  implementation(project(":navigation"))
  implementation(project(":core:movie"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.google.material)

  implementation(libs.expandable.textview)
}
