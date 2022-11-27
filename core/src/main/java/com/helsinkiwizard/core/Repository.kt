package com.helsinkiwizard.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.helsinkiwizard.core.coin.CoinType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class BaseRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
        val COIN_TYPE = intPreferencesKey("coin_type")
    }

    val getCoinType: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[COIN_TYPE] ?: CoinType.BITCOIN.ordinal
        }

    suspend fun saveIntPreference(key: Preferences.Key<Int>, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
