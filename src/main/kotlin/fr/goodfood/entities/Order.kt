package fr.goodfood.entities

data class RawOrder(
    var restaurant: Int,
    var user: Int,
    var time: String,
    var address: Int,
    var products: List<Int>
)

data class Order(
    override var id: Int? = null,
    var restaurant: Restaurant,
    var user: User,
    var time: String,
    var address: Address,
    var products: List<Product>
): Identified(id)
