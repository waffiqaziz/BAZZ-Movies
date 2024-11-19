plugins {
  id("bazzmovies.android.library")
  id("bazzmovies.android.room")
  id("bazzmovies.hilt")
  id("kotlin-parcelize")
}

android {
  namespace = "com.waffiq.bazz_movies.core.database"

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
  defaultConfig {
    consumerProguardFiles("consumer-rules.pro")
  }
}
dependencies {
  api(project(":core:data"))
  api(project(":core:common"))
}

