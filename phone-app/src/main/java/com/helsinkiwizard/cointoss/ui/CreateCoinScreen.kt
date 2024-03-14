package com.helsinkiwizard.cointoss.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.ui.viewmodel.CreateCoinViewModel
import com.helsinkiwizard.core.theme.Forty
import com.helsinkiwizard.core.theme.PercentEighty
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.theme.Two
import com.helsinkiwizard.core.utils.sentenceCase

@Composable
fun CreateCoinScreen(
    navController: NavController,
    viewModel: CreateCoinViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.heads).sentenceCase(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = Twelve, top = Twelve, end = Twelve, bottom = Twelve)
        )
        AddCoin(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
private fun AddCoin(
    modifier: Modifier
) {
    val image = null
    Box(
        modifier = modifier
            .fillMaxWidth(PercentEighty)
            .aspectRatio(1f)
            .border(width = Two, color = MaterialTheme.colorScheme.primary, shape = CircleShape)
            .clip(CircleShape)
            .clickable {

            }
    ) {
        if (image != null) {
            Image(
                bitmap = image!!,
                contentDescription = null
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.AddCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(Forty)
                    .fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateCoinScreenPreview() {
    val navController = NavController(LocalContext.current)
    val repository = Repository(LocalContext.current)
    val viewModel = CreateCoinViewModel(repository)
    CoinTossTheme {
        Surface {
            CreateCoinScreen(navController, viewModel)
        }
    }
}
