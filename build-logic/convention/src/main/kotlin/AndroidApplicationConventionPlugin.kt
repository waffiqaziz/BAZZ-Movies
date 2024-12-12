import com.android.build.api.dsl.ApplicationExtension
import com.waffiq.bazz_movies.configureKotlinAndroid
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("com.dropbox.dependency-guard")
        apply("io.gitlab.arturbosch.detekt")
      }
      extensions.configure<ApplicationExtension> {
        configureKotlinAndroid(this)
        defaultConfig {
          applicationId = "com.bazz.bazz_movies"
          namespace = "com.waffiq.bazz_movies"
          ndkVersion = libs.findVersion("ndkVersion").get().toString()
          targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
          versionCode = 13
          versionName = "1.1.1"

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

        buildFeatures {
          viewBinding = true
          buildConfig = true
        }

        @Suppress("UnstableApiUsage")
        testOptions.animationsDisabled = true
        packaging {
          resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/NOTICE.md")
            excludes.add("META-INF/NOTICE.txt")
            excludes.add("META-INF/LICENSE-notice.md")
          }
        }
      }
    }
  }
}
