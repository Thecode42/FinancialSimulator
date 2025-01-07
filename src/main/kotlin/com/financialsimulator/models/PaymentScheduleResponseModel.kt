package com.financialsimulator.models

import com.financialsimulator.utils.BigDecimalSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class PaymentScheduleResponseModel (
    val month: Int, // Number of the month
    @Serializable(with = BigDecimalSerializer::class)
    val payment: BigDecimal, // Total monthly payment
    @Serializable(with = BigDecimalSerializer::class)
    val interest: BigDecimal, // Interests of this month
    @Serializable(with = BigDecimalSerializer::class)
    val principal: BigDecimal, // Payment to principal
    @SerialName("remaining_balance")
    @Serializable(with = BigDecimalSerializer::class)
    val remainingBalance: BigDecimal // Remaining balance
)