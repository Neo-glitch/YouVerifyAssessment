package org.neo.yvstore.features.auth.data.datasource.local

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.features.auth.data.datasource.local.model.CacheUser

interface AuthLocalDatasource {
    suspend fun saveUser(user: CacheUser)
    suspend fun getUser(): CacheUser?
    fun observeUser(): Flow<CacheUser?>
    suspend fun clearUser()
}
