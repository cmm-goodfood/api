package fr.goodfood.controller

import fr.goodfood.ListParams
import fr.goodfood.database.Database
import fr.goodfood.entities.*
import io.javalin.http.Context

object OrderController {

    fun list(ctx: Context) {
        val params = ListParams.fromRequest(ctx)

        val restaurant = ctx.pathParam("restaurant").toInt()
        val items = Database.data<Order>().filter {
            it.restaurant.id == restaurant
        }

        ctx.json(items.subList(params.offset, (params.offset + params.limit).coerceAtMost(items.size)))
    }

    fun find(ctx: Context) {
        AbstractController.find<Order>(ctx)
    }

    fun create(ctx: Context) {
        val raw = ctx.body<RawOrder>()

        val restaurant = Database.get<Restaurant>(raw.restaurant)
        val user = Database.get<User>(raw.user)
        val address = Database.get<Address>(raw.address)

        if (restaurant == null || user == null || address == null) {
            ctx.status(400)
            return
        }

        Database.insert(Order(
            restaurant = restaurant,
            user = user,
            address = address,
            products = raw.products.map {
                Database.get<Product>(it)!!
            },
            time = "now"
        ))

        ctx.status(200)
    }

}