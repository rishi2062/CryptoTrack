package com.example.cryptotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "CoinsActivity") {
                composable("CoinsActivity") {
                    CoinScreen(navController = navController)
                }
                composable(
                    "CoinsDetailUI/{dominantColor}/{coinSymbol}/{coinName}",
                    arguments = listOf(
                        navArgument("dominantColor") {
                            type = NavType.IntType
                        },
                        navArgument("coinSymbol") {
                            type = NavType.StringType
                        },
                        navArgument("coinName") {
                            type = NavType.StringType
                        })

                ) {
                    val dominantColor = remember {
                        val color = it.arguments?.getInt("dominantColor")
                        color?.let { Color(it) } ?: Color.Gray
                    }
                    val coinDetails = remember {
                        it.arguments?.getString("coinSymbol")
                    }
                    val cryptoName = remember {
                        it.arguments?.getString("coinName")
                    }
                    CoinDetailUI(dominantColor = dominantColor,
                        coinSymbol = coinDetails?.toLowerCase(
                            Locale.ROOT
                        )?:"",
                        coinName = cryptoName!!,
                        navController = navController)
                }
            }
        }
    }
}

