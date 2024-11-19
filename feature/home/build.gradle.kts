plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
  id("bazzmovies.glide")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.home"

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
  implementation(project(":core:common"))
  implementation(project(":core:uihelper"))
  implementation(project(":navigation"))
  implementation(project(":core:movie"))
  implementation(project(":core:user"))
  implementation(project(":feature:detail"))

  implementation(libs.shimmer)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.viewpager2)
  implementation(libs.expandable.textview)

  implementation(libs.google.material)
  implementation(libs.android.veil)
}
