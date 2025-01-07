package com.financialsimulator.utils

import java.math.BigDecimal
import java.math.RoundingMode

object LoanUtil {
    fun getMultiplyScale(
        principalValue: BigDecimal,
        secondaryValue: BigDecimal?
            ): BigDecimal = principalValue.multiply(secondaryValue).setScale(2, RoundingMode.HALF_EVEN)
    fun getSubtractScale(
        principalValue: BigDecimal,
        secondaryValue: BigDecimal?
    ): BigDecimal = principalValue.subtract(secondaryValue).setScale(2, RoundingMode.HALF_EVEN)
}

