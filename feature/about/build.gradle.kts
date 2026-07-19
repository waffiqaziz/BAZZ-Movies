plugins {
  alias(libs.plugins.bazzmovies.android.feature)
}

android.namespace = "com.waffiq.bazz_movies.feature.about"

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)

  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.junit.ktx)
  androidTestImplementation(libs.mockk.android)
}

// This module has no unit tests. Disable Gradle 9 "no tests discovered" failure.
// https://docs.gradle.org/current/userguide/upgrading_major_version_9.html#test_task_fails_when_no_tests_are_discovered
tasks.withType<Test>().configureEach {
  failOnNoDiscoveredTests = false
}
