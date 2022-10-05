package com.helsinkiwizard.coinflip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import com.helsinkiwizard.coinflip.CoinType.BITCOIN
import com.helsinkiwizard.coinflip.CoinType.CANADA
import com.helsinkiwizard.coinflip.CoinType.CHINA
import com.helsinkiwizard.coinflip.CoinType.EURO
import com.helsinkiwizard.coinflip.CoinType.INDIA
import com.helsinkiwizard.coinflip.CoinType.JAPAN
import com.helsinkiwizard.coinflip.CoinType.THAILAND
import com.helsinkiwizard.coinflip.CoinType.UKRAINE
import com.helsinkiwizard.coinflip.CoinType.UNITED_KINGDOM
import com.helsinkiwizard.coinflip.CoinType.UNITED_STATES
import com.helsinkiwizard.coinflip.theme.ButtonHeight
import com.helsinkiwizard.coinflip.theme.HalfSpacing
import com.helsinkiwizard.coinflip.theme.PercentEighty
import com.helsinkiwizard.coinflip.theme.TextLarge
import kotlinx.coroutines.launch

@Preview
@Composable
fun CoinList() {
    val list = listOf(
        BITCOIN,
        CANADA,
        CHINA,
        EURO,
        INDIA,
        JAPAN,
        THAILAND,
        UKRAINE,
        UNITED_KINGDOM,
        UNITED_STATES
    )

    ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
        item { ListTitle() }
        items(list) { item ->
            CoinButton(coin = item)
        }
    }
}

@Composable
fun ListTitle() {
    Text(
        text = stringResource(id = R.string.choose_a_coin),
        fontSize = TextLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(bottom = HalfSpacing)
            .fillMaxWidth(PercentEighty)
    )
}

@Composable
fun CoinButton(@PreviewParameter(SampleCoinProvider::class) coin: CoinType) {
    val scope = rememberCoroutineScope()
    val dataStore = Preferences(LocalContext.current)

    Button(
        onClick = {
            scope.launch {
                dataStore.saveCoinType(coin.ordinal)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(ButtonHeight)
    ) {
        Image(
            painter = painterResource(id = coin.heads),
            contentDescription = stringResource(id = coin.contentDesc),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
    }
}

class SampleCoinProvider : PreviewParameterProvider<CoinType> {
    override val values = sequenceOf(EURO)
}