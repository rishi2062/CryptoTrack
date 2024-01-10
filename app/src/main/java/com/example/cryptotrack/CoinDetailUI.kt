package com.example.cryptotrack

import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.cryptotrack.ViewModel.CoinDetailsViewModel
import com.example.cryptotrack.handler.ResultHandler
import com.example.cryptotrack.model.CoinData
import com.example.cryptotrack.model.CoinDetail
import com.example.cryptotrack.model.Coins
import com.example.cryptotrack.model.Rates
import kotlinx.coroutines.CoroutineScope
import java.util.Locale


//lateinit var target : String
@Composable
fun CoinDetailUI(
    dominantColor : androidx.compose.ui.graphics.Color,
    coinSymbol : String,
    coinName : String,
    navController: NavController,
    coinImageSize : Dp = 150.dp,
    topPadding : Dp = 20.dp,
    viewModel: CoinDetailsViewModel = hiltViewModel()
){

    val coinInfo = produceState<ResultHandler<Double>>(initialValue = ResultHandler.Loading()){
        //Log.d("TargetCrypto--")
        value = viewModel.loadCoinDetails(coinSymbol)!!
    }.value
    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
        .padding(bottom = 16.dp)){

            BackGroundSection(navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .align(Alignment.TopCenter))

            CoinDetailsState(
                coinName = coinName,
                coinSymbol =coinSymbol,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        top = topPadding + coinImageSize / 2f,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                    .shadow(10.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                loadingModifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .padding(
                        start = 16.dp,
                        top = topPadding + coinImageSize / 2f,
                        end = 16.dp,
                        bottom = 16.dp
                    )
            )

            Box(contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                .fillMaxSize()){
                val coinImage = "https://assets.coinlayer.com/icons/${coinSymbol.toUpperCase(Locale.ROOT)}.png"
                AsyncImage(model = coinImage, contentDescription = "Logo", modifier = Modifier
                    .size(coinImageSize)
                    .offset(y = topPadding)
                    .clip(CircleShape))
            }
//        Demo_ExposedDropdownMenuBox(coinName = coinName,coinSymbol,viewModel)
//        coinInfo.data?.let { CoinDetailBox(coin = it) }
    }
}

@Composable
fun BackGroundSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(androidx.compose.ui.graphics.Color.Black, androidx.compose.ui.graphics.Color.Transparent)
                    )
                )
            ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun CoinDetailsState(
    coinName : String,
    coinSymbol: String,
    viewModel: CoinDetailsViewModel = hiltViewModel(),
    modifier : Modifier = Modifier,
    loadingModifier : Modifier = Modifier
) {
    Demo_ExposedDropdownMenuBox(coinName = coinName,coinSymbol,viewModel,modifier)
}

@Composable
fun CoinDetailBox(
    coin : Double,
    coinName : String,
    viewModel: CoinDetailsViewModel = hiltViewModel(),
    modifier : Modifier = Modifier
){
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ){

        Text(
            text = "Name :   $coinName",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,

        )
        Text(
            text = "Target : in Rs : ${coin}",

        )
//        Demo_ExposedDropdownMenuBox(coinName,viewModel)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox(coinName : String,coinSymbol : String,
                                viewModel: CoinDetailsViewModel = hiltViewModel(),modifier: Modifier = Modifier) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val target = listOf("AED	United Arab Emirates Dirham",
        "AFN Afghan Afghani",
        "ALL Albanian Lek",
        "AMD	Armenian Dram",
        "KRW	South Korean Won",
        "KWD	Kuwaiti Dinar",
        "KYD	Cayman Islands Dollar",
        "KZT	Kazakhstani Tenge",
        "LKR	Sri Lankan Rupee",
        "LRD	Liberian Dollar",
        "LSL	Lesotho Loti",
        "LTL	Lithuanian Litas",
        "LVL	Latvian Lats",
        "LYD	Libyan Dinar",
        "INR	Indian Rupee",
        "IQD	Iraqi Dinar",
                "IRR	Iranian Rial",
                "ISK	Icelandic Króna",
                "JEP	Jersey Pound",
                "JMD	Jamaican Dollar",
                "JOD	Jordanian Dinar",
                "JPY	Japanese Yen",
                "KES	Kenyan Shilling",
                "KGS	Kyrgystani Som",
                "KHR	Cambodian Riel",
                "KMF	Comorian Franc",
                "KPW	North Korean Won",
        "MAD	Moroccan Dirham",
        "TJS	Tajikistani Somoni",
        "TMT	Turkmenistani Manat",
        "TND	Tunisian Dinar",
        "TOP	Tongan Paʻanga",
        "TRY	Turkish Lira",
        "TTD	Trinidad and Tobago Dollar",
        "TWD	New Taiwan Dollar",
        "TZS	Tanzanian Shilling",
        "UAH	Ukrainian Hryvnia",
        "UGX	Ugandan Shilling",
        "USD	United States Dollar",
        "UYU	Uruguayan Peso",
        "UZS	Uzbekistan Som",
        "VEF	Venezuelan Bolívar Fuerte",
        "VND	Vietnamese Dong",
        "VUV	Vanuatu Vatu",
        "WST	Samoan Tala",
        "XAF	CFA Franc BEAC",
        "XAG	Silver (troy ounce)",
        "XAU	Gold (troy ounce)",
        "XCD	East Caribbean Dollar",
        "XDR	Special Drawing Rights",
        "XOF	CFA Franc BCEAO",
        "XPF	CFP Franc",
        "YER	Yemeni Rial",
        "ZAR	South African Rand",
        "ZMK	Zambian Kwacha (pre-2013)",
        "ZMW	Zambian Kwacha",
        "ZWL	Zimbabwean Dollar)")
    var selectedText by remember {
        mutableStateOf(target[0])
    }

    Box(
        modifier = Modifier
            . fillMaxWidth ()
            .offset(x = 0.dp, y = 420.dp)
            .padding(22.dp)
            .background(androidx.compose.ui.graphics.Color.White),

        contentAlignment = Alignment.BottomCenter,

        ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            Text(
                text = selectedText,
                modifier = Modifier
                    .menuAnchor()
                    .offset(x = (-10).dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.align(Alignment.Center).fillMaxWidth(1f)

            ) {
                target.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            viewModel.setSelectedText(selectedText.substring(0..2))
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    var ans by remember { mutableStateOf("")}
    val selectedTextState by viewModel.selectedText.observeAsState()
    LaunchedEffect(selectedTextState) {
        selectedTextState?.let {
            val result = viewModel.loadCoinDetails(coinSymbol)
            if (result != null) {
                ans = result.data.toString()
            }

        }
    }
    Text(text = "NAME : ${coinName}", fontSize = 20.sp, modifier = Modifier.
    padding(top = 300.dp, start = 20.dp))
    Text(
        text = "Target in ${selectedText} : ${ans}", fontSize = 20.sp, modifier = Modifier.
        padding(top = 335.dp, start = 20.dp)
        )

}
