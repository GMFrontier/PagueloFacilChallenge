package com.gmfrontier.transaction_data.di

import com.gmfrontier.transaction_data.remote.TransactionAPI
import com.gmfrontier.transaction_data.repository.TransactionRepositoryImpl
import com.gmfrontier.transaction_data.util.TokenInterceptor
import com.gmfrontier.transaction_domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionDataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionAPI(client: OkHttpClient): TransactionAPI {
        return Retrofit.Builder()
            .baseUrl(TransactionAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        api: TransactionAPI,
    ): TransactionRepository {
        return TransactionRepositoryImpl(
            api = api
        )
    }
}