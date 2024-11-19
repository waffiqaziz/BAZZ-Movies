plugins {
  id("bazzmovies.android.feature")
  id("bazzmovies.glide")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.more"

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
  implementation(project(":core:database"))
  implementation(project(":core:user"))
  implementation(project(":navigation"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.google.material)

  implementation(libs.country.picker.android)
}
