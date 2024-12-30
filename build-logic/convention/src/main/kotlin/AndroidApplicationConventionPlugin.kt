import com.android.build.api.dsl.ApplicationExtension
import com.waffiq.bazz_movies.configureCommonAndroidSettings
import com.waffiq.bazz_movies.configureKotlinAndroid
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

  @Suppress("LongMethod")
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("com.dropbox.dependency-guard")
        apply("bazzmovies.detekt")
      }
      extensions.configure<ApplicationExtension> {
        configureKotlinAndroid(this)
        configureCommonAndroidSettings(this)
        buildFeatures {
          viewBinding = true
          buildConfig = true
        }
        defaultConfig {
          applicationId = "com.bazz.bazz_movies"
          namespace = "com.waffiq.bazz_movies"
          ndkVersion = libs.findVersion("ndkVersion").get().toString()
          targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
          versionCode = libs.findVersion("versionCode").get().toString().toInt()
          versionName = libs.findVersion("versionName").get().toString()

          testInstrumentationRunner = "com.waffiq.bazz_movies.testrunner.CustomTestRunner"

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
            isShrinkResources = true
            isMinifyEnabled = true

            resValue("string", "app_name", "BAZZ Movies Debug")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            proguardFiles(
              getDefaultProguardFile("proguard-android-optimize.txt"),
              "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
          }

          getByName("release") {
            isDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true

            resValue("string", "app_name", "@string/app_name_release")
            proguardFiles(
              getDefaultProguardFile("proguard-android-optimize.txt"),
              "proguard-rules.pro"
            )
          }
        }
      }
    }
  }
}
