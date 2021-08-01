rootProject.name = "java-reactive-rest-service"

pluginManagement {
    // variables for gradle.properties
    val springBootVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
            }
        }
    }
}
