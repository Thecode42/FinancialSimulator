package com.financialsimulator.services

import com.financialsimulator.models.LoanRequestModel
import com.financialsimulator.models.PaymentScheduleResponseModel
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import java.io.ByteArrayOutputStream

import java.math.BigDecimal
import java.math.RoundingMode

class FixedAmortizationService {
    fun calculateFixedAmortization(request: LoanRequestModel): List<PaymentScheduleResponseModel> {
        val monthlyRate = request.annualRate.divide(BigDecimal(100)) // We convert the percentage to decimal
            .divide(BigDecimal(12), 10, RoundingMode.HALF_UP)
        val totalPayments = request.termInMonths

        // Monthly payment formula (A = P * r * (1 + r)^n / ((1 + r)^n - 1))
        // Numerator: Principal * Monthly Rate * (1 + Monthly Rate)^Total Payments
        val numerator = request.principal.multiply(monthlyRate).multiply((BigDecimal.ONE + monthlyRate).pow(totalPayments))
        // Denominator: (1 + Monthly Rate)^Total Payments - 1
        val denominator = (BigDecimal.ONE + monthlyRate).pow(totalPayments) - BigDecimal.ONE
        // Monthly payment: Division of the numerator by the denominator, with precision of 2 decimals
        val monthlyPayment = numerator.divide(denominator,2 , RoundingMode.HALF_UP)
        //List to store payment schedule
        val schedule = mutableListOf<PaymentScheduleResponseModel>()
        // We initialize the remaining balance with the loan amount (principal)
        var remainingBalance = request.principal
        // We iterate over each month of the total term
        for (month in 1..totalPayments){
            // Monthly Interest Calculation: Remaining Balance * Monthly Rate
            val interest = remainingBalance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP)

            val principal = if (month == totalPayments){
                remainingBalance
            } else {
                // Calculating the principal payment: Monthly payment - Monthly interest
                monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP)
            }
            // Remaining Balance Update: Previous Balance - Principal Payment
            remainingBalance = remainingBalance.subtract(principal).setScale(2, RoundingMode.HALF_UP)

            // We added the current payment detail to the schedule
            schedule.add(
                PaymentScheduleResponseModel(
                    month = month,
                    payment = monthlyPayment,
                    interest = interest,
                    principal = principal,
                    remainingBalance = remainingBalance
                )
            )
        }
        // Final validation to ensure the balance is exactly 0.00
        if (remainingBalance.compareTo(BigDecimal.ZERO) != 0) {
            val lastPayment = schedule.last()
            val adjustedPrincipal = lastPayment.principal.add(remainingBalance)
            schedule[schedule.size - 1] = lastPayment.copy(
                principal = adjustedPrincipal.setScale(2, RoundingMode.HALF_EVEN),
                remainingBalance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN)
            )
        }
        return schedule
    }
    fun exportToCSVFile(schedule: List<PaymentScheduleResponseModel>): ByteArray {
        val header = "Month,Payment,Interest,Principal,RemainingBalance"
        val rows = schedule.joinToString("\n") { payment ->
            "${payment.month},${payment.payment},${payment.interest},${payment.principal},${payment.remainingBalance}"
        }
        val csvContent = "$header\n$rows"
        return csvContent.toByteArray(Charsets.UTF_8)
    }
    fun exportToPDF(schedule: List<PaymentScheduleResponseModel>): ByteArray {
        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)

        val contentStream = PDPageContentStream(document, page)
        contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA) , 12f)

        contentStream.beginText()
        contentStream.newLineAtOffset(50f, 750f)
        contentStream.showText("Payment Schedule")
        contentStream.newLineAtOffset(0f, -20f)
        contentStream.showText("Month | Payment | Interest | Principal | Remaining Balance")
        contentStream.newLineAtOffset(0f, -20f)

        schedule.forEach { payment ->
            val row = "${payment.month} | ${payment.payment} | ${payment.interest} | ${payment.principal} | ${payment.remainingBalance}"
            contentStream.showText(row)
            contentStream.newLineAtOffset(0f, -20f)
        }
        contentStream.endText()
        contentStream.close()

        val outputStream = ByteArrayOutputStream()
        document.save(outputStream)
        document.close()

        return outputStream.toByteArray()
    }
}