package com.example.cryptotrack.ViewModel

import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptotrack.Repository.GetCoin
import com.example.cryptotrack.handler.ResultHandler
import com.example.cryptotrack.model.CoinData
import com.example.cryptotrack.model.CoinDetail
import com.example.cryptotrack.model.Rates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val repo: GetCoin
) : ViewModel() {
    private val _selectedText = MutableLiveData<String>("INR")
    val selectedText: LiveData<String> get() = _selectedText
   // LaunchedEffectHandler
   fun setSelectedText(text: String) {
        Log.d("TargetCrypto", "UPDATED ${text}")
        _selectedText.value = text

    }

    suspend fun loadCoinDetails(symbol : String) : ResultHandler<Double>? {
        Log.d("ANSWER", _selectedText.value + " " + symbol)
        return _selectedText.value?.let { repo.getCryptoData(it,symbol) }
    }
    }
