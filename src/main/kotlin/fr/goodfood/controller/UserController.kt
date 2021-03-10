package fr.goodfood.controller

import fr.goodfood.ListParams
import fr.goodfood.Role
import fr.goodfood.entities.User
import io.javalin.Javalin
import io.javalin.core.validation.BodyValidator
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.plugin.json.JavalinJson
import java.util.*

object UserController {

    private var id = 5
    private val mock = arrayListOf(
        User(id = 1, email = "clement.curiel@viacesi.fr", firstname = "Clément", lastname = "CURIEL", role = Role.RESTAURANT),
        User(id = 2, email = "matteo.hevin@viacesi.fr", firstname = "Matteo", lastname = "HEVIN", role = Role.DELIVERY),
        User(id = 3, email = "pierre.pegeon@viacesi.fr", firstname = "Pierre", lastname = "PEGEON", role = Role.MODERATOR),
        User(id = 4, email = "marwane.benoukaiss@viacesi.fr", firstname = "Marwane", lastname = "BENOU-KAÏSS", role = Role.USER)
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

        if(user == null) {
            ctx.status(404)
        } else {
            ctx.json(user)
        }
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
        user.id = id++
        user.role = Role.USER

        mock.add(user)

        ctx.status(200)
    }

    fun edit(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val existing = mock.find {
            it.id == id
        }

        if(existing == null) {
            ctx.status(404)
            return
        }

        val user = ctx.body<User>()
        existing.email = user.email
        existing.firstname = user.firstname
        existing.lastname = user.lastname
        existing.password = user.password
        existing.address = user.address

        ctx.status(200)
    }

    fun delete(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val removed = mock.removeIf {
            it.id == id
        }

        if(removed) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

}