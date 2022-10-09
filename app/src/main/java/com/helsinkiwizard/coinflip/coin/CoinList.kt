package com.helsinkiwizard.coinflip.coin

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
import androidx.wear.tiles.TileService
import com.helsinkiwizard.coinflip.R
import com.helsinkiwizard.coinflip.Repository
import com.helsinkiwizard.coinflip.Repository.Companion.COIN_TYPE
import com.helsinkiwizard.coinflip.coin.CoinType.BITCOIN
import com.helsinkiwizard.coinflip.coin.CoinType.CANADA
import com.helsinkiwizard.coinflip.coin.CoinType.CHINA
import com.helsinkiwizard.coinflip.coin.CoinType.EURO
import com.helsinkiwizard.coinflip.coin.CoinType.INDIA
import com.helsinkiwizard.coinflip.coin.CoinType.JAPAN
import com.helsinkiwizard.coinflip.coin.CoinType.THAILAND
import com.helsinkiwizard.coinflip.coin.CoinType.UKRAINE
import com.helsinkiwizard.coinflip.coin.CoinType.UNITED_KINGDOM
import com.helsinkiwizard.coinflip.coin.CoinType.UNITED_STATES
import com.helsinkiwizard.coinflip.theme.ButtonHeight
import com.helsinkiwizard.coinflip.theme.HalfSpacing
import com.helsinkiwizard.coinflip.theme.PercentEighty
import com.helsinkiwizard.coinflip.theme.TextLarge
import com.helsinkiwizard.coinflip.tile.CoinTileService
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
@PreviewParameter(SampleCoinProvider::class)
fun CoinButton(coin: CoinType) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = Repository(context)

    Button(
        onClick = {
            scope.launch {
                dataStore.saveIntPreference(COIN_TYPE, coin.ordinal)
                TileService.getUpdater(context).requestUpdate(CoinTileService::class.java)
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