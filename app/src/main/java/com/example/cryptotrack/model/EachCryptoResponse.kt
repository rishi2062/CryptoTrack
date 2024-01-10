package com.example.cryptotrack.model

data class EachCryptoResponse(
    val success : Boolean,
    val terms: String,
    val privacy : String,
    val timestamp : Long,
    val target:String,
    val rates: Map<String,Double>
)
