package fr.goodfood.controller

import fr.goodfood.ListParams
import fr.goodfood.entities.*
import io.javalin.core.validation.BodyValidator
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson
import java.util.*
import kotlin.math.cos
import kotlin.math.sqrt

object RestaurantController {

    private var id = 2
    private var productId = 1
    val mock = arrayListOf(
        Restaurant(
            id = 1,
            name = "EatSalad",
            description = "Salade sur mesure à emporter et en livraison",
            shortDescription = "Salade sur mesure à emporter et en livraison",
            address = Address(
                id = 1,
                number = "8",
                street = "Cours d'Albret",
                city = "Bordeaux",
                postcode = "33000"
            ),
            deliveryRadius = 30,
            location = Location("44.838717", "-0.581660"),
            products = arrayListOf()
        )
    )

    fun list(ctx: Context) {
        val params: ListParams = try {
            BodyValidator(JavalinJson.fromJson(ctx.body(), ListParams::class.java)).get()
        } catch (e: Exception) {
            ListParams()
        }

        ctx.json(mock.subList(params.offset, (params.offset + params.limit).coerceAtMost(mock.size)))
    }

    fun find(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val user = mock.find {
            it.id == id
        }

        if (user == null) {
            ctx.status(404)
        } else {
            ctx.json(user)
        }
    }

    data class RestaurantSearch(val search: String, val location: Location, val radius: Int)

    fun search(ctx: Context) {
        val search = ctx.body<RestaurantSearch>()

        mock.filter {
            val text = it.name.contains(search.search) ||
                    it.shortDescription?.contains(search.search) ?: false ||
                    it.description?.contains(search.search) ?: false ||
                    it.address?.street?.contains(search.search) ?: false ||
                    it.address?.city?.contains(search.search) ?: false ||
                    it.address?.postcode?.contains(search.search) ?: false;

            val location = it.location?.let { it1 ->
                distance(it1, search.location) <= search.radius
            }

            text || location ?: false
        }
    }

    fun create(ctx: Context) {
        val restaurant = ctx.body<Restaurant>()
        restaurant.id = id++

        mock.add(restaurant)

        ctx.status(200)
    }

    fun edit(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val existing = mock.find {
            it.id == id
        }

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
        val removed = mock.removeIf {
            it.id == id
        }

        if (removed) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addProduct(ctx: Context) {
        val id = ctx.pathParam("restaurant").toInt()
        val restaurant = mock.find {
            it.id == id
        }

        if (restaurant != null) {
            val product = ctx.body<Product>()
            product.id = productId++;

            restaurant.products.add(product)

            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun editProduct(ctx: Context) {
        val id = ctx.pathParam("restaurant").toInt()
        val restaurant = mock.find {
            it.id == id
        }

        if(restaurant !== null) {
            val productId = ctx.pathParam("product").toInt()
            val existing = restaurant.products.find {
                it.id == productId
            }

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
        val restaurant = mock.find {
            it.id == id
        }

        if(restaurant !== null) {
            val productId = ctx.pathParam("product").toInt()
            val removed = restaurant.products.removeIf {
                it.id == productId
            }

            if(removed) {
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