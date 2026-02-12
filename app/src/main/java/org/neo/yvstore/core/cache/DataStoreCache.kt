package org.neo.yvstore.core.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "yv_store_app_cache"
)

class DataStoreCache(private val context: Context) : AppCache {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun <T> saveObject(key: String, value: T, serializer: KSerializer<T>) {
        val jsonString = json.encodeToString(serializer, value)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = jsonString
        }
    }

    override suspend fun <T> getObject(key: String, serializer: KSerializer<T>): T? {
        return try {
            val jsonString = context.dataStore.data
                .map { preferences -> preferences[stringPreferencesKey(key)] }
                .first()

            jsonString?.let { json.decodeFromString(serializer, it) }
        } catch (e: SerializationException) {
            null
        }
    }

    override fun <T> observeObject(key: String, serializer: KSerializer<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            try {
                val jsonString = preferences[stringPreferencesKey(key)]
                jsonString?.let { json.decodeFromString(serializer, it) }
            } catch (e: SerializationException) {
                null
            }
        }
    }

    override suspend fun remove(key: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun contains(key: String): Boolean {
        return context.dataStore.data
            .map { preferences -> preferences.contains(stringPreferencesKey(key)) }
            .first()
    }
}
