package fr.goodfood.entities

data class Restaurant(
    var id: Int = 0,
    var name: String,
    var shortDescription: String? = null,
    var description: String? = null,
    var address: Address? = null,
    var location: Location? = null,
    var deliveryRadius: Int? = null,
    var products: ArrayList<Product>
)

data class Location(var latitude: String, var longitude: String)

data class Product(
    var id: Int = 0,
    var category: String,
    var name: String,
    var description: String? = null,
    var price: Int? = null,
    var quantity: Int? = null
)