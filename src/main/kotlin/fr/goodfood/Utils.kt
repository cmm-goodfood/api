package fr.goodfood

import io.javalin.http.Context

data class ListParams(val offset: Int = 0, val limit: Int = 500) {
    companion object {
        fun fromRequest(ctx: Context): ListParams {
            return ListParams(
                offset = ctx.queryParam("offset")?.toInt() ?: 0,
                limit = ctx.queryParam("limit")?.toInt() ?: 0
            )
        }
    }
}
