package fr.goodfood.controller

import fr.goodfood.ListParams
import fr.goodfood.database.Database
import fr.goodfood.entities.*
import io.javalin.http.Context
import javalinjwt.JavalinJWT

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
        val raw = ctx.body<CreateOrder>()

        val email = JavalinJWT.getDecodedFromContext(ctx)
            .getClaim("email")
            .asString()

        val restaurant = Database.get<Restaurant>(ctx.pathParam("restaurant").toInt())
        val user = Database.first<User> {
            it.email == email
        }

        if (restaurant == null || user == null) {
            ctx.status(404)
            return
        }

        Database.insert(
            Order(
                type = raw.type,
                state = OrderState.PREPARING,
                restaurant = restaurant,
                user = user,
                address = user.address!!,
                products = raw.products.map {
                    Database.get<Product>(it)!!
                },
                time = "now"
            )
        )

        ctx.status(200)
    }

    fun edit(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val existing = Database.get<Order>(id)
        if (existing == null) {
            ctx.status(404)
            return
        }

        val order = ctx.body<EditOrder>()
        existing.type = order.type ?: existing.type
        existing.state = order.state ?: existing.state
        existing.products = order.products?.map {
            Database.get<Product>(it)!!
        } ?: existing.products

        ctx.status(200)
    }

}