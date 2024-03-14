package com.helsinkiwizard.cointoss.ui.drawer

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.helsinkiwizard.cointoss.navigation.NavRoute

class DrawerModel(
    val drawerOption: NavRoute,
    @StringRes val title: Int,
    val icon: ImageVector
)
