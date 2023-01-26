package com.gmfrontier.transaction_data.remote.dto

data class FraudInfo(
    val credit_card: CreditCard,
    val risk_score: Double
)