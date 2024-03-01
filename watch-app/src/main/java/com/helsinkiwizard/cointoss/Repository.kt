package com.helsinkiwizard.cointoss

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import com.helsinkiwizard.core.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class Repository(context: Context) : BaseRepository(context) {

    companion object {
        val TILE_RESOURCE_VERSION = intPreferencesKey("tile_resources_version")
    }

    val getResourceVersion: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[TILE_RESOURCE_VERSION] ?: 0
        }
}
