import com.android.build.gradle.LibraryExtension
import com.waffiq.bazz_movies.configureCommonAndroidSettings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "bazzmovies.android.library")
      apply(plugin = "bazzmovies.hilt")
      apply(plugin = "bazzmovies.detekt")
      extensions.configure<LibraryExtension> {
        configureCommonAndroidSettings(this)
        buildFeatures.viewBinding = true
        defaultConfig.consumerProguardFiles("consumer-rules.pro")
      }

      dependencies {
        add("implementation", project(":core:designsystem"))
        add("implementation", project(":navigation"))
      }
    }
  }
}
