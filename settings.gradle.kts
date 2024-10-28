pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
  repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
  }
}
rootProject.name = "BAZZ Movies"
include(":app")
include(":core")
include(":core-ui")
include(":feature-person")
include(":feature-detail")
include(":navigation")
