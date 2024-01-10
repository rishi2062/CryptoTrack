package com.example.cryptotrack.ViewModel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.cryptotrack.Repository.GetCoin
import com.example.cryptotrack.handler.ResultHandler
import com.example.cryptotrack.model.CoinData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class CoinViewModel @Inject constructor(
    private val repo : GetCoin
) : ViewModel() {
    private var currPage = 0
    var currDate = mutableStateOf("")
    val coinList = mutableStateOf<List<CoinData>>(listOf())

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    private var searchedList = listOf<CoinData>()
    private var isStartingSearch = true
    var isSearching = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadCoinDetail()
    }

    fun searchList(query: String) {
        val listToSearch = if (isStartingSearch) {
            coinList.value
        } else {
            searchedList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                coinList.value = searchedList
                isSearching.value = false
                isStartingSearch = true
                return@launch
            } else {
                val results = listToSearch.filter {
                    it.name.contains(query.trim(), ignoreCase = true)
                }
                if (isStartingSearch) {
                    searchedList = coinList.value
                    isStartingSearch = false
                }
                coinList.value = results
                isSearching.value = true
            }
        }
    }

    fun loadCoinDetail() {

        viewModelScope.launch {

            isLoading.value = true
             when(val result = repo.getCoinData(currPage, 100)) {
                is ResultHandler.Success -> {
                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm")
                    currDate.value = sdf.format(Date())
                    Log.d("Refresh", "CALLED At ${currDate.value}")
                    endReached.value = currPage * 100 >= result.data!!.size
                    val coinEntries = result.data.mapIndexed { index, coins ->
                        val url = "https://assets.coinlayer.com/icons/${coins.symbol}.png"
                        CoinData(coins.symbol,coins.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                java.util.Locale.ROOT
                            ) else it.toString()
                        }, url)
                    }
                    currPage++
                    loadError.value = ""
                    isLoading.value = false
                    coinList.value = coinEntries
                }

                is ResultHandler.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                 is ResultHandler.Loading ->{
                     currDate.value = "Loading..."
                     //isLoading.value = true
                 }
            }
        }
    }
    fun calcBackGroundColor(drawable: Drawable, onFinish : (Color) -> Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888,true)
        Palette.from(bmp).generate{
            it?.dominantSwatch?.rgb?.let{colorVal ->
                onFinish(Color(colorVal))
            }
        }
    }
}

