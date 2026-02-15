package org.neo.yvstore.features.auth.data.mapper

import org.neo.yvstore.features.auth.data.datasource.remote.model.UserDto
import org.neo.yvstore.core.domain.model.User

fun UserDto.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName
    )
}


