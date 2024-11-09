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
include(":feature-home")
include(":feature-person")
include(":feature-detail")
include(":feature-search")
include(":navigation")
include(":feature-login")
include(":core-user")
include(":core-network")
include(":feature-more")
