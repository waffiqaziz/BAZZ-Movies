// Top-level build file, add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false // Available for app module
  alias(libs.plugins.android.library) apply false // Available for library modules
  alias(libs.plugins.kotlin.android) apply false // Available for Kotlin in all modules
//  alias(libs.plugins.detekt) version libs.versions.detekt.get() apply false
  alias(libs.plugins.gms.googleServices) apply false // Google services (used in specific modules)
  alias(libs.plugins.firebase.crashlytics) apply false // Crashlytics plugin for app
  alias(libs.plugins.ksp) version libs.versions.ksp.get() apply false // KSP plugin version defined
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}
