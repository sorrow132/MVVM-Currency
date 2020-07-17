package com.example.mvvm_currency.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_currency.CurrencyState
import com.example.mvvm_currency.model.CurrencyModel
import com.example.mvvm_currency.model.IRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

interface IMainViewModel {
    val state: LiveData<CurrencyState>

    val isEnabled: LiveData<Boolean>

    fun fetchCurrency(base: String)
}

class MainViewModel(private var issueRepository: IRepository) : ViewModel(), IMainViewModel {

    private val compositeDisposable = CompositeDisposable()
    private var fetchCurrencyDisposable: Disposable? = null

    override val state: MutableLiveData<CurrencyState> = MutableLiveData()

    override val isEnabled: MutableLiveData<Boolean> = MutableLiveData()

    override fun fetchCurrency(base: String) {
        fetchCurrencyDisposable?.dispose()
        val disposable = issueRepository
            .getInfo(base)
            .map { response ->
                CurrencyModel(
                    response.rates.rubVal.toString(),
                    response.rates.usdVal.toString(),
                    response.rates.eurVal.toString(),
                    response.rates.hkdVal.toString()
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                state.value = CurrencyState.Loading
                isEnabled.value = false
            }
            .subscribe({
                state.value = CurrencyState.LoadInfo(it)
                isEnabled.value = true
            }, {
                state.value = CurrencyState.Error(it)
                isEnabled.value = true
            })
        compositeDisposable.add(disposable)
        fetchCurrencyDisposable = disposable
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}