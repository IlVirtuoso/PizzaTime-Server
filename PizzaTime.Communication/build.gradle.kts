plugins {
    kotlin("jvm") version "2.0.0"

}

group = "com.PizzaTime"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.rabbitmq:amqp-client:5.21.0")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("com.google.code.gson:gson:2.11.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}