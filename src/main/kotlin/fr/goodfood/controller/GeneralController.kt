package fr.goodfood.controller

import io.javalin.http.Context
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
        val email = ctx.body<Email>();

        //TODO: generate token and send password reset email

        ctx.result(InputStream.nullInputStream())
    }

    data class ResetPassword(val token: String, val email: String, val password: String);

    fun resetPassword(ctx: Context) {
        val data = ctx.body<ResetPassword>()

        //TODO: change user password and send email

        ctx.result(InputStream.nullInputStream())
    }

    data class Revoke(val token: String)

    fun revoke(ctx: Context) {
        val data = ctx.body<Revoke>()

        //TODO: revoke token

        ctx.result(InputStream.nullInputStream())
    }

}