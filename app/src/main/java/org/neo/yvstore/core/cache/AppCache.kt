package org.neo.yvstore.core.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Interface for application-level caching.
 * Supports storing and retrieving objects using Kotlinx Serialization.
 */
interface AppCache {
    /**
     * Saves an object to the cache with the given key.
     *
     * @param key The key to store the object under
     * @param value The object to store
     * @param serializer The serializer for the object type
     */
    suspend fun <T> saveObject(key: String, value: T, serializer: KSerializer<T>)

    /**
     * Retrieves an object from the cache with the given key.
     *
     * @param key The key of the object to retrieve
     * @param serializer The serializer for the object type
     * @return The object if found, null otherwise
     */
    suspend fun <T> getObject(key: String, serializer: KSerializer<T>): T?

    /**
     * Observes changes to an object in the cache with the given key.
     *
     * @param key The key of the object to observe
     * @param serializer The serializer for the object type
     * @return A Flow emitting the object whenever it changes, or null if not found
     */
    fun <T> observeObject(key: String, serializer: KSerializer<T>): Flow<T?>

    /**
     * Removes an object from the cache with the given key.
     *
     * @param key The key of the object to remove
     */
    suspend fun remove(key: String)

    /**
     * Clears all objects from the cache.
     */
    suspend fun clear()

    /**
     * Checks if the cache contains an object with the given key.
     *
     * @param key The key to check
     * @return true if the key exists in the cache, false otherwise
     */
    suspend fun contains(key: String): Boolean
}

/**
 * Extension function to save an object without explicitly passing a serializer.
 * Uses reified type parameter to automatically get the serializer.
 */
suspend inline fun <reified T> AppCache.saveObject(key: String, value: T) {
    saveObject(key, value, serializer())
}

/**
 * Extension function to retrieve an object without explicitly passing a serializer.
 * Uses reified type parameter to automatically get the serializer.
 */
suspend inline fun <reified T> AppCache.getObject(key: String): T? {
    return getObject(key, serializer())
}

/**
 * Extension function to observe an object without explicitly passing a serializer.
 * Uses reified type parameter to automatically get the serializer.
 */
inline fun <reified T> AppCache.observeObject(key: String): Flow<T?> {
    return observeObject(key, serializer())
}
