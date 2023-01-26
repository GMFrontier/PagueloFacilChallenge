package com.gmfrontier.transaction_data.remote.dto

import com.squareup.moshi.Json

data class TransactionCountList(
    @field:Json(name = "data")
    val transactions: List<Int>,
)
