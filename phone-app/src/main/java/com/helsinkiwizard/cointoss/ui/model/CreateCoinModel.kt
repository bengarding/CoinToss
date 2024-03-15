package com.helsinkiwizard.cointoss.ui.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CreateCoinModel {
    var headsUri by mutableStateOf<Uri?>(null)
    val tailsUri  by mutableStateOf<Uri?>(null)
}
