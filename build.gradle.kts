import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
}

group = "me.tuesd4y"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //    implementation(kotlin("stdlib-jdk8"))
    implementation("one.util:streamex:0.6.8")
    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest-junit:2.0.0.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configurations {
    all {
        exclude("org.hamcrest","hamcrest-core")
        exclude("org.hamcrest","hamcrest-library")
    }
}