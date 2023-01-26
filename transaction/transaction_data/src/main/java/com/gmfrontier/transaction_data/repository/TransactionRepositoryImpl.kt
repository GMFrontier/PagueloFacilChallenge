package com.gmfrontier.transaction_data.repository

import com.gmfrontier.transaction_data.remote.TransactionAPI
import com.gmfrontier.transaction_data.mapper.toUiTransactionModel
import com.gmfrontier.transaction_domain.model.TransactionModel
import com.gmfrontier.transaction_domain.repository.QUERY_PAGE_SIZE
import com.gmfrontier.transaction_domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val api: TransactionAPI
) : TransactionRepository {
    override suspend fun searchTransactions(
        operationCode: String,
        offset: Int,
        token: String
    ): Result<List<TransactionModel>> {
        return try {
            val filter = "codOper::$operationCode"
            val transactionDto = api.searchTransactions(
                limit = QUERY_PAGE_SIZE,
                offset = offset,
                filter = filter,
                token = token
            )
            Result.success(
                transactionDto.transactions
                    .map { it.toUiTransactionModel() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getTransactionsCount(token: String): Result<Int> {
        return try {
            val count = api.getTransactionsCount(token).transactions.count()
            Result.success(count)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}