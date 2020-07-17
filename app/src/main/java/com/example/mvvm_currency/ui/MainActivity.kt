package com.example.mvvm_currency.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_currency.CurrencyState
import com.example.mvvm_currency.R
import com.example.mvvm_currency.model.IRepository
import com.example.mvvm_currency.model.RepositoryImpl
import com.example.mvvm_currency.model.network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: IMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return modelClass.getConstructor(IRepository::class.java)
                    .newInstance(RepositoryImpl(NetworkService().getApiService()))
            }
        }).get(MainViewModel::class.java)

        acceptedButton.setOnClickListener {
            viewModel.fetchCurrency(baseCurrencyText.text.toString())
            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(baseCurrencyText.windowToken, 0)
        }

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                CurrencyState.Loading -> {
                    progressBar.visibility = View.VISIBLE

                    rubContainer.visibility = View.GONE
                    usdContainer.visibility = View.GONE
                    eurContainer.visibility = View.GONE
                    hkdContainer.visibility = View.GONE
                    textViewError.visibility = View.GONE
                }
                is CurrencyState.LoadInfo -> {
                    rubContainer.visibility = View.VISIBLE
                    usdContainer.visibility = View.VISIBLE
                    eurContainer.visibility = View.VISIBLE
                    hkdContainer.visibility = View.VISIBLE
                    textViewError.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    rub_value.text = state.currency.rub
                    usd_value.text = state.currency.usd
                    eur_value.text = state.currency.eur
                    hkd_value.text = state.currency.hkd

                }
                is CurrencyState.Error -> {
                    rubContainer.visibility = View.GONE
                    usdContainer.visibility = View.GONE
                    eurContainer.visibility = View.GONE
                    hkdContainer.visibility = View.GONE
                    textViewError.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    textViewError.text = state.error.message
                }
            }
        })

        viewModel.isEnabled.observe(this, Observer { isEnabled ->
            acceptedButton.isEnabled = isEnabled
        })
    }
}
