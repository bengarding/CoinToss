package com.helsinkiwizard.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.helsinkiwizard.cointoss.theme.Text12
import com.helsinkiwizard.core.theme.LinkText
import com.helsinkiwizard.core.theme.Typography

@OptIn(ExperimentalTextApi::class) // UrlAnnotation
@Composable
fun buildTextWithLink(
    fullText: String,
    linkText: String,
    style: SpanStyle = Typography.body1.copy(fontSize = Text12).toSpanStyle(),
    linkStyle: SpanStyle = LinkText
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style) {
            append(fullText)
        }

        val startIndex = fullText.indexOf(linkText)
        if (startIndex != -1) {
            val endIndex = startIndex + linkText.length
            addStyle(linkStyle, startIndex, endIndex)
            addStringAnnotation(linkText, linkText, startIndex, endIndex)
            addUrlAnnotation(UrlAnnotation(linkText), startIndex, endIndex)
        }
    }
}

fun AnnotatedString.onLinkClick(uri: String = "", offset: Int, onClick: (String) -> Unit) {
    if (uri.isNotEmpty()) {
        getStringAnnotations(tag = uri, start = offset, end = offset).firstOrNull()?.let {
            onClick(it.item)
        }
    } else {
        getStringAnnotations(start = offset, end = offset).firstOrNull()?.let {
            onClick(it.item)
        }
    }
}
