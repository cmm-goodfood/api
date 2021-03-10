package fr.goodfood

import io.javalin.core.security.Role

enum class Role : Role {
    ANONYMOUS, USER, DELIVERY, RESTAURANT, MODERATOR
}

val mapping: Map<String, fr.goodfood.Role> = hashMapOf(
    fr.goodfood.Role.ANONYMOUS.name to fr.goodfood.Role.ANONYMOUS,
    fr.goodfood.Role.USER.name to fr.goodfood.Role.USER,
    fr.goodfood.Role.MODERATOR.name to fr.goodfood.Role.MODERATOR
);