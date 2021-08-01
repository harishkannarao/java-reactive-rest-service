plugins {
	id("java")
	id("org.springframework.boot")
}

// variables for gradle.properties
val projectVersion: String by project
val javaVersion: String by project
val reactorTestVersion: String by project
val springBootVersion: String by project
val r2dbcPostgresqlVersion: String by project
val postgresqlVersion: String by project

group = "com.harishkannarao.java.spring.rest"
version = ""
java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

allprojects {

	apply(plugin= "java")
	apply(plugin= "org.springframework.boot")

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
//		implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:$springBootVersion")
//		runtimeOnly("io.r2dbc:r2dbc-postgresql:$r2dbcPostgresqlVersion")
//		runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
		testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
		testImplementation("io.projectreactor:reactor-test:$reactorTestVersion")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		val commandLineProperties = System.getProperties().entries.associate { it.key.toString() to it.value }
		val finalSystemProperties = commandLineProperties + mapOf(Pair("reactor.netty.http.server.accessLogEnabled", "true"))
		systemProperties(finalSystemProperties)
	}

	tasks.withType<Jar> {
		archiveVersion.set(projectVersion)
	}

	task<JavaExec>("runLocal") {
		description = "Runs application locally"
		mainClass.set("com.harishkannarao.java.spring.rest.javareactiverestservice.runner.SpringBootTestRunner")
		classpath = sourceSets["test"].runtimeClasspath
		args(listOf("--spring.profiles.active=int-test", "--server.port=8080"))
		val commandLineProperties = System.getProperties().entries.associate { it.key.toString() to it.value }
		val finalSystemProperties = commandLineProperties + mapOf(Pair("reactor.netty.http.server.accessLogEnabled", "true"))
		systemProperties(finalSystemProperties)
		dependsOn(tasks["compileJava"], tasks["compileTestJava"])
	}
}