package com.helsinkiwizard.core.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.os.Parcelable
import timber.log.Timber
import java.io.IOException
import java.util.Locale

/**
 * Converts a string into sentence case (first word capitalized).
 */
fun String.sentenceCase(): String {
    return this.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

/**
 * Returns this string if it's not null or empty or the result of calling defaultValue function if the string
 * is null or empty.
 */
inline fun String?.ifNullOrEmpty(defaultValue: () -> String): String {
    return if (this.isNullOrEmpty()) defaultValue() else this
}

fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: IOException) {
        Timber.e(e, "toBitmap failed")
        null
    }
}

/**
 * Replaces [Intent.getParcelableExtra] to support deprecated method on devices running SDK 33 or higher
 * These functions may be unnecessary in the future: https://issuetracker.google.com/issues/242048899
 */
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

/**
 * Replaces [Bundle.getParcelable] to support deprecated method on devices running SDK 33 or higher
 */
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

/**
 * Replaces [Intent.getParcelableArrayListExtra] to support deprecated method on devices running SDK 33 or higher
 */
inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= TIRAMISU -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

/**
 * Replaces [Bundle.getParcelableArrayList] to support deprecated method on devices running SDK 33 or higher
 */
inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= TIRAMISU -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}
