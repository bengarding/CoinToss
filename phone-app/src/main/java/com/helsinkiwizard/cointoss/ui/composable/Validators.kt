package com.helsinkiwizard.cointoss.ui.composable

import com.helsinkiwizard.core.ui.model.MutableInputWrapper

fun xssValidator(): (MutableInputWrapper<String>) -> Boolean = { wrapper ->
    wrapper.value.contains("<").not() && wrapper.value.contains(">").not()
}