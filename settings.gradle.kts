rootProject.name = "java-reactive-rest-service"

pluginManagement {
    // variables for gradle.properties
    val springBootVersion: String by settings
    val pluginUseLatestVersions: String by settings
    val pluginVersions: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
                "se.patrikerdes.use-latest-versions" -> useVersion(pluginUseLatestVersions)
                "com.github.ben-manes.versions" -> useVersion(pluginVersions)
            }
        }
    }
}
