package com.gmfrontier.transaction_domain.use_case

import com.gmfrontier.transaction_domain.repository.TransactionRepository
import javax.inject.Inject


class GetTransactionsCount @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(token: String): Result<Int> {
        return repository.getTransactionsCount(token)
    }
}