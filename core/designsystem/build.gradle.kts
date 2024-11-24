plugins {
  id("bazzmovies.android.library")
}

android {
  namespace = "com.waffiq.bazz_movies.core.designsystem"
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
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.core.splashscreen)

  api(libs.google.material)
}
