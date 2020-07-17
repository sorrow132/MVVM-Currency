package com.example.mvvm_currency.model

import com.example.mvvm_currency.model.data.ExchangeResponse
import com.example.mvvm_currency.model.data.Rates
import com.example.mvvm_currency.model.network.CurrencyApiService
import io.reactivex.Single

interface IRepository {

    fun getInfo(base: String): Single<ExchangeResponse>
}

class RepositoryImpl(private val network: CurrencyApiService) : IRepository {

    override fun getInfo(base: String): Single<ExchangeResponse> {
        return network.requestCurrency(base)
    }
}

class NoInternetRepository() : IRepository {

    override fun getInfo(base: String): Single<ExchangeResponse> {
        return Single.just(ExchangeResponse(rates = Rates(1.0, 1.0, 1.0, 1.0)))
    }

}