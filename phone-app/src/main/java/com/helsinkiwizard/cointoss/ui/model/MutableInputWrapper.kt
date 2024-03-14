package com.helsinkiwizard.cointoss.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.helsinkiwizard.core.CoreConstants.VALUE_UNDEFINED

/**
 * Used for jetpack compose input fields to store state information.
 * When setting a validator, it should return true when [value] is valid.
 *
 * @param initialValue initial value
 * @param errorTextRes optional inline error message text resource
 * @param data any extra data that should be held by this wrapper
 */
class MutableInputWrapper<T>(
    val initialValue: T,
    initialVisibility: Boolean = true,
    errorTextRes: Int = VALUE_UNDEFINED,
    var data: Any? = null
) {
    var value by mutableStateOf(initialValue)
    var isError by mutableStateOf(false)
    var isVisible by mutableStateOf(initialVisibility)
    var errorTextRes by mutableIntStateOf(errorTextRes)

    var validator: ((MutableInputWrapper<T>) -> Boolean)? = null
    fun validate() {
        isError = validator?.invoke(this) == false
    }

    fun isValid(): Boolean {
        return validator == null || validator?.invoke(this) == true
    }
}
