package com.helsinkiwizard.core.utils

import java.util.Locale

/**
 * Converts a string into sentence case (first word capitalized).
 */
fun String.sentenceCase(): String {
    return this.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
