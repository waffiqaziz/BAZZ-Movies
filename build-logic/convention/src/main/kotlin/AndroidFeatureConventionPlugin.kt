import com.android.build.gradle.LibraryExtension
import com.waffiq.bazz_movies.configureCommonAndroidSettings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("bazzmovies.android.library")
        apply("bazzmovies.hilt")
        apply("bazzmovies.detekt")
      }
      extensions.configure<LibraryExtension> {
        configureCommonAndroidSettings(this)
        buildFeatures.viewBinding = true
        defaultConfig.consumerProguardFiles("consumer-rules.pro")
        buildTypes {
          getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
          }
          if (!buildTypes.names.contains("staging")) {
            create("staging") {
              isMinifyEnabled = true
              proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
              )
            }
          }
          getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
              getDefaultProguardFile("proguard-android-optimize.txt"),
              "proguard-rules.pro"
            )
          }
        }
      }

      dependencies {
        add("implementation", project(":core:designsystem"))
        add("implementation", project(":navigation"))
      }
    }
  }
}
