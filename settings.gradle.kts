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
include(":core:model")
include(":core:ui")
include(":core:user")
include(":core:network")
include(":core:movie")
include(":core:database")
include(":feature:login")
include(":feature:home")
include(":feature:search")
include(":feature:favorite")
include(":feature:watchlist")
include(":feature:more")
include(":feature:person")
include(":feature:detail")
include(":navigation")
