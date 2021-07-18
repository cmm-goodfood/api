package fr.goodfood

import io.javalin.http.Context
import java.util.*
import kotlin.streams.asSequence

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

fun randomString(length: Long): String {
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    return Random().ints(length, 0, source.length)
        .asSequence()
        .map(source::get)
        .joinToString("")
}