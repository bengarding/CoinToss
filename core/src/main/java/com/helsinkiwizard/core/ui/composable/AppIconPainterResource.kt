package com.helsinkiwizard.core.ui.composable

import android.graphics.drawable.AdaptiveIconDrawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

@Composable
fun appIconPainterResource(@DrawableRes id: Int): Painter {
    val res = LocalContext.current.resources
    val theme = LocalContext.current.theme

    // Adaptive icons automatically conform to different device shapes (like circles, squares, or squircles) for a
    // consistent look across launchers
    val adaptiveIcon = ResourcesCompat.getDrawable(res, id, theme) as? AdaptiveIconDrawable
    return if (adaptiveIcon != null) {
        BitmapPainter(adaptiveIcon.toBitmap().asImageBitmap())
    } else {
        // Couldn't load the drawable as an Adaptive Icon, just use painterResource
        painterResource(id)
    }
}
