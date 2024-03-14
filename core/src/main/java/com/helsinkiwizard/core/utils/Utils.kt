package com.helsinkiwizard.core.utils

import android.content.Intent
import android.net.Uri

fun getEmailIntent(email: String): Intent {
    return Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    }
}
