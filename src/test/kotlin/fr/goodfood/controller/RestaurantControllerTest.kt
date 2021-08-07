package fr.goodfood.controller

import io.javalin.http.Context
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class RestaurantControllerTest {

    private val ctx = mockk<Context>(relaxed = true)

    @Test
    fun `GET the list of restaurants`() {
        RestaurantController.list(ctx)
        verify { ctx.status(201) }
    }

}