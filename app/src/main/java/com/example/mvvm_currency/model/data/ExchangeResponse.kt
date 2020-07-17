package com.example.mvvm_currency.model.data

import com.google.gson.annotations.SerializedName

data class ExchangeResponse(
    val rates: Rates
)

data class Rates(
    @SerializedName("RUB")
    val rubVal: Double,
    @SerializedName("USD")
    val usdVal: Double,
    @SerializedName("EUR")
    val eurVal: Double,
    @SerializedName("HKD")
    val hkdVal: Double
)