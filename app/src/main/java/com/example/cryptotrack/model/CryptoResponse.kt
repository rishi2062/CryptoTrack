package com.example.cryptotrack.model

data class CryptoResponse(
    val success: Boolean,
    val crypto: Map<String, CoinData>,
    val fiat: Map<String, String>
)

