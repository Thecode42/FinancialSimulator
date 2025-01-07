package com.financialsimulator

import com.financialsimulator.models.LoanRequestModel
import com.financialsimulator.models.PaymentScheduleResponseModel
import com.financialsimulator.routes.loanRoutes
import com.financialsimulator.serializations.configureSerialization
import com.financialsimulator.services.FixedAmortizationService
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.*
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import kotlin.test.Test
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

class LoanRoutesTest {
    @Test
    fun `test fixed amortization service json`() = testApplication {
        val mockService = mockk<FixedAmortizationService>()
        val expectedSchedule = listOf(
            PaymentScheduleResponseModel(1, BigDecimal("85.61"), BigDecimal("4.17"), BigDecimal("81.44"), BigDecimal("918.56"))
        )
        coEvery { mockService.calculateFixedAmortization(any()) } returns expectedSchedule
        application {
            routing {
                configureSerialization()
                loanRoutes()
            }
        }
        val requestBody = Json.encodeToString(
            LoanRequestModel.serializer(),
            LoanRequestModel(BigDecimal("1000"), BigDecimal(12), 48)
        )
        val response = client.post("/api/loan/amortization/fixed") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("remaining_balance"),"Response should contain payment schedule")
    }
    @Test
    fun `test fixed amortization service cvs`() = testApplication {
        val mockService = mockk<FixedAmortizationService>()
        val expectedSchedule = listOf(
            PaymentScheduleResponseModel(1, BigDecimal("85.61"), BigDecimal("4.17"), BigDecimal("81.44"), BigDecimal("918.56"))
        )
        coEvery { mockService.calculateFixedAmortization(any()) } returns expectedSchedule
        application {
            routing {
                configureSerialization()
                loanRoutes()
            }
        }
        val requestBody = Json.encodeToString(
            LoanRequestModel.serializer(),
            LoanRequestModel(BigDecimal("1000"), BigDecimal(12), 48)
        )
        val response = client.post("/api/loan/amortization/fixed") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Text.CSV)
            setBody(requestBody)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Month,Payment,Interest,Principal,RemainingBalance"))
    }
    @Test
    fun `test fixed amortization service pdf`() = testApplication {
        val mockService = mockk<FixedAmortizationService>()
        val expectedSchedule = listOf(
            PaymentScheduleResponseModel(1, BigDecimal("85.61"), BigDecimal("4.17"), BigDecimal("81.44"), BigDecimal("918.56"))
        )
        coEvery { mockService.calculateFixedAmortization(any()) } returns expectedSchedule
        application {
            routing {
                configureSerialization()
                loanRoutes()
            }
        }
        val requestBody = Json.encodeToString(
            LoanRequestModel.serializer(),
            LoanRequestModel(BigDecimal("1000"), BigDecimal(12), 48)
        )
        val response = client.post("/api/loan/amortization/fixed") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Pdf)
            setBody(requestBody)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Application.Pdf, response.contentType())
        assertTrue(response.bodyAsChannel().readRemaining().readBytes().isNotEmpty())
    }
    @Test
    fun `test invalid request format returns 500`() = testApplication {
        application {
            routing {
                configureSerialization()
                loanRoutes()
            }
        }
        val response = client.post("/api/loan/amortization/fixed") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertTrue(response.bodyAsText().contains("An error occurred"))
    }
}