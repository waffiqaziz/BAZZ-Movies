plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.login"

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:domain"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:user"))

  implementation(libs.androidx.appcompat)
  implementation(libs.google.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  // testing
  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockk)
  testImplementation(libs.truth)
  testImplementation(libs.robolectric)

  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.mockk.android)
}
