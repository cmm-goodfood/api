package fr.goodfood.database

import fr.goodfood.Role
import fr.goodfood.entities.*
import kotlin.reflect.KClass

object Database {

    val id = HashMap<KClass<out Identified>, Int>()

    val data = hashMapOf<KClass<out Identified>, ArrayList<out Identified>>(
        Address::class to ArrayList(),
        User::class to ArrayList(),
        Product::class to ArrayList(),
        Restaurant::class to ArrayList(),
        Order::class to ArrayList()
    )

    init {
        val coursAlbret = Address(
            number = "8",
            street = "Cours d'Albret",
            city = "Bordeaux",
            postcode = "33000"
        )

        val eightOfMay = Address(
            number = "13",
            street = "rue du 8 mai 1945",
            addition = "Rés. Bérénice, Bat. C, Appt. 205",
            city = "Cenon",
            postcode = "33150"
        )

        insert(coursAlbret, eightOfMay)

        insert(User(
            email = "clement.curiel@viacesi.fr",
            firstname = "Clément",
            lastname = "CURIEL",
            role = Role.RESTAURANT
        ))

        insert(User(email = "matteo.hevin@viacesi.fr", firstname = "Matteo", lastname = "HEVIN", role = Role.DELIVERY))

        insert(User(
            email = "pierre.pegeon@viacesi.fr",
            firstname = "Pierre",
            lastname = "PEGEON",
            role = Role.MODERATOR
        ))

        val mbenoukaiss = User(
            email = "marwane.benoukaiss@viacesi.fr",
            firstname = "Marwane",
            lastname = "BENOU-KAÏSS",
            role = Role.USER,
            address = eightOfMay,
            password = "oke"
        )

        insert(mbenoukaiss)

        val waterBottle = Product(
            category = ProductCategory.DRINK,
            name = "Bouteille d'eau",
            description = "Contient 50cl d'eau",
            price = 49.99,
            quantity = 55
        )

        insert(waterBottle)

        val eatSalad = Restaurant(
            name = "EatSalad",
            description = "Salade sur mesure à emporter et en livraison",
            shortDescription = "Salade sur mesure à emporter et en livraison",
            address = coursAlbret,
            deliveryRadius = 30,
            location = Location("44.838717", "-0.581660"),
            products = arrayListOf(waterBottle)
        )

        insert(eatSalad)

        insert(Order(
            type = OrderType.TAKEAWAY,
            state = OrderState.DELIVERED,
            restaurant = eatSalad,
            user = mbenoukaiss,
            address = eightOfMay,
            products = arrayListOf(waterBottle),
            time = "21/09/2021 14:32:20"
        ))
    }

    inline fun <reified T: Identified> data(): ArrayList<T> {
        return data[T::class] as ArrayList<T>
    }

    inline fun <reified T: Identified> nextIndex(): Int {
        val i = id[T::class] ?: 0
        id[T::class] = i + 1

        return i + 1
    }

    inline fun <reified T: Identified> get(id: Int): T? {
        return data<T>().find {
            it.id == id
        }
    }

    inline fun <reified T: Identified> filter(predicate: (T) -> Boolean): List<T> {
        return data<T>().filter(predicate)
    }

    inline fun <reified T: Identified> first(predicate: (T) -> Boolean): T? {
        return data<T>().firstOrNull(predicate)
    }

    inline fun <reified T: Identified> insert(vararg items: T) {
        val list = data<T>()

        for (item in items) {
            item.id = nextIndex<T>()
            list.add(item)
        }
    }

    inline fun <reified T: Identified> remove(id: Int): Boolean {
        return data<T>().removeIf {
            it.id == id
        }
    }

}