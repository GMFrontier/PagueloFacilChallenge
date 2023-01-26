package com.gmfrontier.transaction_domain.model

const val CASH = "CASH"
const val MASTERCARD = "MC"

data class TransactionModel(
    val id: Int,
    val merchantName: String,
    val date: String,
    val codOper: String,
    val cardHolderName: String,
    val amount: Double,
    val taxAmount: Double,
    val status: String,
    val description: String,
    val cardType: String,
    val isVisible: Boolean = true
)