package fr.goodfood

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import fr.goodfood.database.Database
import fr.goodfood.entities.User
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.Handler
import javalinjwt.JWTGenerator
import javalinjwt.JWTProvider
import javalinjwt.JavalinJWT
import javalinjwt.examples.JWTResponse
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

object Auth {

    val revoked = HashSet<String>()

    data class GenerateToken(val email: String, val password: String)

    fun setup(app: Javalin): Handler {
        val algorithm = Algorithm.HMAC256("verysecretrecstsfdeuzfzoeifjzeoih")

        val generator = JWTGenerator<User> { user, alg ->
            val token: JWTCreator.Builder = JWT.create()
                .withClaim("email", user.email)
                .withClaim("role", user.role?.name)
            token.sign(alg)
        }

        val verifier: JWTVerifier = JWT.require(algorithm).build()

        val provider = JWTProvider(algorithm, generator, verifier)

        app.before {
            JavalinJWT.getTokenFromHeader(it)
                .flatMap { token -> if(!revoked.contains(token)) Optional.of(token) else Optional.empty() }
                .flatMap { token -> provider.validateToken(token) }
                .ifPresent { jwt -> JavalinJWT.addDecodedToContext(it, jwt) }

        }

        return Handler { context: Context ->
            val data = context.body<GenerateToken>()

            val existing = Database.first<User> {
                it.email == data.email
            }

            if (existing != null && existing.password == data.password) {
                val user = User(
                    id = existing.id,
                    role = existing.role,
                    email = data.email,
                    password = data.password
                )

                context.json(JWTResponse(provider.generateToken(user)))
            } else {
                context.status(401)
            }
        }
    }

}
