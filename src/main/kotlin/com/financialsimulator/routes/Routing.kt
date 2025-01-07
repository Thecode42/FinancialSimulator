package com.financialsimulator.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Main routing
 */
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(HttpStatusCode.NotFound, mapOf("message" to "Something went wrong."))
        }
        loanRoutes()
    }
}
