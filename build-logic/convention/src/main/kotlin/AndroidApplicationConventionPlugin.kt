import com.android.build.api.dsl.ApplicationExtension
import com.waffiq.bazz_movies.configureKotlinAndroid
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

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
        defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()

        @Suppress("UnstableApiUsage")
        testOptions.animationsDisabled = true
      }

      dependencies {
        "implementation"(libs.findLibrary("play.integrity").get())
      }
    }
  }
}
