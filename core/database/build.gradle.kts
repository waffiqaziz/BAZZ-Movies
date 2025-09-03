plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.room)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
}

android {
  namespace = "com.waffiq.bazz_movies.core.database"

  sourceSets {
    getByName("test").assets.srcDir("$projectDir/schemas")
  }
}

ksp {
  arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}

dependencies {
  api(project(":core:domain"))
  api(project(":core:common"))
  implementation(project(":core:coroutines"))
  implementation(project(":core:utils"))

  testImplementation(project(":core:test"))
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
