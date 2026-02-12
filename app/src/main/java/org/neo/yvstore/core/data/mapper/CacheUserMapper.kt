package org.neo.yvstore.core.data.mapper

import org.neo.yvstore.core.data.model.CacheUser
import org.neo.yvstore.core.domain.model.User

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