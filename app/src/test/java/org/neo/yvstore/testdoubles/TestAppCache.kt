package org.neo.yvstore.testdoubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.neo.yvstore.core.cache.AppCache
import java.util.concurrent.ConcurrentHashMap

class TestAppCache : AppCache {
    private val storage = HashMap<String, String>()
    private val flows = HashMap<String, MutableSharedFlow<String?>>()
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun <T> saveObject(key: String, value: T, serializer: KSerializer<T>) {
        val jsonString = json.encodeToString(serializer, value)
        storage[key] = jsonString
        getOrCreateFlow(key).emit(jsonString)
    }

    override suspend fun <T> getObject(key: String, serializer: KSerializer<T>): T? {
        val jsonString = storage[key] ?: return null
        return json.decodeFromString(serializer, jsonString)
    }

    override fun <T> observeObject(key: String, serializer: KSerializer<T>): Flow<T?> {
        return getOrCreateFlow(key).map { jsonString ->
            jsonString?.let { json.decodeFromString(serializer, it) }
        }
    }

    override suspend fun remove(key: String) {
        storage.remove(key)
        getOrCreateFlow(key).emit(null)
    }

    override suspend fun clear() {
        val keys = storage.keys.toList()
        storage.clear()
        keys.forEach { getOrCreateFlow(it).emit(null) }
    }

    override suspend fun contains(key: String): Boolean {
        return storage.containsKey(key)
    }

    private fun getOrCreateFlow(key: String): MutableSharedFlow<String?> {
        return flows.getOrPut(key) {
            MutableSharedFlow<String?>(replay = 1).apply {
                tryEmit(storage[key])
            }
        }
    }
}
