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
include(":core:coroutines")
include(":core:data")
include(":core:database")
include(":core:designsystem")
include(":core:domain")
include(":core:favoritewatchlist")
include(":core:instrumentationtest")
include(":core:mappers")
include(":core:movie")
include(":core:network")
include(":core:test")
include(":core:uihelper")
include(":core:user")
include(":core:utils")

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

/**
 * Check if the current Java version is compatible with JDK 21.
 * If not, throw an error with a message indicating the mismatch.
 */
check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
  """
    BAZZ Movies is using JDK 21 but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
  """.trimIndent()
}
