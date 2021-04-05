package dev.icerock.kotless.kotlinversion

import com.gitlab.mvysny.konsumexml.anyName
import com.gitlab.mvysny.konsumexml.konsumeXml
import io.kotless.dsl.lang.http.Get
import io.kotless.dsl.lang.http.redirect
import io.kotless.dsl.model.HttpResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

private val httpClient = HttpClient(CIO)

@Get("/kotlin-version")
fun fetchKotlinVersion(group: String, name: String): HttpResponse {
    val path: String = group.split(".").plus(name).joinToString("/")

    val metadata: String = runBlocking { getPackageMavenMetadata(path = path) }
    val fetchVersion: String = getLatestVersion(metadata = metadata)
    val versionPom: String =
        runBlocking { getVersionPom(path = path, name = name, version = fetchVersion) }
    val dependencies: List<Dependency> = getDependencies(pom = versionPom)
    val kotlinVersion: String = getKotlinVersion(dependencies = dependencies)

    return redirect("https://img.shields.io/badge/kotlin-$kotlinVersion-orange")
}

internal suspend fun getPackageMavenMetadata(path: String): String {
    val mavenPath = "https://repo1.maven.org/maven2/$path"
    val mavenMetadataPath = "$mavenPath/maven-metadata.xml"

    return httpClient.get(mavenMetadataPath)
}

internal fun getLatestVersion(metadata: String): String {
    return with(metadata.konsumeXml()) {
        child("metadata") {
            child("groupId") { skipContents() }
            child("artifactId") { skipContents() }

            child("versioning") {
                childText("latest").also { skipContents() }
            }.also { skipContents() }
        }.also { skipContents() }
    }
}

internal suspend fun getVersionPom(path: String, name: String, version: String): String {
    val mavenPath = "https://repo1.maven.org/maven2/$path/$version"
    val pomPath = "$mavenPath/$name-$version.pom"

    return httpClient.get(pomPath)
}

internal fun getDependencies(pom: String): List<Dependency> {
    var result: List<Dependency>? = null
    with(pom.konsumeXml()) {
        child("project") {
            children(anyName) {
                if (name.localPart == "dependencies") {
                    result = children("dependency") {
                        Dependency(
                            groupId = childText("groupId"),
                            artifactId = childText("artifactId"),
                            version = childText("version")
                        ).also { skipContents() }
                    }
                } else skipContents()
            }
        }
    }
    return result.orEmpty()
}

internal fun getKotlinVersion(dependencies: List<Dependency>): String {
    return dependencies.first { dependency ->
        dependency.groupId == "org.jetbrains.kotlin" && dependency.artifactId.startsWith("kotlin-stdlib")
    }.version
}

internal data class Dependency(
    val groupId: String,
    val artifactId: String,
    val version: String
)
