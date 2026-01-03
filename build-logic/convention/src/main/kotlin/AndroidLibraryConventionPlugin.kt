import com.android.build.api.dsl.LibraryExtension
import com.waffiq.bazz_movies.configureCommonAndroidSettings
import com.waffiq.bazz_movies.configureKotlinAndroid
import com.waffiq.bazz_movies.configureMockitoAgent
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.android.library")
      apply(plugin = "org.jetbrains.kotlin.android")
      apply(plugin = "bazzmovies.detekt")

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        configureCommonAndroidSettings(this)
        configureMockitoAgent()
        defaultConfig.consumerProguardFiles("consumer-rules.pro")
        testOptions.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
        // defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // The resource prefix is derived from the module name,
        // so resources inside ":core:module1" must be prefixed with "core_module1_"
        resourcePrefix =
          path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
            .lowercase() + "_"
      }

      dependencies {
        add("testImplementation", kotlin("test"))
      }
    }
  }
}
