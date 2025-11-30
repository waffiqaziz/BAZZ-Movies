plugins {
  alias(libs.plugins.bazzmovies.android.application)
  alias(libs.plugins.bazzmovies.android.application.firebase)
  alias(libs.plugins.bazzmovies.android.application.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
  alias(libs.plugins.bazzmovies.hilt.test)
  id("kotlin-parcelize")
}

dependencies {
  implementation(project(":core:designsystem"))
  implementation(project(":core:domain"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:user"))
  implementation(project(":feature:about"))
  implementation(project(":feature:detail"))
  implementation(project(":feature:favorite"))
  implementation(project(":feature:home"))
  implementation(project(":feature:login"))
  implementation(project(":feature:more"))
  implementation(project(":feature:person"))
  implementation(project(":feature:search"))
  implementation(project(":feature:watchlist"))
  implementation(project(":navigation"))

  implementation(libs.androidx.activity)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.navigation.fragment)
  implementation(libs.androidx.swiperefreshlayout)
  implementation(libs.play.integrity)

  // leakcanary
  debugImplementation(libs.leakcanary)

  // third-party library
  implementation(libs.expandable.textview)

  // testing
  testImplementation(libs.mockk)

  androidTestImplementation(project(":core:instrumentationtest"))
  androidTestImplementation(libs.bundles.datastore)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.mockito.core)
  androidTestImplementation(libs.mockito.android)
  androidTestImplementation(libs.mockito.android.kotlin)
  androidTestImplementation(libs.mockk.android)
}

dependencyGuard {
  configuration("releaseCompileClasspath")
  configuration("releaseRuntimeClasspath")
}

// Disabling detekt from the check task
tasks.named("check").configure {
  this.setDependsOn(this.dependsOn.filterNot {
    it is TaskProvider<*> && it.name == "detekt"
  })
}
