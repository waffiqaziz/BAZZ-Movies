plugins {
  id("bazzmovies.android.library")
}

android {
  namespace = "com.waffiq.bazz_movies.core.uihelper"

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
  implementation(project(":core:designsystem"))
  api(project(":core:common"))

  implementation(libs.androidx.core.ktx)

  implementation(libs.androidx.paging.runtime)
}