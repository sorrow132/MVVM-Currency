package com.example.mvvm_currency.model.network

import com.example.mvvm_currency.model.data.ExchangeResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("/latest")
    fun requestCurrency(@Query("base") str: String): Single<ExchangeResponse>
}