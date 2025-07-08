plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
}

android.namespace = "com.waffiq.bazz_movies.feature.more"

dependencies {
  implementation(project(":core:uihelper"))
  implementation(project(":core:database"))
  implementation(project(":core:user"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.google.material)

  implementation(libs.country.picker.android)

  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)

  androidTestImplementation(project(":core:designsystem"))
  androidTestImplementation(project(":core:test"))
  androidTestImplementation(libs.androidx.core.testing)
  androidTestImplementation(libs.androidx.datastore.preferences)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)
  androidTestImplementation(libs.androidx.fragment.testing)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.mockk.android)
  androidTestImplementation(libs.mockk.agent)
  androidTestImplementation(libs.truth)
}
