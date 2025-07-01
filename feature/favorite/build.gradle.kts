import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.favorite"

dependencies {
  implementation(project(":core:favoritewatchlist"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.swiperefreshlayout)

  // room
  implementation(libs.androidx.room.common)
  ksp(libs.androidx.room.compiler)
}
