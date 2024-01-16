package com.helsinkiwizard.cointoss.coin

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.tiles.TileService
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.firebase.analytics.FirebaseAnalytics
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.Repository.Companion.COIN_TYPE
import com.helsinkiwizard.cointoss.coin.CoinType.BITCOIN
import com.helsinkiwizard.cointoss.theme.BlackTransparent
import com.helsinkiwizard.cointoss.theme.ButtonHeight
import com.helsinkiwizard.cointoss.theme.Eight
import com.helsinkiwizard.cointoss.theme.Four
import com.helsinkiwizard.cointoss.theme.PercentEighty
import com.helsinkiwizard.cointoss.theme.Text16
import com.helsinkiwizard.cointoss.theme.Text20
import com.helsinkiwizard.cointoss.theme.Thirty
import com.helsinkiwizard.cointoss.theme.Twelve
import com.helsinkiwizard.cointoss.tile.CoinTileService
import com.helsinkiwizard.cointoss.utils.buildTextWithLink
import com.helsinkiwizard.cointoss.utils.onLinkClick
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun CoinList(
    analytics: FirebaseAnalytics?,
    onEmailClick: (Intent) -> Unit
) {
    val listState = rememberScalingLazyListState()
    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        val focusRequester = rememberActiveFocusRequester()
        val coroutineScope = rememberCoroutineScope()

        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    // https://developer.android.com/training/wearables/compose/rotary-input
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable()
        ) {
            item { ListTitle() }
            items(CoinType.entries) { item ->
                CoinButton(
                    coin = item,
                    analytics = analytics
                )
            }
            item { RequestCoin(onEmailClick) }
        }
    }
}

@Composable
fun ListTitle() {
    Text(
        text = stringResource(id = R.string.choose_a_coin),
        fontSize = Text20,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(bottom = Eight)
            .fillMaxWidth(PercentEighty)
    )
}

@Composable
fun CoinButton(
    coin: CoinType,
    analytics: FirebaseAnalytics? // nullable for preview
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = Repository(context)

    Button(
        onClick = {
            scope.launch {
                val name = coin.name.lowercase().replaceFirstChar { it.titlecase() }
                val params = Bundle().apply {
                    putString(FirebaseAnalytics.Param.CONTENT_TYPE, name)
                }
                analytics?.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, params)
                dataStore.saveIntPreference(COIN_TYPE, coin.value)
                TileService.getUpdater(context).requestUpdate(CoinTileService::class.java)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(ButtonHeight)
    ) {
        Box {
            Image(
                painter = painterResource(id = coin.heads),
                contentDescription = stringResource(id = coin.nameRes),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(id = coin.nameRes),
                fontWeight = FontWeight.Normal,
                fontSize = Text16,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = BlackTransparent, shape = CircleShape)
                    .padding(vertical = Four, horizontal = Twelve)
            )
        }
    }
}

@Composable
private fun RequestCoin(onEmailClick: (Intent) -> Unit) {
    val emailAddress = stringResource(id = R.string.email_address)
    val annotatedString = buildTextWithLink(
        fullText = stringResource(id = R.string.request_coin),
        linkText = emailAddress
    )
    ClickableText(
        text = annotatedString,
        modifier = Modifier.padding(vertical = Thirty, horizontal = Twelve),
        onClick = { offset ->
            annotatedString.onLinkClick(
                offset = offset,
                onClick = {
                    onEmailClick(getEmailIntent(emailAddress))
                }
            )
        }
    )
}

private fun getEmailIntent(email: String): Intent {
    return Intent(ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
        putExtra(EXTRA_EMAIL, arrayOf(email))
        putExtra(EXTRA_SUBJECT, "Subject") // You can customize the subject
        putExtra(EXTRA_TEXT, "Body") // You can customize the email body
    }
}

@Preview(name = "large round", device = WearDevices.LARGE_ROUND)
@Preview(name = "square", device = WearDevices.SQUARE)
@Composable
private fun CoinButtonPreview() {
    CoinButton(coin = BITCOIN, analytics = null)
}

@Preview(name = "large round", device = WearDevices.LARGE_ROUND)
@Preview(name = "square", device = WearDevices.SQUARE)
@Composable
private fun CoinListPreview() {
    CoinList(analytics = null, {})
}
