pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
  repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
  }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "BAZZ-Movies"
include(":app")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:designsystem")
include(":core:favoritewatchlist")
include(":core:movie")
include(":core:network")
include(":core:test")
include(":core:uihelper")
include(":core:user")
include(":feature:about")
include(":feature:detail")
include(":feature:favorite")
include(":feature:home")
include(":feature:login")
include(":feature:more")
include(":feature:person")
include(":feature:search")
include(":feature:watchlist")
include(":navigation")
