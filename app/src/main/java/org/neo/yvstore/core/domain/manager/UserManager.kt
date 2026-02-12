package org.neo.yvstore.core.domain.manager

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.User

interface UserManager {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?

    fun observeUser() : Flow<User?>
    suspend fun clearUser()
}