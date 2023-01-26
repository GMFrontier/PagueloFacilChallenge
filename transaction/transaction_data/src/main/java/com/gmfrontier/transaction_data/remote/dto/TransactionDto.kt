package com.gmfrontier.transaction_data.remote.dto

import com.squareup.moshi.Json


data class TransactionDto(
    @field:Json(name = "data")
    val transactions: List<Transaction>,
)