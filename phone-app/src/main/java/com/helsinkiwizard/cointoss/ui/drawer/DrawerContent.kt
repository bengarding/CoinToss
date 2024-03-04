package com.helsinkiwizard.cointoss.ui.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.core.theme.Sixteen
import com.helsinkiwizard.core.theme.Sixty
import com.helsinkiwizard.core.theme.Twenty
import kotlinx.coroutines.launch

object DrawerParams {
    val drawerButtons = arrayListOf(
        DrawerModel(
            NavRoute.Home,
            R.string.home,
            Icons.Rounded.Home
        ),
        DrawerModel(
            NavRoute.CoinList,
            R.string.choose_a_coin,
            Icons.Outlined.AccountCircle
        ),
        DrawerModel(
            NavRoute.Settings,
            R.string.settings,
            Icons.Rounded.Settings
        )
    )
}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    menuItems: List<DrawerModel>,
    defaultPick: NavRoute,
    onClick: (NavRoute) -> Unit
) {
    var currentPick by remember { mutableStateOf(defaultPick) }
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = Sixteen, horizontal = Twenty)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer)

            menuItems.forEach { item ->
                DrawerItem(item = item) { navOption ->

                    if (currentPick == navOption) {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        return@DrawerItem
                    }

                    currentPick = navOption
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    onClick(navOption)
                }
            }
        }
    }
}

@Composable
fun DrawerItem(item: DrawerModel, onClick: (options: NavRoute) -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .fillMaxWidth()
                .clickable { onClick(item.drawerOption) }
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(Sixty)
                    .padding(horizontal = Sixteen)
            )
            Text(
                text = stringResource(id = item.title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}