package org.neo.yvstore.features.auth.data.mapper

import org.neo.yvstore.features.auth.data.datasource.remote.model.AuthUser
import org.neo.yvstore.features.auth.domain.model.User

/**
 * Mapper to convert remote AuthUser model to domain User model
 */
fun AuthUser.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName
    )
}
