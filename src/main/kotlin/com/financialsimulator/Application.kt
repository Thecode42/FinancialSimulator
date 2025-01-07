package com.financialsimulator

import com.financialsimulator.routes.configureRouting
import com.financialsimulator.serializations.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
