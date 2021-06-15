package fr.goodfood

import fr.goodfood.controller.GeneralController
import fr.goodfood.controller.OrderController
import fr.goodfood.controller.RestaurantController
import fr.goodfood.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.security.SecurityUtil.roles
import javalinjwt.JWTAccessManager

fun main() {
    val app = Javalin.create {
        it.accessManager(JWTAccessManager("role", mapping, Role.ANONYMOUS))
    }

    val generator = Auth.setup(app)

    app.routes {
        get("/version", GeneralController::version, roles(Role.ANONYMOUS))
        path("/v1") {
            post("/token", generator, roles(Role.ANONYMOUS))
            delete("/token", GeneralController::revoke, roles(Role.ANONYMOUS))
            post("/users/reset", GeneralController::resetRequest, roles(Role.ANONYMOUS))
            put("/users/reset", GeneralController::resetPassword, roles(Role.ANONYMOUS))

            get("/users", UserController::list, roles(Role.USER))
            get("/users/:id", UserController::find, roles(Role.USER))
            get("/users/search", UserController::findByEmail, roles(Role.USER))
            post("/users", UserController::create, roles(Role.USER))
            patch("/users/:id", UserController::edit, roles(Role.USER))
            delete("/users/:id", UserController::delete, roles(Role.USER))

            get("/restaurants", RestaurantController::list, roles(Role.USER))
            get("/restaurants/:id", RestaurantController::find, roles(Role.USER))
            post("/restaurants/search", RestaurantController::search, roles(Role.USER))
            post("/restaurants", RestaurantController::create, roles(Role.USER))
            patch("/restaurants/:id", RestaurantController::edit, roles(Role.USER))
            delete("/restaurants/:id", RestaurantController::delete, roles(Role.USER))
            post("/restaurants/:restaurant/products", RestaurantController::addProduct, roles(Role.USER))
            patch("/restaurants/:restaurant/products/:product", RestaurantController::editProduct, roles(Role.USER))
            delete("/restaurants/:restaurant/products/:product", RestaurantController::deleteProduct, roles(Role.USER))

            get("/restaurant/:restaurant/orders", OrderController::list, roles(Role.USER))
            post("/restaurant/:restaurant/orders", OrderController::create, roles(Role.USER))
            get("/orders/:id", OrderController::find, roles(Role.USER))
            patch("/orders/:number", OrderController::edit, roles(Role.USER))
        }
    }

    app.start(8080)
}