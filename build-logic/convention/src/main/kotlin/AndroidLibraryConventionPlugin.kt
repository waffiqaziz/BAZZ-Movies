import com.android.build.gradle.LibraryExtension
import com.waffiq.bazz_movies.configureCommonAndroidSettings
import com.waffiq.bazz_movies.configureKotlinAndroid
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.android.library")
      apply(plugin = "org.jetbrains.kotlin.android")
      apply(plugin = "bazzmovies.detekt")

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        configureCommonAndroidSettings(this)
        defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
        defaultConfig.consumerProguardFiles("consumer-rules.pro")
        // defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // testOptions.animationsDisabled = true
        // The resource prefix is derived from the module name,
        // so resources inside ":core:module1" must be prefixed with "core_module1_"
        resourcePrefix =
          path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
            .lowercase() + "_"
      }
    }
  }
}
