package fr.goodfood.entities

import fr.goodfood.Role

data class User(
    val role: Role,
    val email: String,
    val password: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: Address? = null
)

data class Address(
    val street: String
)