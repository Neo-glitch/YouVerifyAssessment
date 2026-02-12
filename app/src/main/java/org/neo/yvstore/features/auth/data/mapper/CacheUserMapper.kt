package org.neo.yvstore.features.auth.data.mapper

import org.neo.yvstore.features.auth.data.datasource.local.model.CacheUser
import org.neo.yvstore.features.auth.domain.model.User

fun User.toCacheUser(): CacheUser {
    return CacheUser(
        uid = this.uid,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName
    )
}

fun CacheUser.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName
    )
}