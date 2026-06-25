plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.room)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
  alias(libs.plugins.kotlin.serialization)
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

  implementation(libs.gson)
  implementation(libs.kotlinx.serialization.json)

  // Remove false warning "..should be marked with @InternalSerializationApi"
  // https://youtrack.jetbrains.com/issue/KTIJ-31549/K2-kotlinx.serialization.Serializable-error-only-in-IDE#focus=Comments-27-12461586.0-0
  // https://github.com/Kotlin/kotlinx.serialization/issues/2844#issuecomment-3254213059
  implementation(libs.androidx.annotation.experimental)

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
