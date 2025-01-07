package com.financialsimulator.models

import com.financialsimulator.utils.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class LoanRequestModel (
    @Serializable(with = BigDecimalSerializer::class)
    val principal: BigDecimal, //Payment to principal
    @SerialName("annual_rate")
    @Serializable(with = BigDecimalSerializer::class)
    val annualRate: BigDecimal, //Annual Rate
    @SerialName("term_in_months")
    val termInMonths: Int //Deadlines in months
)