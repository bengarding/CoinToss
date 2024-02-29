package com.helsinkiwizard.cointoss.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.wear.compose.material.Typography

// Set of Material typography styles to start with
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
