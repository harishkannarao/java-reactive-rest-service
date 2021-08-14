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
val flywayVersion: String by project
val testContainersVersion: String by project
val logstashLogbackEncoderVersion: String by project

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
		implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:$springBootVersion")
		implementation("io.r2dbc:r2dbc-postgresql:$r2dbcPostgresqlVersion") {
			exclude("io.projectreactor.netty", "reactor-netty")
		}
		implementation("org.postgresql:postgresql:$postgresqlVersion")
		implementation("org.flywaydb:flyway-core:$flywayVersion")
		implementation("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")
		testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
		testImplementation("io.projectreactor:reactor-test:$reactorTestVersion")
		testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		val commandLineProperties = System.getProperties().entries.associate { it.key.toString() to it.value }
		systemProperties(commandLineProperties)
	}

	tasks.withType<Jar> {
		archiveVersion.set(projectVersion)
	}

	task<JavaExec>("runLocal") {
		description = "Runs application locally"
		mainClass.set("com.harishkannarao.java.spring.rest.javareactiverestservice.runner.LocalRunnerWithFixtures")
		classpath = sourceSets["test"].runtimeClasspath
		args(emptyList<String>())
		val commandLineProperties = System.getProperties().entries.associate { it.key.toString() to it.value }
		systemProperties(commandLineProperties)
		dependsOn(tasks["compileJava"], tasks["compileTestJava"])
	}
}