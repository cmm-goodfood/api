package fr.goodfood

enum class Role : io.javalin.core.security.Role {
    ANONYMOUS, CLIENT, FRANCHISE, FRANCHISES_MANAGER
}

val ANONYMOUS = setOf(Role.ANONYMOUS, Role.CLIENT, Role.FRANCHISE, Role.FRANCHISES_MANAGER)
val CLIENT = setOf(Role.CLIENT, Role.FRANCHISE, Role.FRANCHISES_MANAGER)
val FRANCHISE = setOf(Role.FRANCHISE, Role.FRANCHISES_MANAGER)
val FRANCHISES_MANAGER = setOf(Role.FRANCHISES_MANAGER)

val mapping: Map<String, Role> = hashMapOf(
    Role.ANONYMOUS.name to Role.ANONYMOUS,
    Role.CLIENT.name to Role.CLIENT,
    Role.FRANCHISES_MANAGER.name to Role.FRANCHISES_MANAGER
);