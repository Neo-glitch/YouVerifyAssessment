package org.neo.yvstore.features.auth.data.datasource.local

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.cache.AppCache
import org.neo.yvstore.core.cache.getObject
import org.neo.yvstore.core.cache.observeObject
import org.neo.yvstore.core.cache.saveObject
import org.neo.yvstore.core.data.model.CacheUser

class AuthLocalDatasourceImpl(
    private val appCache: AppCache
) : AuthLocalDatasource {

    companion object {
        const val KEY_AUTH_USER = "auth_user"
    }

    override suspend fun saveUser(user: CacheUser) {
        appCache.saveObject(KEY_AUTH_USER, user)
    }

    override suspend fun getUser(): CacheUser? {
        return appCache.getObject<CacheUser>(KEY_AUTH_USER)
    }

    override fun observeUser(): Flow<CacheUser?> {
        return appCache.observeObject<CacheUser>(KEY_AUTH_USER)
    }

    override suspend fun clearUser() {
        appCache.remove(KEY_AUTH_USER)
    }
}
