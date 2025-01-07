package com.financialsimulator.routes

import com.financialsimulator.models.LoanRequestModel
import com.financialsimulator.models.PaymentScheduleResponseModel
import com.financialsimulator.services.FixedAmortizationService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loanRoutes(){
    route("/api/loan"){
        post("/amortization/fixed") {
            val fixedAmortizationService = FixedAmortizationService()
            try {
                val request = call.receive<LoanRequestModel>()
                val paymentSchedule: List<PaymentScheduleResponseModel> = fixedAmortizationService.calculateFixedAmortization(request)
                when (call.request.acceptItems().firstOrNull()?.value) {
                    "text/csv" -> {
                        val csvBytes = fixedAmortizationService.exportToCSVFile(paymentSchedule)
                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "payment_schedule.csv").toString()
                        )
                        call.respondBytes(csvBytes, ContentType.Text.CSV)
                    }
                    "application/pdf" -> {
                        val pdfBytes = fixedAmortizationService.exportToPDF(paymentSchedule)
                        call.respondBytes(pdfBytes, ContentType.Application.Pdf)
                    } else -> {
                            call.respond(HttpStatusCode.OK, paymentSchedule)
                        }
                }
            }catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }
    }
}