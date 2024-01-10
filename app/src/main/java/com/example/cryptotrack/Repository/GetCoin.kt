package com.example.cryptotrack.Repository

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cryptotrack.Api.coinService
import com.example.cryptotrack.handler.ResultHandler
import com.example.cryptotrack.model.CoinData
import com.example.cryptotrack.model.CoinDetail
import com.example.cryptotrack.model.Coins
import com.example.cryptotrack.model.ListCoins
import com.example.cryptotrack.model.Rates
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@ActivityScoped
class GetCoin @Inject constructor(private val api : coinService ) {
    private val coinsList = ListCoins()

    suspend fun getCoinData(page: Int, pageSize: Int): ResultHandler<ListCoins> {
        delay(100L)
        val startingIndex = page * pageSize
        try{
        val crypto = api.getCoinData()
        if(crypto.isSuccessful){
            val cryptoResponse = crypto.body()
            cryptoResponse?.let {
                val cryptoMap: Map<String, CoinData> = it.crypto
                for ((key, cryptoData) in cryptoMap) {
                    val cryptoCoin = Coins(cryptoData.symbol,cryptoData.name)
                    coinsList.add(cryptoCoin)
                    Log.d("CryptoInfo", "Symbol: ${cryptoData.symbol}, Name: ${cryptoData.name}")
                }
            }
        } else {
            Log.e("ApiError", "Error: ${crypto.code()}")
            return ResultHandler.Error("Error in fetching request....", null)
        }
    } catch (e: Exception) {
        Log.e("ApiError", "Exception: ${e.message}")
            return ResultHandler.Error("Error in fetching request....", null)
    }

        if (startingIndex + pageSize <= coinsList.size) {
            coinsList.slice(startingIndex until startingIndex + pageSize)
            return ResultHandler.Success(coinsList)
        }
        return ResultHandler.Success(coinsList.subList(0, 0) as ListCoins)
    }

    suspend fun getCryptoData(target:String,symbol : String) : ResultHandler<Double> {

        try{
            Log.d("STEPS","Initial STEP1 " + target)
            val response = api.getCoinIdData(target,symbol)
            Log.d("STEPS","STEP0")
            if(response.isSuccessful){
                Log.d("STEPS","STEP1")
                val cryptoResponse = response.body()
                Log.d("STEPS","STEP2")
                cryptoResponse?.let {
                    Log.d("STEPS","STEP3")
                    val cryptoMap = it.rates
                    Log.d("STEPS","STEP4")
                    val ratesList = mutableListOf<Rates>()
                    var doubleRate = 0.0
                    for ((key, cryptoDetail) in cryptoMap) {
                        Log.d("RATESS", cryptoDetail.toString())
                        doubleRate = cryptoDetail

                    }
                    return ResultHandler.Success(doubleRate)
                }
            }else {
                Log.e("ApiError", "Error: ${response.code()}")
                return ResultHandler.Error("Error in fetching request....", null)
            }
        }catch(e:Exception){
            Log.d("Error in Coin List" , e.message!!)
            return ResultHandler.Error("Error in getting CoinDetails of $symbol",null)
        }
        return ResultHandler.Error("Not Found")
    }
}