import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.springframework.boot.gradle.tasks.bundling.BootWar

plugins {
	war
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "2.0.0"
	kotlin("plugin.spring") version "1.9.24"
}

group = "com.PizzaTime"
java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	implementation("mysql:mysql-connector-java:8.0.28")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.google.code.gson:gson:2.11.0")
	implementation("com.rabbitmq:amqp-client:5.21.0")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	jvmToolchain(21)
}
tasks.withType<Test> {
	useJUnitPlatform()
}

// set name with ./gradlew -PwarName=<custom_name>.war bootWar

