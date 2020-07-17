package com.example.mvvm_currency.model.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkService {
    private val logger =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val httpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(logger)
            .writeTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)

    private val service: CurrencyApiService = Retrofit
            .Builder()
            .client(httpClient.build())
            .baseUrl("https://api.exchangeratesapi.io")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CurrencyApiService::class.java)

    fun getApiService(): CurrencyApiService {
        return service
    }
}