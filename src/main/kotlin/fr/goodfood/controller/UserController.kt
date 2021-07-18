package fr.goodfood.controller

import fr.goodfood.Mailer
import fr.goodfood.Role
import fr.goodfood.database.Database
import fr.goodfood.entities.User
import fr.goodfood.randomString
import io.javalin.http.Context
import javalinjwt.JavalinJWT

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

        val role = Role.valueOf(JavalinJWT.getDecodedFromContext(ctx)
            .getClaim("role")
            .asString())

        user.password = null;
        user.token = randomString(16);
        if(role != Role.FRANCHISES_MANAGER) {
            user.role = Role.CLIENT
        }

        Database.insert(user)
        Mailer.send(user, "Compte créé", "new_account.html", hashMapOf(
            "email" to user.email!!,
            "firstname" to user.firstname!!,
            "lastname" to user.lastname!!,
            "token" to user.token!!
        ))

        ctx.status(200)
    }

    data class ConfirmPassword(val email: String, val token: String, val password: String)

    fun confirm(ctx: Context) {
        val confirmation = ctx.body<ConfirmPassword>()
        val user = Database.first<User> { it.email == confirmation.email }

        if(user == null) {
            ctx.status(404);
            return
        }

        if(confirmation.token == user.token) {
            user.password = confirmation.password
        }

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
        existing.email = user.email ?: existing.email
        existing.firstname = user.firstname ?: existing.firstname
        existing.lastname = user.lastname ?: existing.lastname
        existing.password = user.password ?: existing.password
        existing.address = user.address ?: existing.address

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