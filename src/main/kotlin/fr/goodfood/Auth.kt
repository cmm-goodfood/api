package fr.goodfood

import io.javalin.core.security.Role as JavalinRole
import io.javalin.http.Context
import io.javalin.http.Handler

object Auth {

    private fun extractRole(ctx: Context): Role {
        return Role.ANONYMOUS
    }

    fun accessManager(handler: Handler, ctx: Context, roles: Set<JavalinRole>) {
        if (roles.isEmpty() || roles.contains(extractRole(ctx))) {
            handler.handle(ctx)
        } else {
            ctx.status(401).result("Unauthorized")
        }
    }
}
