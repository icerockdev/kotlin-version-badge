package dev.icerock.kotless.kotlinversion

import io.kotless.dsl.lang.http.Get

@Get("/kotlin-version")
fun fetchKotlinVersion(group: String, name: String): String {
    return "i will try to get kotlin version for $group:$name"
}
