package fr.goodfood.entities

enum class OrderType {
    DELIVER, TAKEAWAY
}

enum class OrderState {
    PREPARING, PREPARED, DELIVERING, TAKEN, DELIVERED, CANCELLED
}

data class CreateOrder(
    var type: OrderType,
    var products: List<Int>
)

data class EditOrder(
    var type: OrderType? = null,
    var state: OrderState? = null,
    var products: List<Int>? = null
)

data class Order(
    override var id: Int? = null,
    var type: OrderType,
    var state: OrderState,
    var restaurant: Restaurant,
    var user: User,
    var time: String,
    var address: Address,
    var products: List<Product>
) : Identified(id)
