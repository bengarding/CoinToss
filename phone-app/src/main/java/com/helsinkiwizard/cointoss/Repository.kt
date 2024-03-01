package com.helsinkiwizard.cointoss

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.helsinkiwizard.core.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(private val context: Context) : BaseRepository(context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
        val TILE_RESOURCE_VERSION = intPreferencesKey("tile_resources_version")
    }

    val getResourceVersion: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[TILE_RESOURCE_VERSION] ?: 0
        }
}
