package com.helsinkiwizard.cointoss.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.ui.graphics.vector.ImageVector
import com.helsinkiwizard.cointoss.R

enum class ThemeMode(
    @StringRes val textRes: Int,
    val iconVector: ImageVector? = null,
    @DrawableRes val iconRes: Int? = null
) {
    LIGHT(R.string.theme_light, iconVector = Icons.Rounded.LightMode),
    DARK(R.string.theme_dark, iconVector = Icons.Rounded.DarkMode),
    SYSTEM(R.string.theme_system, iconRes = R.drawable.ic_day_night)
}
