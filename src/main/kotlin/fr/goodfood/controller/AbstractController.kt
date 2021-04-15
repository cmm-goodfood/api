package fr.goodfood.controller

import fr.goodfood.ListParams
import fr.goodfood.database.Database
import fr.goodfood.entities.Identified
import io.javalin.http.Context

object AbstractController {

    inline fun <reified T : Identified> list(ctx: Context) {
        val items = Database.data<T>()
        val params = ListParams.fromRequest(ctx)

        ctx.json(items.subList(params.offset, (params.offset + params.limit).coerceAtMost(items.size)))
    }

    inline fun <reified T : Identified> find(ctx: Context) {
        val id = ctx.pathParam("id").toInt()
        val item = Database.data<T>().find {
            it.id == id
        }

        if (item == null) {
            ctx.status(404)
        } else {
            ctx.json(item)
        }
    }

}