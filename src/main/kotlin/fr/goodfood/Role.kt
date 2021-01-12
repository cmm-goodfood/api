package fr.goodfood

import io.javalin.core.security.Role

enum class Role : Role {
    ANONYMOUS, USER, DELIVERY, RESTAURANT, MODERATOR
}