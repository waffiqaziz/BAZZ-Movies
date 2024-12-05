plugins {
  id("bazzmovies.android.library")
  id("kotlin-parcelize")
  id("bazzmovies.hilt")
  id("bazzmovies.glide")
}

android {
  namespace = "com.waffiq.bazz_movies.core.movie"

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
  api(project(":core:data"))
  api(project(":core:network"))
  api(project(":core:database"))

  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.core.ktx)

//  testImplementation(libs.junit)
//  androidTestImplementation(libs.junit)
//  androidTestImplementation(libs.androidx.test.rules)
//  androidTestImplementation(libs.androidx.test.runner)
//  androidTestImplementation(libs.androidx.test.ext.junit)
//  androidTestImplementation(libs.espresso.core)

  // paging
  implementation(libs.androidx.paging.runtime)
}