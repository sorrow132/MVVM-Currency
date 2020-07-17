package com.example.mvvm_currency

import com.example.mvvm_currency.model.CurrencyModel

sealed class CurrencyState {

    object Loading : CurrencyState()

    data class LoadInfo(
        val currency: CurrencyModel
    ) : CurrencyState()

    data class Error(
        val error: Throwable
    ) : CurrencyState()

}