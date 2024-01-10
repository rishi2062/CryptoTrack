package com.example.cryptotrack.Api

import com.example.cryptotrack.model.CoinData
import com.example.cryptotrack.model.CoinDetail
import com.example.cryptotrack.model.Coins
import com.example.cryptotrack.model.CryptoResponse
import com.example.cryptotrack.model.EachCryptoResponse
import com.example.cryptotrack.model.ListCoins
import com.example.cryptotrack.model.Rates
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


const val BASE_URL = "http://api.coinlayer.com"
const val API_KEY = "1ed7208d0224e9b8f7ce9519b850281c"
interface coinService {
    @GET("/list?access_key=$API_KEY")
    suspend fun getCoinData() : Response<CryptoResponse>

    @GET("/live?access_key=$API_KEY")
    suspend fun getCoinIdData(
        @Query("target") target : String,@Query("symbols") symbols : String
    ) : Response<EachCryptoResponse>
}

//object coinObject{
//    val coinInstance : coinService
//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        coinInstance = retrofit.create(coinService::class.java)
//    }
//}