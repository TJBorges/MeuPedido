package com.app.meupedido.util

class DataStore() {

    fun name(cod: String): String {
        return when (cod) {
            "BOB" -> return "Bob's"
            "BKG" -> return "Burger King"
            "GRF" -> return "Giraffas"
            "HAB" -> return "Habib's"
            "KFC" -> return "KFC"
            "MCD" -> return "McDonald's"
            "PZH" -> return "Pizza Hut"
            "SPL" -> return "Spoleto"
            "STB" -> return "Starbucks"
            "SBW" -> return "Subway"
            "VVC" -> return "Vivenda do Camarão"
            else -> ""
        }
    }

    fun logo(cod: String): Int {
        return when (cod) {
            "BOB" -> return 1
            "BKG" -> return 2
            "GRF" -> return 3
            "HAB" -> return 4
            "KFC" -> return 5
            "MCD" -> return 6
            "PZH" -> return 7
            "SPL" -> return 8
            "STB" -> return 9
            "SBW" -> return 10
            "VVC" -> return 11
            else -> 0
        }
    }

    fun logoName(cod: String): String {
        return when (cod) {
            "BOB" -> return "bobs"
            "BKG" -> return "burguer_king"
            "GRF" -> return "giraffas"
            "HAB" -> return "habibs"
            "KFC" -> return "kfc"
            "MCD" -> return "mcdonalds"
            "PZH" -> return "pizza_hut"
            "SPL" -> return "spoleto"
            "STB" -> return "starbucks"
            "SBW" -> return "subway"
            "VVC" -> return "vivenda_do_camarao"
            else -> ""
        }
    }
}