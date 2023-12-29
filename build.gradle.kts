val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val postgresqlVersion: String by project
val koinVersion: String by project
val ktormVersion: String by project

plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
}

group = "team.bupt.h7"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.2.4")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.2.4")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.2.4")
    implementation("io.ktor:ktor-server-netty-jvm:2.2.4")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation("io.ktor:ktor-server-status-pages:2.2.4")
    implementation("io.ktor:ktor-server-cors:2.2.4")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("io.ktor:ktor-server-rate-limit:2.2.4")
    implementation("io.ktor:ktor-server-forwarded-header:2.2.4")
    implementation("software.amazon.awssdk:s3:2.22.7")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.2.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

ktor {
    docker {
        localImageName.set("place-quest-server")
        imageTag.set("0.0.1-preview")
        jreVersion.set(JavaVersion.VERSION_11)
    }
}