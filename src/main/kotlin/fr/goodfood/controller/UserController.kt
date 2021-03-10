package fr.goodfood.controller

import fr.goodfood.ListParams
import fr.goodfood.Role
import fr.goodfood.entities.User
import io.javalin.http.Context
import java.util.*

object UserController {

    private val mock = arrayListOf(
        User(email = "clement.curiel@viacesi.fr", firstname = "Clément", lastname = "CURIEL", role = Role.RESTAURANT),
        User(email = "matteo.hevin@viacesi.fr", firstname = "Matteo", lastname = "HEVIN", role = Role.DELIVERY),
        User(email = "pierre.pegeon@viacesi.fr", firstname = "Pierre", lastname = "PEGEON", role = Role.MODERATOR),
        User(email = "marwane.benoukaiss@viacesi.fr", firstname = "Marwane", lastname = "BENOU-KAÏSS", role = Role.USER)
    )

    fun list(ctx: Context) {
        val params = ctx.body<Optional<ListParams>>().orElse(ListParams())

        ctx.json(mock.subList(params.offset, (params.offset + params.limit).coerceAtMost(mock.size)))
    }

    fun find(ctx: Context) {
        ctx.json(mock[ctx.pathParam("id").toInt()])
    }

    fun findByEmail(ctx: Context) {
        val search = ctx.queryParam("email");
        val user = mock.find {
            it.email == search
        }

        if(user != null) {
            ctx.json(user)
        } else {
            ctx.status(404)
        }
    }

    fun create(ctx: Context) {
        val user = ctx.body<User>()
    }

}