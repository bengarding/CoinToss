package com.helsinkiwizard.cointoss.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController

@Composable
fun <T> NavController.GetResult(key: String, onResult: (T) -> Unit) {
    val resultLiveData = currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
    resultLiveData?.observeAsState()?.value?.let {
        resultLiveData.value = null
        onResult(it)
    }
}
