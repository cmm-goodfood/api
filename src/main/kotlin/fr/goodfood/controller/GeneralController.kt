package fr.goodfood.controller

import fr.goodfood.Auth
import fr.goodfood.Mailer
import fr.goodfood.database.Database
import fr.goodfood.entities.User
import io.javalin.http.Context

object GeneralController {

    data class Version(val number: Int, val status: String, val default: Boolean)
    data class VersionResponse(val current: Int, val default: Int, val versions: List<Version>)

    fun version(ctx: Context) {
        ctx.json(
            VersionResponse(
                current = 1,
                default = 1,
                versions = arrayListOf(
                    Version(number = 1, status = "disponible", default = true)
                )
            )
        )
    }

    data class Email(val email: String)

    fun resetRequest(ctx: Context) {
        val data = ctx.body<Email>()

        val user = Database.first<User> {
            it.email == data.email
        }

        if(user != null) {
            Mailer.send(user, "Mot de passe oublié", "password_forgotten.html")
        }

        ctx.status(200)
    }

    data class ResetPassword(val token: String, val id: Int, val password: String)

    fun resetPassword(ctx: Context) {
        val data = ctx.body<ResetPassword>()
        val existing = Database.get<User>(data.id)

        if (existing == null) {
            ctx.status(404)
            return
        }

        existing.password = JHash.hash(data.password)

        ctx.status(200)
    }

    data class Revoke(val token: String)

    fun revoke(ctx: Context) {
        val data = ctx.body<Revoke>()

        Auth.revoked.add(data.token)

        ctx.status(200)
    }

}