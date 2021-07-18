package fr.goodfood

import fr.goodfood.controller.GeneralController
import fr.goodfood.controller.OrderController
import fr.goodfood.controller.RestaurantController
import fr.goodfood.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import javalinjwt.JWTAccessManager

fun main() {
    val app = Javalin.create {
        it.accessManager(JWTAccessManager("role", mapping, Role.ANONYMOUS))
    }

    val generator = Auth.setup(app)

    app.routes {
        get("/", { it.html("API Goodfood") }, ANONYMOUS)
        get("/version", GeneralController::version, ANONYMOUS)
        path("/v1") {
            post("/token", generator, ANONYMOUS)
            delete("/token", GeneralController::revoke, ANONYMOUS)
            post("/users/reset", GeneralController::resetRequest, ANONYMOUS)
            put("/users/reset", GeneralController::resetPassword, ANONYMOUS)

            get("/users", UserController::list, FRANCHISES_MANAGER)
            get("/users/:id", UserController::find, CLIENT)
            get("/users/search", UserController::findByEmail, FRANCHISE)
            post("/users", UserController::create, ANONYMOUS)
            post("/users/confirm", UserController::confirm, ANONYMOUS)
            patch("/users/:id", UserController::edit, CLIENT)
            delete("/users/:id", UserController::delete, CLIENT)

            get("/restaurants", RestaurantController::list, CLIENT)
            get("/restaurants/:id", RestaurantController::find, CLIENT)
            post("/restaurants/search", RestaurantController::search, ANONYMOUS)
            post("/restaurants", RestaurantController::create, FRANCHISES_MANAGER)
            patch("/restaurants/:id", RestaurantController::edit, FRANCHISE)
            delete("/restaurants/:id", RestaurantController::delete, FRANCHISES_MANAGER)
            post("/restaurants/:restaurant/products", RestaurantController::addProduct, FRANCHISE)
            patch("/restaurants/:restaurant/products/:product", RestaurantController::editProduct, FRANCHISE)
            delete("/restaurants/:restaurant/products/:product", RestaurantController::deleteProduct, FRANCHISE)

            get("/restaurants/:restaurant/orders", OrderController::list, CLIENT)
            post("/restaurants/:restaurant/orders", OrderController::create, CLIENT)
            get("/orders/:id", OrderController::find, CLIENT)
            patch("/orders/:number", OrderController::edit, CLIENT)
        }
    }

    app.start(80)
}