package fr.goodfood.entities

import fr.goodfood.Role

data class User(
    var id: Int = 0,
    var role: Role? = null,
    var email: String,
    var password: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var address: Address? = null
)

data class Address(
    val id: Int = 0,
    val number: String,
    val street: String,
    val addition: String? = null,
    val city: String,
    val postcode: String
)