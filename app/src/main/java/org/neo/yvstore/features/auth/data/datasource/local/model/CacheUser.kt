package org.neo.yvstore.features.auth.data.datasource.local.model

import kotlinx.serialization.Serializable

@Serializable
data class CacheUser(
    val uid: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
