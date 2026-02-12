package org.neo.yvstore.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.cache.AppCache
import org.neo.yvstore.core.cache.KEY_USER
import org.neo.yvstore.core.cache.getObject
import org.neo.yvstore.core.cache.observeObject
import org.neo.yvstore.core.cache.saveObject
import org.neo.yvstore.core.data.mapper.toCacheUser
import org.neo.yvstore.core.data.mapper.toUser
import org.neo.yvstore.core.data.model.CacheUser
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.data.datasource.local.AuthLocalDatasourceImpl.Companion.KEY_AUTH_USER

class UserManagerImpl(
    private val appCache: AppCache
): UserManager {
    override suspend fun saveUser(user: User) {
        val cacheUser: CacheUser = user.toCacheUser()
        appCache.saveObject(KEY_USER, cacheUser)
    }

    override suspend fun getUser(): User? {
        val user = appCache.getObject<CacheUser>(KEY_USER)
        return user?.toUser()
    }

    override fun observeUser(): Flow<User?> {
        return appCache.observeObject<CacheUser>(KEY_USER).map { cachedUser ->
            cachedUser?.toUser()
        }.distinctUntilChanged()
    }

    override suspend fun clearUser() {
        appCache.remove(KEY_USER)
    }
}