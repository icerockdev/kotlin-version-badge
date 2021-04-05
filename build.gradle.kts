import io.kotless.KotlessConfig.Optimization.MergeLambda
import io.kotless.plugin.gradle.dsl.KotlessConfig.Optimization.Autowarm
import io.kotless.plugin.gradle.dsl.kotless

plugins {
    kotlin("jvm") version "1.4.31"
    id("io.kotless") version "0.1.7-beta-5"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("io.kotless:kotless-lang:0.1.7-beta-5")
    implementation("io.ktor:ktor-client-core:1.5.3")
    implementation("io.ktor:ktor-client-cio:1.5.3")
    implementation("com.gitlab.mvysny.konsume-xml:konsume-xml:0.14")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.31")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

group = "dev.icerock.kotless.kotlinversion"
version = "1.0-SNAPSHOT"

kotless {
    config {
        bucket = "fetch-kotlin-version"

        terraform {
            profile = "fetchkotlinversion.kotless.user"
            region = "ap-southeast-1"
        }

        optimization {
            mergeLambda = MergeLambda.None
            autowarm = Autowarm(enable = false)
        }
    }
    webapp {
        lambda {
            memoryMb = 128

            kotless {
                packages = setOf("dev.icerock.kotless.kotlinversion")
            }
        }
    }
}
