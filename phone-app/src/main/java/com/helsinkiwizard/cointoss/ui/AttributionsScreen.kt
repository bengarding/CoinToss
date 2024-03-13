package com.helsinkiwizard.cointoss.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.helsinkiwizard.cointoss.ui.model.AttributionModel
import com.helsinkiwizard.cointoss.ui.model.AttributionParams
import com.helsinkiwizard.core.theme.Eight
import com.helsinkiwizard.core.theme.Eighty
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.theme.Two

@Preview(showBackground = true)
@Composable
fun AttributionsScreen() {
    Surface {
        val context = LocalContext.current
        LazyColumn {
            val attributions = AttributionParams.attributions.sortedBy { context.getString(it.coin.nameRes) }
            items(attributions) { model ->
                AttributionItem(model)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun AttributionItem(model: AttributionModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = Twelve)
    ) {
        Image(
            painter = painterResource(model.coin.heads),
            contentDescription = null,
            modifier = Modifier
                .size(Eighty)
                .padding(top = Eight, end = Eight, bottom = Eight)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Two)
        ) {
            Text(
                text = stringResource(id = model.coin.nameRes),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${model.name} - ${model.source}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light
            )
        }
    }
}
