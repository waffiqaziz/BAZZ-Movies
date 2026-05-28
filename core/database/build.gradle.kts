plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.room)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
}

android {
  namespace = "com.waffiq.bazz_movies.core.database"

  sourceSets {
    getByName("test").assets.directories.add("$projectDir/schemas")
  }

  buildFeatures {
    buildConfig = true
  }

  defaultConfig {
    buildConfigField("String", "APP_VERSION", "\"${libs.versions.versionName.get()}\"")
  }
}

ksp {
  arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}

dependencies {
  api(project(":core:common"))
  api(project(":core:models"))
  implementation(project(":core:coroutines"))
  implementation(project(":core:utils"))
  implementation("com.google.code.gson:gson:2.14.0")

  testImplementation(project(":core:test"))
  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.androidx.room.testing)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.turbine)
}

// https://developer.android.com/training/data-storage/room/migrating-db-versions#test
class RoomSchemaArgProvider(
  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  val schemaDir: File,
) : CommandLineArgumentProvider {

  override fun asArguments(): Iterable<String> = listOf("room.schemaLocation=${schemaDir.path}")
}
