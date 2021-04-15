package fr.goodfood.controller

import fr.goodfood.Role
import fr.goodfood.database.Database
import fr.goodfood.entities.User
import io.javalin.http.Context

object UserController {

    fun list(ctx: Context) {
        AbstractController.list<User>(ctx)
    }

    fun find(ctx: Context) {
        AbstractController.find<User>(ctx)
    }

    fun findByEmail(ctx: Context) {
        val search = ctx.queryParam("email")
        val user = Database.filter<User> {
            it.email == search
        }

        if (user.size == 1) {
            ctx.json(user)
        } else {
            ctx.status(404)
        }
    }

    fun create(ctx: Context) {
        val user = ctx.body<User>()
        user.role = Role.USER

        Database.insert(user)

        ctx.status(200)
    }

    fun edit(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val existing = Database.get<User>(id)

        if (existing == null) {
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
        val removed = Database.remove<User>(id)

        if (removed) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

}