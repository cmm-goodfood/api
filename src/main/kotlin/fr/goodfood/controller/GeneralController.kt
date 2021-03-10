package fr.goodfood.controller

import fr.goodfood.entities.User
import io.javalin.http.Context
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import java.io.InputStream


object GeneralController {

    data class Version(val number: Int, val status: String, val default: Boolean);
    data class VersionResponse(val current: Int, val default: Int, val versions: List<Version>);

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

    data class Email(val email: String);

    fun resetRequest(ctx: Context) {
        val data = ctx.body<Email>();

        val email = EmailBuilder.startingBlank()
            .from("goodfood", "goodfood@goodfood.com")
            .to(data.email, data.email)
            .withSubject("Mail")
            .withHTMLText("<p>Mail</p>")
            .buildEmail()

        val mailer = MailerBuilder
            .withSMTPServer("smtp.mailtrap.io", 587, "e10154a96602b7", "a44207f04588d1")
            .buildMailer();

        mailer.sendMail(email);

        ctx.result(String())
    }

    data class ResetPassword(val token: String, val id: Int, val password: String)

    fun resetPassword(ctx: Context) {
        val data = ctx.body<ResetPassword>()

        val existing = UserController.mock.find {
            it.id == data.id
        }

        if(existing == null) {
            ctx.status(404)
            return
        }

        existing.password = data.password

        ctx.result(String())
    }

    data class Revoke(val token: String)

    fun revoke(ctx: Context) {
        val data = ctx.body<Revoke>()

        //TODO: revoke token

        ctx.result(String())
    }

}