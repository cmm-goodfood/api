package fr.goodfood

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.Context

fun main() {
    val server = Javalin.create {
        it.accessManager(Auth::accessManager)
    }

    server.routes {
        ApiBuilder.get("/") { it.result("Hello World") }
        ApiBuilder.get("/unsecured", { it.result("Hello") }, roles(Role.ANONYMOUS))
        ApiBuilder.get("/secured", { it.result("Hello") }, roles(Role.USER))
    }

    server.start(7000)
}