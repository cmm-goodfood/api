package fr.goodfood.controller

import fr.goodfood.database.Database
import fr.goodfood.entities.*
import io.javalin.http.Context
import kotlin.math.cos
import kotlin.math.sqrt

object RestaurantController {

    fun list(ctx: Context) {
        AbstractController.list<Restaurant>(ctx)
    }

    fun find(ctx: Context) {
        AbstractController.find<Restaurant>(ctx)
    }

    data class RestaurantSearch(val search: String, val location: Location, val radius: Int)

    fun search(ctx: Context) {
        val search = ctx.body<RestaurantSearch>()

        val matching = Database.filter<Restaurant> {
            val text = it.name.contains(search.search) ||
                    it.shortDescription?.contains(search.search) ?: false ||
                    it.description?.contains(search.search) ?: false ||
                    it.address?.street?.contains(search.search) ?: false ||
                    it.address?.city?.contains(search.search) ?: false ||
                    it.address?.postcode?.contains(search.search) ?: false

            val location = it.location?.let { it1 ->
                distance(it1, search.location) <= search.radius
            }

            text || location ?: false
        }

        ctx.json(matching)
    }

    fun create(ctx: Context) {
        Database.insert(ctx.body<Restaurant>())

        ctx.status(200)
    }

    fun edit(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val existing = Database.get<Restaurant>(id)

        if (existing == null) {
            ctx.status(404)
            return
        }

        val restaurant = ctx.body<Restaurant>()
        existing.name = restaurant.name
        existing.shortDescription = restaurant.shortDescription
        existing.description = restaurant.description
        existing.address = existing.address
        existing.deliveryRadius = restaurant.deliveryRadius
        existing.location = restaurant.location

        ctx.status(200)
    }

    fun delete(ctx: Context) {
        val id = ctx.pathParam("id").toInt()

        if (Database.remove<Restaurant>(id)) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addProduct(ctx: Context) {
        val id = ctx.pathParam("restaurant").toInt()
        val restaurant = Database.get<Restaurant>(id)

        if (restaurant != null) {
            val product = ctx.body<Product>()
            restaurant.products.add(product)

            Database.insert(product)

            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun editProduct(ctx: Context) {
        val id = ctx.pathParam("restaurant").toInt()
        val restaurant = Database.get<Restaurant>(id)

        if(restaurant !== null) {
            val productId = ctx.pathParam("product").toInt()
            val existing = Database.get<Product>(productId)

            if (existing == null) {
                ctx.status(404)
                return
            }

            val product = ctx.body<Product>()
            existing.category = product.category
            existing.name = product.name
            existing.description = product.description
            existing.price = product.price
            existing.quantity = product.quantity

            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun deleteProduct(ctx: Context) {
        val id = ctx.pathParam("restaurant").toInt()
        val restaurant = Database.get<Restaurant>(id)

        if(restaurant !== null) {
            val productId = ctx.pathParam("product").toInt()

            if(Database.remove<Product>(productId)) {
                restaurant.products.removeIf {
                    it.id == productId
                }

                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    private fun distance(a: Location, b: Location): Double {
        val lat1 = a.latitude.toDouble()
        val lat2 = b.latitude.toDouble()

        val lon1 = a.longitude.toDouble()
        val lon2 = b.longitude.toDouble()

        val x: Double = (lon2 - lon1) * cos((lat1 + lat2) / 2)
        val y: Double = lat2 - lat1

        return sqrt(x * x + y * y) * 6371
    }

}