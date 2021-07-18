package fr.goodfood.entities

data class Restaurant(
    override var id: Int? = null,
    var name: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var address: Address? = null,
    var location: Location? = null,
    var deliveryRadius: Int? = null,
    var products: ArrayList<Product>? = ArrayList()
): Identified(id)

data class Location(var latitude: String, var longitude: String)

enum class ProductCategory {
    APPETIZER, DISH, DESERT, DRINK
}

data class Product(
    override var id: Int? = null,
    var category: ProductCategory? = null,
    var name: String? = null,
    var description: String? = null,
    var price: Double? = null,
    var stock: Int? = null
): Identified(id)