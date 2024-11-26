import org.gradle.kotlin.dsl.android

plugins {
  id("bazzmovies.android.application")
  id("bazzmovies.android.application.firebase")
  id("bazzmovies.hilt")
  id("kotlin-parcelize")
}

android {
  namespace = "com.waffiq.bazz_movies"
  ndkVersion = libs.versions.ndkVersion.get()

  defaultConfig {
    applicationId = "com.bazz.bazz_movies"
    versionCode = 13
    versionName = "1.1.1"

    signingConfig = signingConfigs.getByName("debug")

    javaCompileOptions {
      annotationProcessorOptions {
        arguments["room.schemaLocation"] = "$projectDir/src/main/schemas"
      }
    }
  }

  buildTypes {
    getByName("debug") {
      isDebuggable = true
      isShrinkResources = false
      isMinifyEnabled = false

      resValue("string", "app_name", "BAZZ Movies Debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }

    create("staging") {
      isDebuggable = true
      isShrinkResources = false
      isMinifyEnabled = false

      resValue("string", "app_name", "BAZZ Movies Debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }

    getByName("release") {
      isDebuggable = false
      isShrinkResources = true
      isMinifyEnabled = true

      resValue("string", "app_name", "@string/app_name_release")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }
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
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.core.splashscreen)

  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.navigation.fragment)

  // leakcanary
  debugImplementation(libs.leakcanary)

  // third-party library
  implementation(libs.expandable.textview)
}

dependencyGuard {
  configuration("releaseCompileClasspath")
  configuration("releaseRuntimeClasspath")
}
