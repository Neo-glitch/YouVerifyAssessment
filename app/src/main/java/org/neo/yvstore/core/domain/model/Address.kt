package org.neo.yvstore.core.domain.model

data class Address(
    val id: String,
    val userId: String,
    val streetAddress: String,
    val city: String,
    val state: String,
    val country: String
)