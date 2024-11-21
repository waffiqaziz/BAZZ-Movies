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
include(":core:data")
include(":core:designsystem")
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
include(":feature:about")
include(":feature:person")
include(":feature:detail")
include(":navigation")
include(":core:uihelper")
include(":core:common")
include(":core:favoritewatchlist")
