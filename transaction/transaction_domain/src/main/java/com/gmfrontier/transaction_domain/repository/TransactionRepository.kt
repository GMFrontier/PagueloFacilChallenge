package com.gmfrontier.transaction_domain.repository

import com.gmfrontier.transaction_domain.model.TransactionModel

interface TransactionRepository {

    suspend fun searchTransactions(
        operationCode: String = "",
        offset: Int,
        token: String
    ): Result<List<TransactionModel>>

    suspend fun getTransactionsCount(token: String): Result<Int>
}

const val QUERY_PAGE_SIZE = 5
