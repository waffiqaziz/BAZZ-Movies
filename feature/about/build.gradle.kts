plugins {
  alias(libs.plugins.bazzmovies.android.feature)
}

android.namespace = "com.waffiq.bazz_movies.feature.about"

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:uihelper"))

  implementation(libs.androidx.appcompat)
  implementation(libs.google.material)
  implementation(libs.androidx.constraintlayout)

  testImplementation(libs.androidx.test.core.ktx)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.android.kotlin)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)

  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.junit.ktx)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)
}
