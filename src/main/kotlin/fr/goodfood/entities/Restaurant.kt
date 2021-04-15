package fr.goodfood.entities

data class Restaurant(
    override var id: Int? = null,
    var name: String,
    var shortDescription: String? = null,
    var description: String? = null,
    var address: Address? = null,
    var location: Location? = null,
    var deliveryRadius: Int? = null,
    var products: ArrayList<Product>
): Identified(id)

data class Location(var latitude: String, var longitude: String)

data class Product(
    override var id: Int? = null,
    var category: String,
    var name: String,
    var description: String? = null,
    var price: Double,
    var quantity: Int
): Identified(id)