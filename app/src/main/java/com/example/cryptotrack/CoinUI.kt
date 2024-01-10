package com.example.cryptotrack

//import com.google.accompanist.coil.CoilImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter
import com.example.cryptotrack.ViewModel.CoinViewModel
import com.example.cryptotrack.model.CoinData
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun CoinScreen(
    navController: NavController,
    viewModel: CoinViewModel = hiltViewModel()
){
    Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {
        Column{
            Spacer(modifier = Modifier.height(5.dp))
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                viewModel.searchList(it)
            }
            Spacer(modifier = Modifier.height(10.dp))
            ShowList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
){
    var text by remember{
        mutableStateOf("")
    }

    Box(modifier = modifier.padding(10.dp)) {

        Row {

            OutlinedTextField(
                value = text, onValueChange = {
                    text = it
                    onSearch(it)
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                },
                label = {
                    Text(text = "Search Coin", color = Color.Black)
                },
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.DarkGray
                ),
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .padding(1.dp)

            )
        }


    }

}


@Composable
fun ShowList(
    navController: NavController,
    viewModel: CoinViewModel = hiltViewModel()
) {

    val coinList by remember {
        viewModel.coinList
    }
    val loadError by remember {
        viewModel.loadError
    }
    val isLoading by remember {
        viewModel.isLoading
    }
    val isSearching by remember {
        viewModel.isSearching
    }
    val endReached by remember {
        viewModel.endReached
    }
    val time by remember{
        viewModel.currDate
    }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.loadCoinDetail()}
        ) {
        Box(modifier = Modifier.fillMaxWidth()
            .offset(x = -20.dp,y = (-20).dp),
            contentAlignment = Alignment.TopEnd){
            Text(
                text = "Last Updated : ${time}"
            )
        }
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            val itemCount = if (coinList.size % 2 == 0) {
                coinList.size / 2
            } else {
                coinList.size / 2 + 1
            }
            items(itemCount) {
                if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                    viewModel.loadCoinDetail()
                }
                CoinRow(idx = it, entries = coinList, navController = navController)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadCoinDetail()
            }
        }
    }
}
@Composable
fun EachCoinDetail(
    entry : CoinData,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CoinViewModel = hiltViewModel()
){
    val defaultColor = MaterialTheme.colorScheme.surface
    var backColor by remember {
        mutableStateOf(defaultColor)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(backColor, defaultColor)
                )
            )
            .clickable {
                navController.navigate(
                    "CoinsDetailUI/${backColor.toArgb()}/${entry.symbol}/${entry.name}"
                )
            }
    ){
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    model = entry.image,
                    onSuccess = {
                        val drawable = it.result.drawable
                        viewModel.calcBackGroundColor(drawable) { color ->
                            backColor = color
                        }

                    },

                    contentScale = ContentScale.Crop,

                    ),

                contentDescription = entry.name,
                modifier = modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
                    .padding(10.dp)
            )

            Text(
                text = entry.name,
                fontFamily = FontFamily.SansSerif,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun CoinRow(
    idx : Int,
    entries : List<CoinData>,
    navController: NavController
){
    Column{
        Row{
            EachCoinDetail(entry = entries[idx*2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= idx*2 + 2){
                EachCoinDetail(entry = entries[idx*2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onRetry()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry...")
        }
    }
}

