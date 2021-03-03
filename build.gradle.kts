import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.palantir.docker") version "0.22.1"
	id("com.palantir.docker-compose") version "0.22.1"
	kotlin("jvm") version "1.4.30"
	kotlin("plugin.spring") version "1.4.30"
}

group = "com.fravega"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springdoc:springdoc-openapi-ui:1.5.2")
	implementation("org.springframework.boot:spring-boot-gradle-plugin:2.0.3.RELEASE")
	implementation("se.transmode.gradle:gradle-docker:1.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.2.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	ignoreFailures = true
}

docker {
	dependsOn(tasks.getByName("build"))

	val bootJar: BootJar by tasks

	name = project.name

	setDockerfile(file("Dockerfile"))

	files(bootJar.archiveFile)

	buildArgs(mapOf(
			"JAR_FILE" to bootJar.archiveFileName.get()
	))
}

dockerCompose {
	val bootJar: BootJar by tasks

	setTemplate("../docker-compose.yml.template")

	setDockerComposeFile("../docker-compose.yml")

	templateToken("jarFile", bootJar.archiveFileName.get())
}


