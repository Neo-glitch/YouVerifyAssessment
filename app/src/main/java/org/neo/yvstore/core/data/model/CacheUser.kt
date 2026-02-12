package org.neo.yvstore.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CacheUser(
    val uid: String,
    val email: String,
    val firstName: String,
    val lastName: String
)