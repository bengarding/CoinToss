package com.helsinkiwizard.core.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = Text16
    )
)

val LinkText: SpanStyle
    get() = SpanStyle(
        fontSize = Text12,
        color = LightBlue,
        textDecoration = TextDecoration.Underline,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal
    )
