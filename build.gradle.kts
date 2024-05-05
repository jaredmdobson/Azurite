import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.register

plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "com.GwgCommunity"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories { mavenCentral() }

val junitVersion = "5.8.0"
val lwjglVersion = "3.3.3"
val jomlVersion = "1.10.5"

val lwjglNatives = when {
    OperatingSystem.current().isLinux -> "natives-linux"
    OperatingSystem.current().isMacOsX -> "natives-macos"
    OperatingSystem.current().isWindows -> "natives-windows"
    else -> throw GradleException("Unsupported operating system")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.junit.vintage:junit-vintage-engine:$junitVersion")

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    runtimeOnly("org.lwjgl:lwjgl:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglNatives")

    implementation("org.joml:joml:$jomlVersion")
}

application {
    mainClass.set("scenes.TopDownDemo")
    applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}

// aliases
tasks.register("fatJar") { dependsOn(tasks.named("shadowJar")) }
tasks.register("execute") { dependsOn(tasks.named("runShadow")) }