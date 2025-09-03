import com.android.build.api.dsl.ApplicationExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {

      // Check for the existence of google-services.json to decide whether to apply Firebase plugins
      val googleServicesFile = target.file("app/google-services.json")
      if (!googleServicesFile.exists()) {
        logger.warn("[WARNING] google-services.json not found. Firebase will be disabled for this build.")
        return
      }

      apply(plugin = "com.google.gms.google-services")
      apply(plugin = "com.google.firebase.firebase-perf")
      apply(plugin = "com.google.firebase.crashlytics")

      logger.lifecycle("[INFO] google-services.json found. Firebase plugins applied.")

      dependencies {
        val bom = libs.findLibrary("firebase-bom").get()
        add("implementation", platform(bom))
        "implementation"(libs.findBundle("firebase").get())
      }

      extensions.configure<ApplicationExtension> {
        buildTypes.configureEach {
          // Enable mapping file upload for release builds only
          configure<CrashlyticsExtension> {
            mappingFileUploadEnabled = name == "release"
          }
        }
      }
    }
  }
}
