package com.gmfrontier.transaction_data.remote

import com.gmfrontier.transaction_data.remote.dto.TransactionCountList
import com.gmfrontier.transaction_data.remote.dto.TransactionDto
import retrofit2.http.*

interface TransactionAPI {

    @GET("/PFManagementServices/api/v1/MerchantTransactions")
    suspend fun searchTransactions(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("filter") filter: String,
        @Header("Authorization") token: String
    ): TransactionDto


    @GET("/PFManagementServices/api/v1/MerchantTransactions?filter=&field=idTransaction")
    suspend fun getTransactionsCount(
        @Header("Authorization") token: String
    ): TransactionCountList

    companion object {
        const val BASE_URL = "https://sandbox.paguelofacil.com/"
    }
}