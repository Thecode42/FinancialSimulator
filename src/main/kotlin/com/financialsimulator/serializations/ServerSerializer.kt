package com.financialsimulator.serializations

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            kotlinx.serialization.json.Json {
                prettyPrint = true // Prints JSON in a human-readable, indented format.
                ignoreUnknownKeys = true // Ignores keys in the JSON that are not defined in the data class.
                isLenient = true // Allows less strict JSON formats, such as single quotes or extra whitespace.
                explicitNulls = false // Does not include explicit null values in the JSON output.
                encodeDefaults = true
            }
        )
    }
}
