plugins {
  id("bazzmovies.android.application")
  id("bazzmovies.android.application.firebase")
  id("bazzmovies.hilt")
  id("bazzmovies.hilt.test")
  id("kotlin-parcelize")
}

dependencies {
  implementation(project(":core:data"))
  implementation(project(":core:user"))
  implementation(project(":core:uihelper"))
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

  // jetpack library
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.core.splashscreen)

  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.navigation.fragment)
  implementation(libs.play.integrity)

  // leakcanary
  debugImplementation(libs.leakcanary)

  // third-party library
  implementation(libs.expandable.textview)

  // testing
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.intents)

  testImplementation(libs.mockk)
  androidTestImplementation(libs.mockk.android)
  androidTestImplementation(libs.mockk.agent)
  androidTestImplementation(libs.androidx.datastore.core)
  androidTestImplementation(libs.androidx.datastore.preferences)
}

dependencyGuard {
  configuration("releaseCompileClasspath")
  configuration("releaseRuntimeClasspath")
}

detekt {
  toolVersion = libs.versions.detekt.get()
  parallel = true
  config.setFrom("$rootDir/config/detekt/detekt.yml")
  buildUponDefaultConfig = true
  baseline = file("${rootProject.projectDir}/config/baseline.xml")
  allRules = true
}

// Disabling detekt from the check task
tasks.named("check").configure {
  this.setDependsOn(this.dependsOn.filterNot {
    it is TaskProvider<*> && it.name == "detekt"
  })
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
  // Specify input source files to analyze
  setSource(files("src/main/java"))
  include(
    "**/*.kt",
    "**/*.kts",
  )
  // Specify config file(s) if necessary
  config.setFrom(files("$rootDir/config/detekt/detekt.yml"))

  // Customize reports here
  reports {
    xml {
      required.set(true) // Generates an XML report (set to false if not needed)
      outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt-report.xml"))
    }
    html {
      required.set(true) // Generates an HTML report (set to false if not needed)
      outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt-report.html"))
    }
    txt {
      required.set(false) // Optional: Generates a text report
      outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt-report.txt"))
    }
    sarif {
      required.set(false) // Optional: SARIF report (used for integrations)
      outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt-report.sarif"))
    }
  }
}
