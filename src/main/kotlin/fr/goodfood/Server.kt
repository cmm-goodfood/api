package fr.goodfood

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import fr.goodfood.controller.GeneralController
import fr.goodfood.controller.OrderController
import fr.goodfood.controller.RestaurantController
import fr.goodfood.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.security.SecurityUtil.roles
import javalinjwt.JWTAccessManager
import javalinjwt.JavalinJWT

fun main() {
    val app = Javalin.create {
        it.accessManager(JWTAccessManager("role", mapping, Role.ANONYMOUS))
    }

    val generator = Auth.setup(app);

    app.routes {
        get("/version", GeneralController::version, roles(Role.ANONYMOUS))
        path("/v1") {
            get("/token", generator, roles(Role.ANONYMOUS))
            delete("/token", GeneralController::revoke, roles(Role.ANONYMOUS))
            post("/user/reset", GeneralController::resetRequest, roles(Role.ANONYMOUS))
            put("/user/reset", GeneralController::resetPassword, roles(Role.ANONYMOUS))

            get("/users", UserController::list, roles(Role.USER))
            get("/user/:id", UserController::find, roles(Role.USER))
            get("/user", UserController::findByEmail, roles(Role.USER))
            post("/users", UserController::create, roles(Role.USER))
            patch("/user/:id", UserController::edit, roles(Role.USER))
            delete("/user/:id", UserController::delete, roles(Role.USER))

            get("/restaurants", RestaurantController::list, roles(Role.USER))
            get("/restaurant/:id", RestaurantController::find, roles(Role.USER))
            post("/restaurants/search", RestaurantController::search, roles(Role.USER))
            post("/restaurants", RestaurantController::create, roles(Role.USER))
            patch("/restaurant/:id", RestaurantController::edit, roles(Role.USER))
            delete("/restaurant/:id", RestaurantController::delete, roles(Role.USER))
            post("/restaurants/:restaurant/products", RestaurantController::addProduct, roles(Role.USER))
            patch("/restaurants/:restaurant/products/:product", RestaurantController::editProduct, roles(Role.USER))
            delete("/restaurants/:restaurant/products/:product", RestaurantController::deleteProduct, roles(Role.USER))

            get("/orders/:restaurant", OrderController::list, roles(Role.USER))
            get("/order/:id", OrderController::find, roles(Role.USER))
            post("/orders/:restaurant", OrderController::create, roles(Role.USER))
        }
    }
    //val jwt = JavalinJWT.getDecodedFromContext(ctx)

    app.start(80)
}