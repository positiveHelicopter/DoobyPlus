import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import com.android.build.api.dsl.ApplicationExtension
import com.positiveHelicopter.doobyplus.configureFirebase

class FireBaseConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.gms.google-services")

            val extension = extensions.getByType<ApplicationExtension>()
            configureFirebase(extension)
        }
    }
}