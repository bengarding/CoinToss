package com.helsinkiwizard.coinflip

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Preferences(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
        val COIN_TYPE = intPreferencesKey("coin_type")
    }

    val getCoinType: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[COIN_TYPE] ?: CoinType.EURO.ordinal
        }

    suspend fun saveCoinType(type: Int) {
        context.dataStore.edit { preferences ->
            preferences[COIN_TYPE] = type
        }
    }
}