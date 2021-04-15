package fr.goodfood.entities

import fr.goodfood.Role

data class User(
    override var id: Int? = null,
    var role: Role? = null,
    var email: String,
    var password: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var address: Address? = null
): Identified(id)

data class Address(
    override var id: Int? = null,
    val number: String,
    val street: String,
    val addition: String? = null,
    val city: String,
    val postcode: String
): Identified(id)