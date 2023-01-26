package com.gmfrontier.transaction_domain.use_case

import com.gmfrontier.transaction_domain.model.TransactionModel
import com.gmfrontier.transaction_domain.repository.TransactionRepository
import javax.inject.Inject

class SearchTransactions @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        offset: Int,
        operation: String = "",
        token: String
    ): Result<List<TransactionModel>> {
        return repository.searchTransactions(
            operationCode = operation,
            offset = offset,
            token = token
        )
    }
}